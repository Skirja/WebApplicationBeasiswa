package com.beasiswa.controller;

import com.beasiswa.dao.UserDAO;
import com.beasiswa.dao.BerkasDAO;
import com.beasiswa.model.Berkas;
import com.beasiswa.model.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private UserDAO userDAO;
    private BerkasDAO berkasDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        berkasDAO = new BerkasDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        try {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null || !"admin".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/auth/login");
                return;
            }

            if (action == null || "/".equals(action) || "/dashboard".equals(action)) {
                showDashboard(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int page = 1;
        int recordsPerPage = 10;
        String search = request.getParameter("search");
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        int offset = (page - 1) * recordsPerPage;
        List<User> userList = userDAO.getAllUsers(offset, recordsPerPage, search, sortBy, sortOrder);
        int totalRecords = userDAO.getTotalUsers(search);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        // Fetch berkas for each user
        Map<Integer, List<Berkas>> userBerkasMap = new HashMap<>();
        for (User u : userList) {
            List<Berkas> berkasList = berkasDAO.getAllBerkasByUserId(u.getUserId());
            userBerkasMap.put(u.getUserId(), berkasList);
        }

        request.setAttribute("userList", userList);
        request.setAttribute("userBerkasMap", userBerkasMap);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", search);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("sortOrder", sortOrder);

        request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if ("/verifikasi".equals(action)) {
            verifikasiBerkas(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void verifikasiBerkas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String berkasIdParam = request.getParameter("berkasId");
        String status = request.getParameter("status"); 
        
        if (berkasIdParam == null || status == null || status.isEmpty()) {
            sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "{\"success\": false, \"message\": \"Parameter tidak valid\"}");
            return;
        }

        try {
            int berkasId = Integer.parseInt(berkasIdParam);
            if (!status.equals("Terverifikasi") && !status.equals("Ditolak")) {
                sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "{\"success\": false, \"message\": \"Status tidak valid\"}");
                return;
            }
            berkasDAO.updateBerkasStatus(berkasId, status);
            sendJsonResponse(response, HttpServletResponse.SC_OK, "{\"success\": true, \"message\": \"Status berkas berhasil diperbarui\"}");
        } catch (NumberFormatException e) {
            sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "{\"success\": false, \"message\": \"ID berkas tidak valid\"}");
        } catch (SQLException e) {
            sendJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"success\": false, \"message\": \"Gagal memperbarui status berkas: " + e.getMessage() + "\"}");
        }
    }

    private void sendJsonResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(message);
    }
}