<div align="center">

# TicTacToe : Game Android

![Kotlin](https://img.shields.io/badge/kotlin-%232.3.21-blue?style=flat&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/ui-jetpack%20compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)
![Minimum SDK](https://img.shields.io/badge/minSdk-24-orange)
![Target SDK](https://img.shields.io/badge/targetSdk-36-brightgreen)
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

| Layer            | Teknologi                       |
|------------------|---------------------------------|
| Bahasa           | Kotlin                          |
| UI Framework     | Jetpack Compose (Material 3)    |
| Arsitektur       | MVVM                            |
| State Management | StateFlow + `UiState`           |
| DI               | Hilt                            |
| Data             | Room + Firebase Realtime DB & Analytics |
| Navigasi         | Navigation Compose              |
| Lainnya          | KSP, Material Icons Extended    |

---

## 🏗 Arsitektur

TicTacToe mengikuti pola **MVVM** (Model-View-ViewModel) dengan aliran data satu arah. Setiap layar mengambil state dari `ViewModel`-nya melalui `StateFlow` dan menampilkan `UiState` bertipe sealed class (`Loading`, `Success`, `Error`, `Empty`), sehingga UI hanya menjadi fungsi dari state.

```
Layar Compose  →  ViewModel (StateFlow/UiState)  →  Repository  →  Room / Firebase
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
- Mode online masih dalam pengembangan (v2.1.0).
- © 2025 TicTacToe Project
