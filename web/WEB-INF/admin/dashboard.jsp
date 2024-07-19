<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Admin - Sistem Beasiswa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.7.32/dist/sweetalert2.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1>Dashboard Admin</h1>
            <a href="${pageContext.request.contextPath}/auth/logout" class="btn btn-danger">Logout</a>
        </div>

        <form action="${pageContext.request.contextPath}/admin/dashboard" method="get" class="mb-4">
            <div class="input-group">
                <input type="text" class="form-control" name="search" placeholder="Cari mahasiswa..." value="${param.search}">
                <button class="btn btn-outline-secondary" type="submit">Cari</button>
            </div>
        </form>

        <table class="table table-striped">
            <thead>
                <tr>
                    <th><a href="?sortBy=namaLengkap&sortOrder=${param.sortOrder == 'asc' ? 'desc' : 'asc'}&search=${param.search}">Nama</a></th>
                    <th><a href="?sortBy=email&sortOrder=${param.sortOrder == 'asc' ? 'desc' : 'asc'}&search=${param.search}">Email</a></th>
                    <th><a href="?sortBy=nomorPendaftaran&sortOrder=${param.sortOrder == 'asc' ? 'desc' : 'asc'}&search=${param.search}">Nomor Pendaftaran</a></th>
                    <th><a href="?sortBy=semester&sortOrder=${param.sortOrder == 'asc' ? 'desc' : 'asc'}&search=${param.search}">Semester</a></th>
                    <th>Aksi</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${userList}">
                    <tr>
                        <td>${user.namaLengkap}</td>
                        <td>${user.email}</td>
                        <td>${user.nomorPendaftaran}</td>
                        <td>${user.semester}</td>
                        <td>
                            <button class="btn btn-sm btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#berkas${user.userId}" aria-expanded="false" aria-controls="berkas${user.userId}">
                                Lihat Berkas
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="5">
                            <div class="collapse" id="berkas${user.userId}">
                                <table class="table table-sm">
                                    <thead>
                                        <tr>
                                            <th>Nama Berkas</th>
                                            <th>Tanggal Upload</th>
                                            <th>Status</th>
                                            <th>Aksi</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="berkas" items="${userBerkasMap[user.userId]}">
                                            <tr>
                                                <td>${berkas.namaBerkas}</td>
                                                <td>${berkas.tanggalUpload}</td>
                                                <td>${berkas.statusVerifikasi}</td>
                                                <td>
                                                    <button class="btn btn-sm btn-success" onclick="verifikasiBerkas(${berkas.berkasId}, 'Terverifikasi')">Verifikasi</button>
                                                    <button class="btn btn-sm btn-danger" onclick="verifikasiBerkas(${berkas.berkasId}, 'Ditolak')">Tolak</button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${currentPage eq i ? 'active' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/admin/dashboard?page=${i}&search=${param.search}&sortBy=${param.sortBy}&sortOrder=${param.sortOrder}">${i}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.7.32/dist/sweetalert2.all.min.js"></script>
    <script>
        function verifikasiBerkas(berkasId, status) {
            const formData = new URLSearchParams();
            formData.append('berkasId', berkasId);
            formData.append('status', status);

            fetch('${pageContext.request.contextPath}/admin/verifikasi', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: formData
            })
            .then(response => {
                return response.text().then(text => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}, message: ${text}`);
                    }
                    return JSON.parse(text);
                });
            })
            .then(data => {
                if (data.success) {
                    Swal.fire('Sukses', 'Status berkas berhasil diperbarui', 'success').then(() => {
                        location.reload();
                    });
                } else {
                    Swal.fire('Error', data.message || 'Gagal memperbarui status berkas', 'error');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire('Error', 'Terjadi kesalahan saat memproses permintaan: ' + error.message, 'error');
            });
        }
    </script>
</body>
</html>