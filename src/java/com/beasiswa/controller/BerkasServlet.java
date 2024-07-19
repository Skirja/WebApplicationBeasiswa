package com.beasiswa.controller;

import com.beasiswa.dao.BerkasDAO;
import com.beasiswa.model.Berkas;
import com.beasiswa.model.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/berkas/*")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5, // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class BerkasServlet extends HttpServlet {

    private BerkasDAO berkasDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        berkasDAO = new BerkasDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        if ("/list".equals(action)) {
            listBerkas(request, response);
        } else if ("/download".equals(action)) {
            downloadBerkas(request, response);
        } else if ("/getBerkasMahasiswa".equals(action)) {
            getBerkasMahasiswa(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        if ("/upload".equals(action)) {
            uploadBerkas(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listBerkas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        int page = 1;
        int recordsPerPage = 5;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        try {
            List<Berkas> berkasList = berkasDAO.getBerkasByUserId(user.getUserId(), page, recordsPerPage);
            int totalRecords = berkasDAO.getTotalBerkasByUserId(user.getUserId());
            int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

            request.setAttribute("berkasList", berkasList);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);

            request.getRequestDispatcher("/user/berkas.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving berkas", e);
        }
    }

    private void downloadBerkas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int berkasId = Integer.parseInt(request.getParameter("id"));
        User user = (User) request.getSession().getAttribute("user");

        try {
            Berkas berkas = berkasDAO.getBerkasById(berkasId);
            if (berkas == null || (!"admin".equals(user.getRole()) && berkas.getUserId() != user.getUserId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            response.setContentType(berkas.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + berkas.getNamaBerkas() + "\"");
            response.getOutputStream().write(berkas.getFileContent());
        } catch (SQLException e) {
            throw new ServletException("Error downloading berkas", e);
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

        try (InputStream fileContent = filePart.getInputStream()) {
            Berkas berkas = new Berkas();
            berkas.setUserId(user.getUserId());
            berkas.setNamaBerkas(fileName);
            berkas.setTanggalUpload(new Date());
            berkas.setStatusVerifikasi("Belum Diverifikasi");
            berkas.setFileContent(fileContent.readAllBytes());
            berkas.setFileType(fileType);

            berkasDAO.addBerkas(berkas);
            response.sendRedirect(request.getContextPath() + "/berkas/list");
        } catch (SQLException e) {
            throw new ServletException("Error uploading berkas", e);
        }
    }

    private void getBerkasMahasiswa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdParam = request.getParameter("userId");
        if (userIdParam == null || userIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"User ID tidak valid atau tidak diberikan\"}");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);
            List<Map<String, Object>> detailBerkasList = berkasDAO.getDetailBerkasByUserId(userId);
            String json = new Gson().toJson(detailBerkasList);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"User ID tidak valid\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Gagal mengambil data berkas: " + e.getMessage() + "\"}");
        }
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