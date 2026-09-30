# AGENTS.md

Aplikasi TicTacToe Android. Satu modul Gradle (`:app`), Kotlin + Jetpack Compose, MVVM,
DI via Hilt, Room untuk riwayat lokal, Firebase disiapkan untuk mode online yang belum selesai.

## Perintah

Jalankan dari root repo lewat wrapper (jangan andalkan Gradle global):

- Cek Kotlin cepat: `.\gradlew.bat :app:compileDebugKotlin`
- Unit test: `.\gradlew.bat testDebugUnitTest` (satu kelas: `--tests "<fully.qualified.ClassName>"`)
- Lint: `.\gradlew.bat :app:lintDebug`
- Instrumented test: `.\gradlew.bat connectedDebugAndroidTest` (butuh device/emulator aktif)

Tidak ada CI (folder `.github/` tidak ada) dan tidak ada ktlint/detekt/`.editorconfig` —
Android Lint satu-satunya gerbang statis.

### `lintDebug` gagal di tree yang bersih — ini bukan salahmu

`:app:lintDebug` saat ini **BUILD FAILED** dengan 1 error + 23 warning, dan tidak ada
`lint-baseline.xml` di repo:

- **Error** `NonObservableLocale` di `ui/screen/history/HistoryScreen.kt:264` —
  `Locale.getDefault()` dipanggil di dalam `@Composable fun formatPlayedAt(...)`.
  Perbaikannya `LocalLocale.current.platformLocale`. Check ini muncul karena upgrade
  Compose BOM, jadi error lama yang baru ketahuan.
- **Warning** `UnusedResources` + `IconLocation` — semua bawaan template (`colors.xml`
  purple/teal/black/white, `drawable/ic_circle.png`, `ic_cross.png`, `ttt.png`, `ic_launcher_round`).

Karena itu `lintDebug` tidak bisa dipakai sebagai gerbang "apakah perubahan saya aman".
Gunakan `compileDebugKotlin` sebagai gate, atau tambahkan baseline dulu
(`android { lint { baseline = file("lint-baseline.xml") } }` → `.\gradlew.bat updateLintBaseline`).

### Verifikasi bukan lewat test

Test suite hanya `ExampleUnitTest` + `ExampleInstrumentedTest` hasil generate — nol coverage.
Strategi verifikasi repo ini adalah **Compose preview** (sudah ada 24, mencakup
phone/tablet/dark/loading/error) plus emulator lewat skill `android-cli`.
Konsekuensinya: setiap perubahan layar sebaiknya menambah `@Preview`.

## Toolchain

- JDK 17 (`kotlin { jvmToolchain(17) }` + `sourceCompatibility/targetCompatibility 17`).
  Gradle wrapper 9.5.0, AGP 9.3.0, Kotlin 2.3.21, KSP 2.3.11 — semua di `gradle/libs.versions.toml`.
- `compileSdk = 37` (folder SDK lokal bernama `platforms/android-37.0`), `targetSdk = 36`, `minSdk = 24`.
- `libs.versions.toml` **tidak** punya alias plugin `kotlin-android`; kompilasi Kotlin
  berasal dari dukungan bawaan AGP. Yang di-apply hanya `kotlin.compose` dan `kotlin.serialization`.
- Plugin `org.jetbrains.kotlin.plugin.serialization` sudah di-apply, tapi
  `kotlinx-serialization-json` **belum** jadi dependency — tambahkan dulu sebelum pakai `@Serializable`.
- `kotlin.code.style=official`, `android.nonTransitiveRClass=true`, `android.r8.optimizedResourceShrinking=true`.

## Gotcha setup

- `app/google-services.json` wajib ada dan ikut di-commit; plugin
  `com.google.gms.google-services` gagal konfigurasi tanpanya. Isinya bukan secret.
- Build `release` tidak punya `signingConfig` (fallback ke debug signing) dan mengaktifkan
  minify + resource shrinking. `app/proguard-rules.pro` masih 100% komentar template —
  tambahkan keep rule di sana sebelum rilis class yang memakai reflection / `@Serializable` /
  mapping Firebase, lalu verifikasi dengan `assembleRelease`.
- `local.properties` (path SDK) bersifat machine-specific dan di-git-ignore.
- Firebase sudah di-wire (`AppModule.provideGamesRef` → `Constants.PATH_GAMES`) tapi
  **tidak ada** yang memakainya; `HomeScreen` hanya menampilkan Toast untuk mode Online.
  Jangan menganggap jalur kode Firebase teruji.

## Arsitektur

Root package `id.co.mondo.tictactoe`, semua di `app/src/main/java/id/co/mondo/tictactoe/`:

