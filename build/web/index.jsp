<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="id" data-bs-theme="dark">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sukses Beasiswa - Selamat Datang</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                display: flex;
                flex-direction: column;
                min-height: 100vh;
                background: url('./assets/indexbg.jpg') no-repeat center center fixed;
                background-size: cover;
                color: #fff;
                position: relative;
            }
            body::before {
                content: "";
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                z-index: 1;
            }
            .content {
                z-index: 2;
                flex: 1;
                display: flex;
                justify-content: center;
                align-items: center;
                flex-direction: column;
                text-align: center;
            }
            .navbar, .footer {
                background-color: rgba(0, 0, 0, 0.7);
                z-index: 2;
            }
            .btn-primary, .btn-outline-primary {
                transition: transform 0.3s;
            }
            .btn-primary:hover, .btn-outline-primary:hover {
                transform: scale(1.1);
            }
            .lead {
                font-size: 1.5rem;
            }
        </style>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg">
            <div class="container">
                <a class="navbar-brand" href="#">Sukses Beasiswa</a>
            </div>
        </nav>

        <div class="container content">
            <div class="row">
                <div class="col-md-8 offset-md-2">
                    <h1 class="mb-4">Selamat Datang di Sukses Beasiswa</h1>
                    <p class="lead">Sistem ini membantu Anda mendaftar dan mengelola beasiswa dengan mudah.</p>
                    <div class="mt-5">
                        <a href="${pageContext.request.contextPath}/auth/login" class="btn btn-primary btn-lg me-3">Masuk</a>
                        <a href="${pageContext.request.contextPath}/auth/register" class="btn btn-outline-primary btn-lg">Daftar</a>
                    </div>
                </div>
            </div>
        </div>

        <footer class="footer mt-5 py-3">
            <div class="container text-center">
                <p>&copy; 2024 Sistem Beasiswa. Hak Cipta Dilindungi.</p>
                <p class="mb-0">Di buat oleh kelompok 4</p>
            </div>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>