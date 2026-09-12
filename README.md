# OpenXRay Android Launcher

S.T.A.L.K.E.R. motoru için Android launcher uygulaması.

## Bağımlılıklar

- [xray-16-android](https://github.com/your-username/xray-16-android) — Motor (`libxr_3da.so`)
- [SDL2](https://github.com/libsdl-org/SDL) — Giriş ve pencere yönetimi (`libSDL2.so`)

## CI

GitHub Actions ile otomatik derleme:

1. SDL2'yi Android için derler
2. xray-16-android motorunu derler
3. APK oluşturur

**Workflow:** `.github/workflows/build.yml`

**Artifact indirme:**
- Actions sekmesi → ilgili workflow → "Artifacts" bölümü

## Yerel Derleme

### Gereksinimler
- Android SDK 34
- JDK 17
- NDK 26.1.10909125

### Adımlar

1. **SDL2'yi derle:**
   ```bash
   git clone --depth 1 --branch release-2.28.x https://github.com/libsdl-org/SDL.git
   cd SDL && mkdir build && cd build
   cmake .. \
     -DCMAKE_TOOLCHAIN_FILE=$ANDROID_NDK_HOME/build/cmake/android.toolchain.cmake \
     -DANDROID_ABI=arm64-v8a \
     -DANDROID_PLATFORM=android-29 \
     -DANDROID_STL=c++_shared \
     -DCMAKE_BUILD_TYPE=Release \
     -DSDL_SHARED=ON
   make -j$(nproc)
   cp libSDL2.so /path/to/launcher/app/src/main/jniLibs/arm64-v8a/
   ```

2. **Motoru derle:**
   ```bash
   cd xray-16-android
   cmake -B build \
     -DCMAKE_TOOLCHAIN_FILE=$ANDROID_NDK_HOME/build/cmake/android.toolchain.cmake \
     -DANDROID_ABI=arm64-v8a \
     -DANDROID_PLATFORM=android-29 \
     -DCMAKE_BUILD_TYPE=Release \
     -DBUILD_SHARED_LIBS=OFF \
     -DXRAY_USE_GLES=ON
   cmake --build build -j$(nproc)
   cp build/libxr_3da.so /path/to/launcher/app/src/main/jniLibs/arm64-v8a/
   ```

3. **Launcher'ı derle:**
   ```bash
   cd openxray-android-launcher
   ./gradlew assembleRelease
   ```

4. **APK'yı yükle:**
   ```bash
   adb install app/build/outputs/apk/release/app-release-unsigned.apk
   ```

## Kullanım

1. Uygulamayı açın
2. "Dizin Seç" butonuna basın
3. Oyun dosyalarının bulunduğu klasörü seçin (örn: `/sdcard/Game/STALKER/`)
4. Oyun modunu seçin (Call of Chernobyl / Clear Sky / Shadow of Chernobyl)
5. "OYNA" butonuna basın

## Yapı

```
openxray-android-launcher/
├── .github/workflows/build.yml
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/openxray/launcher/
│       │   ├── LauncherActivity.kt
│       │   ├── GameActivity.kt
│       │   └── PrefsManager.kt
│       ├── res/
│       └── jniLibs/arm64-v8a/
│           ├── libSDL2.so
│           └── libxr_3da.so
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```
