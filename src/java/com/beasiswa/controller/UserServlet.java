package com.beasiswa.controller;

import com.beasiswa.dao.BerkasDAO;
import com.beasiswa.dao.UserDAO;
import com.beasiswa.model.Berkas;
import com.beasiswa.model.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/user/*")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5, // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class UserServlet extends HttpServlet {

    private BerkasDAO berkasDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        berkasDAO = new BerkasDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        try {
            if (action == null || "/".equals(action) || "/dashboard".equals(action)) {
                showDashboard(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            System.err.println("Error in UserServlet doGet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        try {
            int page = 1;
            int recordsPerPage = 10;
            if (request.getParameter("page") != null) {
                page = Math.max(1, Integer.parseInt(request.getParameter("page")));
            }

            int offset = Math.max(0, (page - 1) * recordsPerPage);
            List<Berkas> berkasList = berkasDAO.getBerkasByUserId(user.getUserId(), offset, recordsPerPage);
            int totalRecords = berkasDAO.getTotalBerkasByUserId(user.getUserId());
            int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
            
            request.setAttribute("berkasList", berkasList);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("/WEB-INF/user/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving berkas list", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        if ("/upload".equals(action)) {
            uploadBerkas(request, response);
        } else if ("/updateFotoProfil".equals(action)) {
            updateFotoProfil(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void uploadBerkas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Part filePart = request.getPart("berkas");
        String fileName = getSubmittedFileName(filePart);
        String fileType = filePart.getContentType();

        try {
            // Simpan file fisik
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String filePath = uploadPath + File.separator + fileName;
            try (InputStream fileContent = filePart.getInputStream();
                 OutputStream out = new FileOutputStream(filePath)) {
                out.write(fileContent.readAllBytes());
            }

            // Buat objek Berkas
            Berkas berkas = new Berkas();
            berkas.setUserId(user.getUserId());
            berkas.setNamaBerkas(fileName);
            berkas.setPathBerkas("uploads/" + fileName);
            berkas.setTanggalUpload(new Date());
            berkas.setStatusVerifikasi("Belum Diverifikasi");
            berkas.setFileType(fileType);

            // Simpan berkas ke database
            berkasDAO.addBerkas(berkas);

            response.sendRedirect(request.getContextPath() + "/user/dashboard");
        } catch (SQLException e) {
            throw new ServletException("Error uploading berkas", e);
        }
    }

    private void updateFotoProfil(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Part filePart = request.getPart("newFotoProfil");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (!"jpg".equals(fileExtension) && !"jpeg".equals(fileExtension) && !"png".equals(fileExtension)) {
                request.setAttribute("error", "Format foto profil tidak valid. Gunakan JPG, JPEG, atau PNG.");
                showDashboard(request, response);
                return;
            }
            if (filePart.getSize() > 5 * 1024 * 1024) { // 5 MB limit
                request.setAttribute("error", "Ukuran foto profil terlalu besar. Maksimum 5 MB.");
                showDashboard(request, response);
                return;
            }
            try (InputStream fileContent = filePart.getInputStream()) {
                user.setFotoProfilContent(fileContent.readAllBytes());
                user.setFotoProfilType(filePart.getContentType());
            }

            try {
                userDAO.updateUserFotoProfil(user);
                request.setAttribute("success", "Foto profil berhasil diperbarui.");
            } catch (SQLException e) {
                request.setAttribute("error", "Terjadi kesalahan saat memperbarui foto profil.");
            }
        } else {
            request.setAttribute("error", "Tidak ada file yang dipilih.");
        }
        showDashboard(request, response);
    }

    private String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}