package com.beasiswa.model;

public class User {

    private int userId;
    private String namaLengkap;
    private String email;
    private String password;
    private String role;
    private String nomorPendaftaran;
    private int semester;
    private String nomorTelepon;
    private String alamatLengkap;
    private int jenisBeasiswaId;
    private String fotoProfil;
    private byte[] fotoProfilContent;
    private String fotoProfilType;

    // Constructor
    public User() {
    }

    public User(int userId, String namaLengkap, String email, String password, String role,
            String nomorPendaftaran, int semester, String nomorTelepon, String alamatLengkap,
            int jenisBeasiswaId, String fotoProfil) {
        this.userId = userId;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.password = password;
        this.role = role;
        this.nomorPendaftaran = nomorPendaftaran;
        this.semester = semester;
        this.nomorTelepon = nomorTelepon;
        this.alamatLengkap = alamatLengkap;
        this.jenisBeasiswaId = jenisBeasiswaId;
        this.fotoProfil = fotoProfil;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNomorPendaftaran() {
        return nomorPendaftaran;
    }

    public void setNomorPendaftaran(String nomorPendaftaran) {
        this.nomorPendaftaran = nomorPendaftaran;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getAlamatLengkap() {
        return alamatLengkap;
    }

    public void setAlamatLengkap(String alamatLengkap) {
        this.alamatLengkap = alamatLengkap;
    }

    public int getJenisBeasiswaId() {
        return jenisBeasiswaId;
    }

    public void setJenisBeasiswaId(int jenisBeasiswaId) {
        this.jenisBeasiswaId = jenisBeasiswaId;
    }

    public String getFotoProfil() {
        return fotoProfil;
    }

    public void setFotoProfil(String fotoProfil) {
        this.fotoProfil = fotoProfil;
    }

    public byte[] getFotoProfilContent() {
        return fotoProfilContent;
    }

    public void setFotoProfilContent(byte[] fotoProfilContent) {
        this.fotoProfilContent = fotoProfilContent;
    }

    public String getFotoProfilType() {
        return fotoProfilType;
    }

    public void setFotoProfilType(String fotoProfilType) {
        this.fotoProfilType = fotoProfilType;
    }
}