# demo-shop-app

Offline Android demo shop (Kotlin + Jetpack Compose) used to try QA agents such as Droid CUA.

The app is fully offline: six hard-coded products, an in-memory cart, a pre-filled demo checkout, and an order confirmation. It has no network access (no `INTERNET` permission), no persistence, and no real payments. Relaunching the app starts with an empty cart.

- Application ID: `com.loadmill.demoshop`
- App label: Demo Shop
- Version: 1.0.0 (versionCode 1)
- minSdk 26, targetSdk 36

## Demo journey

1. **Catalog**: tap **Add to cart** on **Backpack**. The top bar button shows **Cart (1)**.
2. Tap **Cart (1)** to open **Your cart**. Adjust quantity with **-** / **+**, or tap **Remove**. Tap **Checkout** (disabled when the cart is empty).
3. **Checkout**: the form is pre-filled with demo details (Jane Doe, demo card `4242 4242 4242 4242`, expiry `12/30`, CVC `123`). Tap **Place order**. Empty fields are flagged as required.
4. **Order confirmed**: shows an order number (`DS-123456` style), the items, and the total. Tap **Start over** to clear the cart and return to the catalog.

## Build

Requires JDK 17+ (Android Studio's bundled JBR works) and an Android SDK with platform 36. Point Gradle at the SDK with `local.properties` (`sdk.dir=/path/to/Android/sdk`) or `ANDROID_HOME`.

```sh
./gradlew assembleDebug
./gradlew assembleRelease
./gradlew testDebugUnitTest
```

APK output:

- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release, signed: `app/build/outputs/apk/release/app-release.apk`
- Release, unsigned (no signing properties set): `app/build/outputs/apk/release/app-release-unsigned.apk`

## Release signing

The `release` build is signed only when all four Gradle properties below are set, for example in `~/.gradle/gradle.properties`:

```properties
DEMO_SHOP_KEYSTORE_FILE=/absolute/path/to/demo-shop.jks
DEMO_SHOP_KEYSTORE_PASSWORD=...
DEMO_SHOP_KEY_ALIAS=...
DEMO_SHOP_KEY_PASSWORD=...
```

Without them, `assembleRelease` still succeeds and produces an unsigned APK. Never commit a keystore; `*.jks` and `*.keystore` are gitignored.
