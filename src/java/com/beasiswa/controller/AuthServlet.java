package com.beasiswa.controller;

import com.beasiswa.dao.JenisBeasiswaDAO;
import com.beasiswa.model.JenisBeasiswa;
import com.beasiswa.dao.UserDAO;
import com.beasiswa.model.User;
import com.beasiswa.util.FileUploadUtil;
import java.io.IOException;
import java.time.Year;
import java.util.Random;
import java.util.List;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.InputStream;

@WebServlet("/auth/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 5, // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class AuthServlet extends HttpServlet {

    private UserDAO userDAO;
    private JenisBeasiswaDAO jenisBeasiswaDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        jenisBeasiswaDAO = new JenisBeasiswaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        if ("/login".equals(action)) {
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else if ("/register".equals(action)) {
            showRegistrationForm(request, response);
        } else if ("/logout".equals(action)) {
            logout(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        if ("/login".equals(action)) {
            login(request, response);
        } else if ("/register".equals(action)) {
            register(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = userDAO.getUserByEmail(email);
            if (user != null && user.getPassword().equals(password)) {
                request.getSession().setAttribute("user", user);
                if ("admin".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/user/dashboard");
                }
            } else {
                request.setAttribute("error", "Email atau password salah");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Error during login", e);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = new User();
        user.setNamaLengkap(request.getParameter("namaLengkap"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        user.setRole("mahasiswa");
        user.setNomorPendaftaran(generateNomorPendaftaran());
        user.setSemester(Integer.parseInt(request.getParameter("semester")));
        user.setNomorTelepon(request.getParameter("nomorTelepon"));
        user.setAlamatLengkap(request.getParameter("alamatLengkap"));
        user.setJenisBeasiswaId(Integer.parseInt(request.getParameter("jenisBeasiswaId")));

        Part filePart = request.getPart("fotoProfil");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (!"jpg".equals(fileExtension) && !"jpeg".equals(fileExtension) && !"png".equals(fileExtension)) {
                request.setAttribute("error", "Format foto profil tidak valid. Gunakan JPG, JPEG, atau PNG.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            if (filePart.getSize() > 5 * 1024 * 1024) { // 5 MB limit
                request.setAttribute("error", "Ukuran foto profil terlalu besar. Maksimum 5 MB.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            try (InputStream fileContent = filePart.getInputStream()) {
                user.setFotoProfilContent(fileContent.readAllBytes());
                user.setFotoProfilType(filePart.getContentType());
            }
        }

        try {
            userDAO.registerUser(user);
            response.sendRedirect(request.getContextPath() + "/auth/login");
        } catch (Exception e) {
            request.setAttribute("error", "Terjadi kesalahan saat mendaftar. Silakan coba lagi.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private String generateNomorPendaftaran() {
        String prefix = "NP";
        String tahun = String.format("%04d", Year.now().getValue());
        String randomDigits = String.format("%04d", new Random().nextInt(10000));
        return prefix + tahun + randomDigits;
    }

    private void showRegistrationForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<JenisBeasiswa> jenisBeasiswaList = jenisBeasiswaDAO.getAllJenisBeasiswa();
            request.setAttribute("jenisBeasiswaList", jenisBeasiswaList);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving jenis beasiswa", e);
        }
    }
}