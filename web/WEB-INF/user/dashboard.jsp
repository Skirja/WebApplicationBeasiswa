<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Base64" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Dashboard Mahasiswa - Sistem Beasiswa</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <h2 class="text-center mb-4">Dashboard Mahasiswa</h2>
            <div class="row">
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-body text-center">
                            <h3 class="card-title">Profil</h3>
                            <c:choose>
                                <c:when test="${not empty user.fotoProfilContent}">
                                    <img src="data:${user.fotoProfilType};base64,${Base64.getEncoder().encodeToString(user.fotoProfilContent)}" alt="Foto Profil" class="img-fluid rounded-circle mb-3" style="width: 150px; height: 150px;">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/assets/default-profile.png" alt="Default Foto Profil" class="img-fluid rounded-circle mb-3" style="width: 150px; height: 150px;">
                                </c:otherwise>
                            </c:choose>
                            <p><strong>Nama:</strong> ${user.namaLengkap}</p>
                            <p><strong>Email:</strong> ${user.email}</p>
                            <p><strong>Nomor Pendaftaran:</strong> ${user.nomorPendaftaran}</p>
                            <p><strong>Semester:</strong> ${user.semester}</p>
                            <p><strong>Nomor Telepon:</strong> ${user.nomorTelepon}</p>
                            <p><strong>Alamat:</strong> ${user.alamatLengkap}</p>
                            <form action="${pageContext.request.contextPath}/user/updateFotoProfil" method="post" enctype="multipart/form-data">
                                <div class="mb-3">
                                    <label for="newFotoProfil" class="form-label">Ganti Foto Profil:</label>
                                    <input type="file" class="form-control" id="newFotoProfil" name="newFotoProfil" accept="image/*" required>
                                </div>
                                <button type="submit" class="btn btn-primary"><i class="fas fa-upload"></i> Perbarui Foto Profil</button>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-body">
                            <h3 class="card-title">Daftar Berkas</h3>
                            <a href="#" class="btn btn-success mb-3" data-bs-toggle="modal" data-bs-target="#uploadModal"><i class="fas fa-file-upload"></i> Upload Berkas Baru</a>
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>Nama Berkas</th>
                                        <th>Tanggal Upload</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="berkas" items="${berkasList}">
                                    <tr>
                                        <td>${berkas.namaBerkas}</td>
                                        <td>${berkas.tanggalUpload}</td>
                                        <td><span class="badge ${berkas.statusVerifikasi == 'Verified' ? 'bg-success' : 'bg-warning'}">${berkas.statusVerifikasi}</span></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <nav aria-label="Page navigation">
                                <ul class="pagination justify-content-center">
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <li class="page-item ${currentPage eq i ? 'active' : ''}">
                                            <a class="page-link" href="${pageContext.request.contextPath}/user/dashboard?page=${i}">${i}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/auth/logout" class="btn btn-danger mt-3"><i class="fas fa-sign-out-alt"></i> Logout</a>
        </div>

        <!-- Modal Upload Berkas -->
        <div class="modal fade" id="uploadModal" tabindex="-1" aria-labelledby="uploadModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="uploadModalLabel">Upload Berkas Baru</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="${pageContext.request.contextPath}/user/upload" method="post" enctype="multipart/form-data">
                            <div class="mb-3">
                                <label for="berkas" class="form-label">Pilih Berkas:</label>
                                <input type="file" class="form-control" id="berkas" name="berkas" required>
                            </div>
                            <button type="submit" class="btn btn-primary"><i class="fas fa-upload"></i> Upload</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/js/all.min.js"></script>
        <script>
            <c:if test="${not empty error}">
                alert("${error}");
            </c:if>
        </script>
    </body>
</html>