- `data/local/{dao,entity,model}` — Room. `AppDatabase` = `version = 1`, `exportSchema = false`;
  builder di `di/AppModule.kt:28` memakai `fallbackToDestructiveMigration(true)` dengan nama db
  `tictactoe_database`. Artinya perubahan entity **menghapus riwayat pemain**, dan tidak ada
  folder schema yang perlu di-update.
- `data/repository/GameRepository.kt` — satu-satunya pintu data; hanya DAO (tanpa Firebase),
  mengembalikan `util.Result` (`Success`/`Error`/`Loading`), tidak melempar exception.
- `ui/navigation/Screen.kt` — sealed class rute; `TicTacToeApp.kt` adalah `NavHost` (start = Home).
- `ui/screen/{home,offline,play,history}` — `offline`, `play`, `history` masing-masing punya
  `*Screen.kt` + `*ViewModel.kt`; **`home` tidak punya ViewModel** (menu statis).
- `util/` — `UiState` (sealed interface: `Loading`/`Empty`/`Success`/`Error`), `Result`, `Constants`.
- Wiring aplikasi: `App.kt` = `@HiltAndroidApp`, `MainActivity` = `@AndroidEntryPoint` +
  `enableEdgeToEdge()`. ViewModel `@HiltViewModel` + `hiltViewModel()`, layar mengumpulkan
  state dengan `collectAsStateWithLifecycle()`.

## Konvensi

- Pisahkan `*Content` (stateless: param biasa + lambda, tanpa ViewModel/nav) dan `*Screen`
  (stateful). Ikuti pola ini — itulah yang membuat preview jadi mungkin.
- Render state sepenuhnya dari `UiState<T>`; pakai ulang
  `ui/component/{LoadingContent, ErrorContent, ErrorInlineContent}.kt` alih-alih membuat UI
  loading/error sendiri. `ErrorContent` = layar penuh, `ErrorInlineContent` = inline di bawah form.
- `NavController` selalu dikirim sebagai param non-null ke `*Screen` dan tidak pernah disimpan
  di ViewModel. Sisi navigasi lewat `LaunchedEffect(state)`.
- Aturan back stack: masuk Play → `popUpTo(Screen.Home.route)`; keluar Play →
  `popUpTo(0) { inclusive = true }`.
- Rute `Play` mewajibkan argumen `roomId` (`play/{roomId}`, dibuat lewat
  `Screen.Play.createRoute(...)`); `PlayViewModel` membacanya dari `SavedStateHandle`.
  Navigasi ke sana terjadi di `LaunchedEffect` `OfflineSetupScreen` saat `UiState.Success`.
- Aturan main (`makeMove`, `checkWinner` di `PlayViewModel.kt`) tetap di ViewModel dan hanya
  menyentuh enum di `data/local/model/Game.kt` — jangan panggil tipe Compose/Android di sana
  agar tetap bisa dipisah ke unit test.
- Edge-to-edge wajib: setiap `Scaffold` memakai `contentWindowInsets = WindowInsets.safeDrawing`
  dan kontennya memakai `.padding(innerPadding).consumeWindowInsets(innerPadding)`.
  `MainActivity` memanggil `enableEdgeToEdge()`; manifest menyetel
  `android:windowSoftInputMode="adjustResize"` di activity (penting untuk dua kolom nama).
- Layout adaptif saat ini manual: `PlayScreen` bercabang di `BoxWithConstraints` +
  `maxWidth >= 600.dp`. **Tidak ada** dependency `material3-adaptive` walaupun tabel tech
  stack di README menyebut "Material 3 Adaptive".
- Copy UI berbahasa Indonesia dan di-hardcode di Kotlin (`strings.xml` hanya berisi `app_name`).
- README berbahasa Indonesia dan mendokumentasikan fitur + struktur proyek — perbarui saat
  layar/fitur berubah.

## Kebersihan repo

- `.idea/` ikut ter-track sebagian. Karena `core.autocrlf=true` dan tidak ada `.gitattributes`,
  `git status` sering menampilkan `.idea/*.xml` sebagai modified padahal `git diff` kosong —
  abaikan, jangan di-commit.
- `app/release/app-release.aab` dan `app/release/release/app-release.aab` adalah artefak build
  yang ikut ter-track; `app/.gitignore` hanya mengabaikan `/build`. Jangan menambah yang baru.
- `.kotlin/` tidak masuk `.gitignore`, jadi file `.salive` dari Kotlin daemon muncul sebagai untracked.
- Gaya pesan commit bebas dan campuran: `type(scope) : deskripsi singkat`, sesuai riwayat git.

## Skills

Skill lokal repo ada di `.agents/skills/` — baca dulu sebelum mengerjakan hal yang cocok,
jangan karang sendiri: `edge-to-edge`, `adaptive`, `navigation-3`, `r8-analyzer`,
`testing-setup`, `android-cli`.