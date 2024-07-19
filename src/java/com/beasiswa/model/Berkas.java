package com.beasiswa.model;

import java.util.Date;

public class Berkas {

    private int berkasId;
    private int userId;
    private String namaBerkas;
    private String pathBerkas;
    private Date tanggalUpload;
    private String statusVerifikasi;
    private String fileType;
    private byte[] fileContent;

    // Constructor
    public Berkas() {
    }

    // Getters and Setters
    public int getBerkasId() {
        return berkasId;
    }

    public void setBerkasId(int berkasId) {
        this.berkasId = berkasId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNamaBerkas() {
        return namaBerkas;
    }

    public void setNamaBerkas(String namaBerkas) {
        this.namaBerkas = namaBerkas;
    }

    public String getPathBerkas() {
        return pathBerkas;
    }

    public void setPathBerkas(String pathBerkas) {
        this.pathBerkas = pathBerkas;
    }

    public Date getTanggalUpload() {
        return tanggalUpload;
    }

    public void setTanggalUpload(Date tanggalUpload) {
        this.tanggalUpload = tanggalUpload;
    }

    public String getStatusVerifikasi() {
        return statusVerifikasi;
    }

    public void setStatusVerifikasi(String statusVerifikasi) {
        this.statusVerifikasi = statusVerifikasi;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}