## Dokumentasi Kode Sistem Beasiswa

### Deskripsi Umum

Kode ini merupakan bagian dari Sistem Beasiswa, sebuah aplikasi web yang dirancang untuk membantu proses pendaftaran dan pengelolaan beasiswa.  Sistem ini terdiri dari beberapa komponen utama:

* **AuthServlet:** Mengelola autentikasi pengguna (login dan registrasi).
* **UserServlet:** Mengelola dashboard dan fitur pengguna, termasuk upload berkas.
* **AdminServlet:** Mengelola dashboard dan fitur admin, termasuk verifikasi berkas.
* **BerkasServlet:** Mengelola berkas, termasuk upload, download, dan manajemen status.
* **Model:** Berisi kelas model seperti `User`, `Berkas`, dan `JenisBeasiswa`.
* **DAO:** Berisi kelas DAO (Data Access Object) untuk berinteraksi dengan database.
* **Util:** Berisi kelas utilitas seperti `DatabaseUtil` dan `FileUploadUtil`.

### Deskripsi Setiap File

**AuthServlet.java**

* **Tujuan:** Mengelola autentikasi pengguna, termasuk login dan registrasi.
* **Fungsi:**
    * `doGet()`: Menangani permintaan HTTP GET, seperti menampilkan form login dan registrasi.
    * `doPost()`: Menangani permintaan HTTP POST, seperti memproses data login dan registrasi.
    * `login()`: Memeriksa kredensial login dan mengarahkan pengguna ke dashboard yang sesuai.
    * `register()`: Menyimpan data pengguna baru ke database, termasuk validasi data dan upload foto profil.
    * `generateNomorPendaftaran()`: Menghasilkan nomor pendaftaran unik untuk pengguna baru.
    * `showRegistrationForm()`: Menampilkan form registrasi, termasuk daftar jenis beasiswa.

**Berkas.java**

* **Tujuan:** Merupakan kelas model yang merepresentasikan berkas yang diunggah pengguna.
* **Fungsi:**
    * Menyediakan getter dan setter untuk semua atribut berkas, seperti `berkasId`, `userId`, `namaBerkas`, `pathBerkas`, `tanggalUpload`, `statusVerifikasi`, `fileType`, dan `fileContent`.

**BerkasDAO.java**

* **Tujuan:** Menyediakan metode untuk berinteraksi dengan tabel `berkas` dalam database.
* **Fungsi:**
    * `addBerkas()`: Menyimpan berkas baru ke database.
    * `getBerkasByUserId()`: Mengambil daftar berkas berdasarkan ID pengguna.
    * `getAllBerkasByUserId()`: Mengambil semua berkas berdasarkan ID pengguna.
    * `getBerkasById()`: Mengambil berkas berdasarkan ID berkas.
    * `getTotalBerkasByUserId()`: Mengambil jumlah total berkas berdasarkan ID pengguna.
    * `updateBerkasStatus()`: Memperbarui status verifikasi berkas.
    * `getDetailBerkasByUserId()`: Mengambil detail berkas berdasarkan ID pengguna.

**BerkasServlet.java**

* **Tujuan:** Mengelola berkas, termasuk upload, download, dan manajemen status.
* **Fungsi:**
    * `doGet()`: Menangani permintaan HTTP GET, seperti menampilkan daftar berkas, mengunduh berkas, dan mengambil data berkas mahasiswa.
    * `doPost()`: Menangani permintaan HTTP POST, seperti mengupload berkas.
    * `listBerkas()`: Menampilkan daftar berkas pengguna.
    * `downloadBerkas()`: Mengunduh berkas.
    * `uploadBerkas()`: Menyimpan berkas ke database.
    * `getBerkasMahasiswa()`: Mengambil data berkas mahasiswa.

**DatabaseUtil.java**

* **Tujuan:** Menyediakan metode untuk terhubung ke database.
* **Fungsi:**
    * `getConnection()`: Mengembalikan koneksi ke database.
    * `closeConnection()`: Menutup koneksi ke database.

**FileUploadUtil.java**

* **Tujuan:** Menyediakan metode utilitas untuk mengelola file upload.
* **Fungsi:**
    * `saveFile()`: Menyimpan file yang diupload ke direktori yang ditentukan.
    * `downloadFile()`: Mengunduh file dari direktori yang ditentukan.

**JenisBeasiswa.java**

* **Tujuan:** Merupakan kelas model yang merepresentasikan jenis beasiswa.
* **Fungsi:**
    * Menyediakan getter dan setter untuk atribut `jenisBeasiswaId` dan `namaBeasiswa`.

**JenisBeasiswaDAO.java**

* **Tujuan:** Menyediakan metode untuk berinteraksi dengan tabel `jenis_beasiswa` dalam database.
* **Fungsi:**
    * `getAllJenisBeasiswa()`: Mengambil semua jenis beasiswa dari database.

**MySQLContextListener.java**

* **Tujuan:**  Mengatur konteks servlet untuk koneksi database MySQL.
* **Fungsi:**
    * `contextInitialized()`: Dijalankan saat konteks servlet diinisialisasi.
    * `contextDestroyed()`: Dijalankan saat konteks servlet dihancurkan. Membersihkan koneksi database dan deregister driver JDBC.

**User.java**

* **Tujuan:** Merupakan kelas model yang merepresentasikan pengguna.
* **Fungsi:**
    * Menyediakan getter dan setter untuk semua atribut pengguna, seperti `userId`, `namaLengkap`, `email`, `password`, `role`, `nomorPendaftaran`, `semester`, `nomorTelepon`, `alamatLengkap`, `jenisBeasiswaId`, `fotoProfil`, `fotoProfilContent`, dan `fotoProfilType`.

**UserDAO.java**

* **Tujuan:** Menyediakan metode untuk berinteraksi dengan tabel `users` dalam database.
* **Fungsi:**
    * `getUserByEmail()`: Mengambil pengguna berdasarkan email.
    * `registerUser()`: Menyimpan pengguna baru ke database.
    * `getAllUsers()`: Mengambil semua pengguna.
    * `getTotalUsers()`: Mengambil jumlah total pengguna.
    * `extractUserFromResultSet()`: Mengambil data pengguna dari objek `ResultSet`.
    * `updateUser()`: Memperbarui data pengguna.
    * `updateUserFotoProfil()`: Memperbarui foto profil pengguna.
    * `getUserById()`: Mengambil pengguna berdasarkan ID pengguna.

**UserServlet.java**

* **Tujuan:** Mengelola dashboard dan fitur pengguna, termasuk upload berkas.
* **Fungsi:**
    * `doGet()`: Menangani permintaan HTTP GET, seperti menampilkan dashboard.
    * `doPost()`: Menangani permintaan HTTP POST, seperti memproses data upload berkas dan foto profil.
    * `showDashboard()`: Menampilkan dashboard pengguna, termasuk daftar berkas.
    * `uploadBerkas()`: Menyimpan berkas pengguna ke database.
    * `updateFotoProfil()`: Memperbarui foto profil pengguna.

**AdminServlet.java**

* **Tujuan:** Mengelola dashboard dan fitur admin, termasuk verifikasi berkas.
* **Fungsi:**
    * `doGet()`: Menangani permintaan HTTP GET, seperti menampilkan dashboard admin.
    * `doPost()`: Menangani permintaan HTTP POST, seperti memproses data verifikasi berkas.
    * `showDashboard()`: Menampilkan dashboard admin, termasuk daftar pengguna dan berkas.
    * `verifikasiBerkas()`: Memperbarui status verifikasi berkas.
    * `sendJsonResponse()`: Mengirimkan respons JSON ke klien.
