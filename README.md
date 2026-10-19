# demo-shop-app

Offline Android demo shop (Kotlin + Jetpack Compose) used to try QA agents such as Droid CUA.

The app is a small storefront branded **Loadmill Demo Shop** so it is clearly a demo: six hard-coded products with bundled photos, an in-memory cart, a pre-filled demo checkout, and an order confirmation. It is fully offline: no network access (no `INTERNET` permission), no persistence, and no real payments. Relaunching the app starts with an empty cart.

- Application ID: `com.loadmill.demoshop`
- App label: Demo Shop
- Version: 1.0.0 (versionCode 1)
- minSdk 26, targetSdk 36
- Light theme only, edge-to-edge, Plus Jakarta Sans font. Photo and font credits are in [`CREDITS.md`](CREDITS.md).

## Demo journey

1. **Catalog**: the home screen shows the **Loadmill Demo Shop** wordmark (a shopping-bag mark with a small "LOADMILL" label above **Demo Shop**), a dark **Cart** button with the item count (for example **Cart 0**), category chips (**All**, **Gear**, **Apparel**, **Accessories**) and a two-column product grid. **Backpack** is the first product. Each card has a compact **Add to cart** button; tapping the card itself opens the product page. Products in the cart show a "1 in cart" tag on their photo.
2. **Product page**: tap **Backpack** to see a large photo, the price, a description, and an **Add to cart** button at the bottom. Tapping it adds the item and opens an **Added to cart** sheet with the product thumbnail, name and price, a **View cart** button and a **Continue shopping** button. **View cart** opens the cart (back from the cart returns to the product page); **Continue shopping** returns to the catalog. Swiping the sheet down, tapping outside it or pressing system back just closes it and stays on the product page, where "1 in your cart" appears above the button and the **Cart** button shows **1**. Use the round back button (or system back) to return to the catalog. The compact **Add to cart** buttons on catalog cards add the item without opening the sheet.
3. **Your cart**: tap **View cart** (or **Cart**) to see each item with a thumbnail, a **-** / **+** quantity stepper and **Remove**, plus an order summary (subtotal, shipping **Free**, total). Tap **Checkout** (disabled when the cart is empty; the empty cart offers **Continue shopping**).
4. **Checkout**: grouped **Contact**, **Shipping** and **Payment** sections pre-filled with demo details (Jane Doe, demo card `4242 4242 4242 4242`, expiry `12/30`, CVC `123`), a preview of the demo card, and an order summary. Tap **Place order**. Empty fields are flagged as **Required** and "Please fill in all fields." is shown above the button.
5. **Order confirmed**: a success check, the order number (`DS-123456` style), delivery info, the items and the total. Tap **Continue shopping** to clear the cart and return to the catalog.

Screen transitions and small feedback animations last at most 300 ms; nothing loops or auto-advances.

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
