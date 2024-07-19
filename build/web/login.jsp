<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login - Sistem Beasiswa</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .login-container {
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
            }
            .login-form {
                padding: 2rem 5rem;
                border-radius: 10px;
            }
            .login-image {
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                overflow: hidden;
            }
            .login-image img {
                width: 100%;
                height: 100%;
                object-fit: cover;
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <div class="row w-100 align-items-center">
                <div class="col-md-6 login-form">
                    <h2>Login</h2>
                    <form action="${pageContext.request.contextPath}/auth/login" method="post">
                        <div class="mb-3">
                            <label for="email" class="form-label">Email:</label>
                            <input type="email" class="form-control" id="email" name="email" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Password:</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Login</button>
                    </form>
                    <p class="mt-3">Belum punya akun? <a href="${pageContext.request.contextPath}/auth/register">Daftar di sini</a></p>
                </div>
                <div class="col-md-6 d-none d-md-block p-0 login-image">
                    <img src="${pageContext.request.contextPath}/assets/loginimg.jpeg" alt="Login Image">
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <% if (request.getAttribute("error") != null) { %>
        <script>
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: '${error}'
            });
        </script>
        <% }%>
    </body>
</html>