package com.beasiswa.dao;

import com.beasiswa.model.JenisBeasiswa;
import com.beasiswa.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JenisBeasiswaDAO {

    public List<JenisBeasiswa> getAllJenisBeasiswa() throws SQLException {
        List<JenisBeasiswa> jenisBeasiswaList = new ArrayList<>();
        String sql = "SELECT * FROM jenis_beasiswa";
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                jenisBeasiswaList.add(extractJenisBeasiswaFromResultSet(rs));
            }
        }
        return jenisBeasiswaList;
    }

    private JenisBeasiswa extractJenisBeasiswaFromResultSet(ResultSet rs) throws SQLException {
        JenisBeasiswa jenisBeasiswa = new JenisBeasiswa();
        jenisBeasiswa.setJenisBeasiswaId(rs.getInt("jenis_beasiswa_id"));
        jenisBeasiswa.setNamaBeasiswa(rs.getString("nama_beasiswa"));
        return jenisBeasiswa;
    }
}
