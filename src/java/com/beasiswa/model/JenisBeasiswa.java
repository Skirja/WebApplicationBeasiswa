package com.beasiswa.model;

public class JenisBeasiswa {

    private int jenisBeasiswaId;
    private String namaBeasiswa;

    // Constructor
    public JenisBeasiswa() {
    }

    public JenisBeasiswa(int jenisBeasiswaId, String namaBeasiswa) {
        this.jenisBeasiswaId = jenisBeasiswaId;
        this.namaBeasiswa = namaBeasiswa;
    }

    // Getters and Setters
    public int getJenisBeasiswaId() {
        return jenisBeasiswaId;
    }

    public void setJenisBeasiswaId(int jenisBeasiswaId) {
        this.jenisBeasiswaId = jenisBeasiswaId;
    }

    public String getNamaBeasiswa() {
        return namaBeasiswa;
    }

    public void setNamaBeasiswa(String namaBeasiswa) {
        this.namaBeasiswa = namaBeasiswa;
    }
}
