<div align="center">

# TicTacToe : Game Android

![Kotlin](https://img.shields.io/badge/kotlin-%232.3.21-blue?style=flat&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/ui-jetpack%20compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)
![Minimum SDK](https://img.shields.io/badge/minSdk-24-orange)
![Target SDK](https://img.shields.io/badge/targetSdk-36-brightgreen)
![JDK](https://img.shields.io/badge/JDK-17-orange)
![Architecture](https://img.shields.io/badge/architecture-MVVM-6f42c1)
![Hilt](https://img.shields.io/badge/DI-Hilt-99cc00?logo=dagger&logoColor=white)
![Room](https://img.shields.io/badge/data-Room-green)
![Firebase](https://img.shields.io/badge/firebase-realtime%20db%20%26%20analytics-FFCA28?style=flat&logo=firebase&logoColor=black)
</div>

---

**TicTacToe** adalah aplikasi game Android Native yang sederhana dan seru — main Tic-Tac-Toe melawan teman secara offline, dengan riwayat permainan yang tersimpan otomatis.

### 📖 Detail

- **Aplikasi apa** — TicTacToe adalah aplikasi game Android berbasis **Kotlin** dan **Jetpack Compose**.
- **Masalahnya** — Main Tic-Tac-Toe bareng teman biasanya pakai kertas/papan seadanya, jadi hasil pertandingan gampang hilang dan susah dilacak.
- **Hasilnya** — TicTacToe mencatat skor X vs O secara otomatis dan menyimpan riwayat pertandingan di perangkat, jadi main jadi praktis tanpa setelan rumit.

---

## 📸 Tampilan Aplikasi

Berikut adalah tampilan antarmuka aplikasi:

<p align="start">
  **Sementara dipersiapkan**
</p>

---

## ✨ Fitur Utama

- **🌐 Mode Online** – Bermain melawan pemain lain secara real-time (masih dalam pengembangan).
- **👥 Mode Offline** – Mainkan Tic-Tac-Toe dua pemain dalam satu perangkat, dengan skor kemenangan X & O serta penghitung seri per sesi.
- **📜 Riwayat Permainan** – Setiap sesi tersimpan otomatis ke database lokal (Room) dan bisa dilihat di layar History.

---

## 🛠 Tech Stack

| Layer            | Teknologi                                          |
|------------------|----------------------------------------------------|
| Bahasa           | Kotlin                                             |
| UI Framework     | Jetpack Compose (Material 3)                       |
| Arsitektur       | MVVM                                               |
| State Management | StateFlow + `UiState`                              |
| DI               | Hilt                                               |
| Data             | Room (aktif) + Firebase Realtime DB & Analytics¹   |
| Navigasi         | Navigation Compose                                 |
| Lainnya          | KSP, Material Icons Extended                       |

¹ Firebase sudah terpasang di modul DI, tetapi belum dipakai fitur apa pun — mode Online
masih berupa placeholder.

### Versi Toolchain

| Tool          | Versi   |
|---------------|---------|
| Gradle        | 9.5.0   |
| AGP           | 9.3.0   |
| Kotlin        | 2.3.21  |
| KSP           | 2.3.11  |
| compileSdk    | 37      |
| minSdk        | 24      |
| targetSdk     | 36      |
| JDK           | 17      |

---

## 🏗 Arsitektur

TicTacToe mengikuti pola **MVVM** (Model-View-ViewModel) dengan aliran data satu arah. Setiap layar mengambil state dari `ViewModel`-nya melalui `StateFlow` dan menampilkan `UiState` bertipe sealed class (`Loading`, `Success`, `Error`, `Empty`), sehingga UI hanya menjadi fungsi dari state.

```
Layar Compose  →  ViewModel (StateFlow/UiState)  →  GameRepository  →  Room
```

### Struktur Proyek

```
app/src/main/java/id/co/mondo/tictactoe/
├── data/
│   ├── local/
│   │   ├── dao/          # GameHistoryDao
│   │   ├── entity/       # GameHistoryEntity
│   │   ├── model/        # Game, Cell, Winner, dll.
│   │   └── AppDatabase   # Room database
│   └── repository/       # GameRepository
├── di/                   # Modul Hilt (Room & Firebase)
├── ui/
│   ├── component/        # Composable yang dapat digunakan ulang
│   ├── navigation/       # Screen & TicTacToeApp
│   ├── screen/           # home / offline / play / history
│   └── theme/            # Color, Type, Theme
└── util/                 # Constants, Result, UiState
```

---

## 🛠 Menjalankan Project

Prasyarat: **JDK 17** dan Android SDK dengan platform `android-37.0`. Gradle tidak perlu
dipasang manual — selalu pakai wrapper di dalam repo.

```bash
# Cek kompilasi Kotlin (verifikasi paling cepat)
./gradlew :app:compileDebugKotlin

# Build APK debug
./gradlew assembleDebug

# Build APK release (minify + resource shrinking aktif)
./gradlew assembleRelease
```

Di Windows pakai `.\gradlew.bat <task>`, misalnya `.\gradlew.bat assembleDebug`.

`app/google-services.json` sudah ikut di-commit dan **wajib ada** — tanpa file itu plugin
`com.google.gms.google-services` gagal saat konfigurasi.

> **Catatan:** `lintDebug` saat ini gagal pada kode yang sudah ada sebelumnya (1 error
> `NonObservableLocale` di `HistoryScreen.kt` + 23 warning resource). Bukan akibat
> perubahanmu — pakai `compileDebugKotlin` sebagai gerbang.

---

## 🤝 Kontribusi

TicTacToe saat ini merupakan proyek pribadi, tetapi kontribusi sangat kami sambut! Jika kamu ingin membantu:

1. **Fork** repositori ini.
2. Buat **branch** fitur/perbaikan (mis. `feat/fitur-keren` atau `fix/perbaikan-bug`).
3. Lakukan perubahan dan commit.
4. Ajukan **Pull Request** yang menjelaskan apa yang kamu lakukan dan alasannya.

Baik itu memperbaiki bug, menambah fitur, atau menyempurnakan dokumentasi — setiap bantuan sangat berarti!

---

## 📌 Catatan

- Aplikasi ini dibuat untuk pembelajaran dan eksplorasi konsep game development Android.
- UI dibuat sederhana dan fokus pada gameplay.
- Versi aplikasi saat ini **2.1** (`versionCode` 5). Mode online masih dalam pengembangan.
- Riwayat permainan disimpan di Room dengan `fallbackToDestructiveMigration(true)`, jadi
  perubahan skema database pada versi berikutnya akan **menghapus riwayat pemain** yang ada.
- © 2025 TicTacToe Project
