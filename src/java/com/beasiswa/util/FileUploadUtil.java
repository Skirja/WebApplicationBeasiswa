package com.beasiswa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

public class FileUploadUtil {

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.home") + File.separator + "beasiswa_files" + File.separator;

    public static String saveFile(Part filePart, String subDirectory) throws IOException {
        String fileName = getSubmittedFileName(filePart);
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        String uploadPath = UPLOAD_DIRECTORY + subDirectory;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        Path filePath = Paths.get(uploadPath + File.separator + uniqueFileName);
        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return uniqueFileName;
    }

    public static void downloadFile(HttpServletResponse response, String filePath, String fileName) throws IOException {
        File file = new File(UPLOAD_DIRECTORY + filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLength((int) file.length());

        try (InputStream inputStream = new FileInputStream(file); OutputStream outStream = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private static String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}