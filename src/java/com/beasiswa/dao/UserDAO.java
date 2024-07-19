package com.beasiswa.dao;

import com.beasiswa.model.User;
import com.beasiswa.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserDAO {

    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public void registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (nama_lengkap, email, password, role, nomor_pendaftaran, semester, nomor_telepon, alamat_lengkap, jenis_beasiswa_id, foto_profil_content, foto_profil_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getNamaLengkap());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getNomorPendaftaran());
            stmt.setInt(6, user.getSemester());
            stmt.setString(7, user.getNomorTelepon());
            stmt.setString(8, user.getAlamatLengkap());
            stmt.setInt(9, user.getJenisBeasiswaId());
            stmt.setBytes(10, user.getFotoProfilContent());
            stmt.setString(11, user.getFotoProfilType());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
    }

    public List<User> getAllUsers(int offset, int recordsPerPage, String search, String sortBy, String sortOrder) throws SQLException {
        List<User> users = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE role != 'admin'");
        
        if (search != null && !search.isEmpty()) {
            sql.append(" AND (nama_lengkap LIKE ? OR email LIKE ? OR nomor_pendaftaran LIKE ?)");
        }
        
        if (sortBy != null && !sortBy.isEmpty()) {
            sql.append(" ORDER BY ");
            switch (sortBy.toLowerCase()) {
                case "namalengkap":
                    sql.append("nama_lengkap");
                    break;
                case "email":
                    sql.append("email");
                    break;
                case "nomorpendaftaran":
                    sql.append("nomor_pendaftaran");
                    break;
                case "semester":
                    sql.append("semester");
                    break;
                default:
                    sql.append("user_id");
                    break;
            }
            if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
                sql.append(" DESC");
            } else {
                sql.append(" ASC");
            }
        }
        
        sql.append(" LIMIT ? OFFSET ?");
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
            }
            stmt.setInt(paramIndex++, recordsPerPage);
            stmt.setInt(paramIndex, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(extractUserFromResultSet(rs));
                }
            }
        }
        return users;
    }

    public int getTotalUsers(String search) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM users WHERE role != 'admin'");
        
        if (search != null && !search.isEmpty()) {
            sql.append(" AND (nama_lengkap LIKE ? OR email LIKE ? OR nomor_pendaftaran LIKE ?)");
        }
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
                stmt.setString(3, searchPattern);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setNamaLengkap(rs.getString("nama_lengkap"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setNomorPendaftaran(rs.getString("nomor_pendaftaran"));
        user.setSemester(rs.getInt("semester"));
        user.setNomorTelepon(rs.getString("nomor_telepon"));
        user.setAlamatLengkap(rs.getString("alamat_lengkap"));
        user.setJenisBeasiswaId(rs.getInt("jenis_beasiswa_id"));
        user.setFotoProfilContent(rs.getBytes("foto_profil_content"));
        user.setFotoProfilType(rs.getString("foto_profil_type"));
        return user;
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET nama_lengkap = ?, email = ?, password = ?, role = ?, " +
                     "nomor_pendaftaran = ?, semester = ?, nomor_telepon = ?, alamat_lengkap = ?, " +
                     "jenis_beasiswa_id = ?, foto_profil_content = ?, foto_profil_type = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getNamaLengkap());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getNomorPendaftaran());
            stmt.setInt(6, user.getSemester());
            stmt.setString(7, user.getNomorTelepon());
            stmt.setString(8, user.getAlamatLengkap());
            stmt.setInt(9, user.getJenisBeasiswaId());
            stmt.setBytes(10, user.getFotoProfilContent());
            stmt.setString(11, user.getFotoProfilType());
            stmt.setInt(12, user.getUserId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        }
    }

    public void updateUserFotoProfil(User user) throws SQLException {
        String sql = "UPDATE users SET foto_profil_content = ?, foto_profil_type = ? WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBytes(1, user.getFotoProfilContent());
            stmt.setString(2, user.getFotoProfilType());
            stmt.setInt(3, user.getUserId());
            stmt.executeUpdate();
        }
    }

    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setNamaLengkap(rs.getString("nama_lengkap"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    user.setNomorPendaftaran(rs.getString("nomor_pendaftaran"));
                    user.setSemester(rs.getInt("semester"));
                    user.setNomorTelepon(rs.getString("nomor_telepon"));
                    user.setAlamatLengkap(rs.getString("alamat_lengkap"));
                    user.setJenisBeasiswaId(rs.getInt("jenis_beasiswa_id"));
                    user.setFotoProfilContent(rs.getBytes("foto_profil_content"));
                    user.setFotoProfilType(rs.getString("foto_profil_type"));
                    return user;
                }
            }
        }
        return null;
    }
}