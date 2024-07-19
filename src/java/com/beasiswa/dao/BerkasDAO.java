package com.beasiswa.dao;

import com.beasiswa.model.Berkas;
import com.beasiswa.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BerkasDAO {

    public void addBerkas(Berkas berkas) throws SQLException {
        String sql = "INSERT INTO berkas (user_id, nama_berkas, path_berkas, tanggal_upload, status_verifikasi, file_type, file_content) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, berkas.getUserId());
            stmt.setString(2, berkas.getNamaBerkas());
            stmt.setString(3, berkas.getPathBerkas());
            stmt.setTimestamp(4, new Timestamp(berkas.getTanggalUpload().getTime()));
            stmt.setString(5, berkas.getStatusVerifikasi());
            stmt.setString(6, berkas.getFileType());
            stmt.setBytes(7, berkas.getFileContent());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating berkas failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    berkas.setBerkasId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating berkas failed, no ID obtained.");
                }
            }
        }
    }

    public List<Berkas> getBerkasByUserId(int userId, int offset, int recordsPerPage) throws SQLException {
        List<Berkas> berkasList = new ArrayList<>();
        String sql = "SELECT * FROM berkas WHERE user_id = ? LIMIT ? OFFSET ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recordsPerPage);
            stmt.setInt(3, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Berkas berkas = new Berkas();
                    berkas.setBerkasId(rs.getInt("berkas_id"));
                    berkas.setUserId(rs.getInt("user_id"));
                    berkas.setNamaBerkas(rs.getString("nama_berkas"));
                    berkas.setPathBerkas(rs.getString("path_berkas"));
                    berkas.setTanggalUpload(rs.getTimestamp("tanggal_upload"));
                    berkas.setStatusVerifikasi(rs.getString("status_verifikasi"));
                    berkas.setFileType(rs.getString("file_type"));
                    // Tidak perlu mengambil file_content di sini untuk efisiensi
                    berkasList.add(berkas);
                }
            }
        }
        return berkasList;
    }

    public List<Berkas> getAllBerkasByUserId(int userId) throws SQLException {
        List<Berkas> berkasList = new ArrayList<>();
        String sql = "SELECT * FROM berkas WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Berkas berkas = new Berkas();
                    berkas.setBerkasId(rs.getInt("berkas_id"));
                    berkas.setUserId(rs.getInt("user_id"));
                    berkas.setNamaBerkas(rs.getString("nama_berkas"));
                    berkas.setPathBerkas(rs.getString("path_berkas"));
                    berkas.setTanggalUpload(rs.getTimestamp("tanggal_upload"));
                    berkas.setStatusVerifikasi(rs.getString("status_verifikasi"));
                    berkas.setFileType(rs.getString("file_type"));
                    berkasList.add(berkas);
                }
            }
        }
        return berkasList;
    }

    public Berkas getBerkasById(int berkasId) throws SQLException {
        String sql = "SELECT * FROM berkas WHERE berkas_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, berkasId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractBerkasFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public int getTotalBerkasByUserId(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM berkas WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public void updateBerkasStatus(int berkasId, String status) throws SQLException {
        String sql = "UPDATE berkas SET status_verifikasi = ? WHERE berkas_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, berkasId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating berkas failed, no rows affected.");
            }
        }
    }

    private Berkas extractBerkasFromResultSet(ResultSet rs) throws SQLException {
        Berkas berkas = new Berkas();
        berkas.setBerkasId(rs.getInt("berkas_id"));
        berkas.setUserId(rs.getInt("user_id"));
        berkas.setNamaBerkas(rs.getString("nama_berkas"));
        berkas.setTanggalUpload(rs.getTimestamp("tanggal_upload"));
        berkas.setStatusVerifikasi(rs.getString("status_verifikasi"));
        berkas.setFileType(rs.getString("file_type"));
        berkas.setPathBerkas(rs.getString("path_berkas"));
        // Tidak mengambil file_content di sini untuk menghemat memori
        return berkas;
    }

    public List<Map<String, Object>> getDetailBerkasByUserId(int userId) throws SQLException {
        List<Map<String, Object>> detailBerkasList = new ArrayList<>();
        String sql = "SELECT * FROM berkas WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> berkas = new HashMap<>();
                    berkas.put("berkasId", rs.getInt("berkas_id"));
                    berkas.put("namaBerkas", rs.getString("nama_berkas"));
                    berkas.put("tanggalUpload", rs.getTimestamp("tanggal_upload").getTime());
                    berkas.put("statusVerifikasi", rs.getString("status_verifikasi"));
                    detailBerkasList.add(berkas);
                }
            }
        }
        return detailBerkasList;
    }
}