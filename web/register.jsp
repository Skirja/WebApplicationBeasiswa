<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Registrasi - Sistem Beasiswa</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <h2>Registrasi</h2>
            <form action="${pageContext.request.contextPath}/auth/register" method="post" enctype="multipart/form-data">
                <div class="mb-3">
                    <label for="namaLengkap" class="form-label">Nama Lengkap:</label>
                    <input type="text" class="form-control" id="namaLengkap" name="namaLengkap" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email:</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password:</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <div class="mb-3">
                    <label for="semester" class="form-label">Semester:</label>
                    <input type="number" class="form-control" id="semester" name="semester" min="1" max="8" required>
                </div>
                <div class="mb-3">
                    <label for="nomorTelepon" class="form-label">Nomor Telepon:</label>
                    <input type="tel" class="form-control" id="nomorTelepon" name="nomorTelepon" required>
                </div>
                <div class="mb-3">
                    <label for="alamatLengkap" class="form-label">Alamat Lengkap:</label>
                    <textarea class="form-control" id="alamatLengkap" name="alamatLengkap" required></textarea>
                </div>
                <div class="mb-3">
                    <label for="jenisBeasiswaId" class="form-label">Jenis Beasiswa:</label>
                    <select class="form-select" id="jenisBeasiswaId" name="jenisBeasiswaId" required>
                        <c:forEach var="beasiswa" items="${jenisBeasiswaList}">
                            <option value="${beasiswa.jenisBeasiswaId}">${beasiswa.namaBeasiswa}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="fotoProfil" class="form-label">Foto Profil:</label>
                    <input type="file" class="form-control" id="fotoProfil" name="fotoProfil" accept="image/*" required>
                </div>
                <button type="submit" class="btn btn-primary">Daftar</button>
            </form>
            <p class="mt-3">Sudah punya akun? <a href="${pageContext.request.contextPath}/auth/login">Login di sini</a></p>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </body>
</html>