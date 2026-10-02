# Andamp source template

The smallest music source [Andamp](https://github.com/mattijsf/andamp) can play from: one
artist, one album, one song. Copy it to start a source of your own.

A source is a separate app installed beside Andamp. Andamp finds it by the intent it
answers, lists it under Preferences > Music sources and in the Media Library, and plays
what it decodes through its own equalizer, effects and visualizer.

## What is here

```
app/src/main/AndroidManifest.xml   the service Andamp binds, and the settings screen it opens
SourceService.kt                   identity, sign-in state, library, and where a track's audio is
SourceIdentity.kt                  scheme, label, launcher alias, update and home pages
TemplateLibrary.kt                 artists, albums, tracks
SettingsActivity.kt                the settings screen
```

Everything else (the binder, lifetime, the audio pipe, opening and decoding a URL) comes
from the SDK: `nl.mattix.andamp:source-api` and `nl.mattix.andamp:source-common` on Maven
Central.

## Make it yours

Rename it first. The template's names are all `nl.mattix.andamp.pack.template`; replace
them with a package of your own, for example `com.example.mysource`. Do the steps in this
order and then run `./gradlew gate assembleDebug`.

1. In `app/build.gradle.kts`, set `namespace` and `applicationId` to your package.
2. Move the Kotlin files under `app/src/main/java/` and `app/src/test/java/` to the
   directory of your package and change their `package` lines. `R` and `BuildConfig` are
   generated in the `namespace` package, so the build fails until the two agree.
3. In `app/src/main/AndroidManifest.xml`, change the four fully qualified names: the
   `android:name` of the service, of the activity and of the activity alias, and the
   alias's `android:targetActivity`. A name that does not match a class makes the app
   crash when Andamp binds it or opens its settings.
4. Set `SourceIdentity.LAUNCHER_ALIAS` to the alias's new `android:name`. If the two
   differ, the service and the settings screen throw when they start.
5. Choose the scheme in `SourceIdentity.SCHEME`. Every track address starts with it and
   saved playlists record it, so it cannot change after a release.
6. Set the name: `SourceIdentity.LABEL` is what Andamp shows, and `app_name` in
   `app/src/main/res/values/strings.xml` is the name in the app list.
7. Set `rootProject.name` in `settings.gradle.kts` and `package-name` in
   `release-please-config.json` to the name of your repository.

Then make it play your music:

1. Serve your library from `TemplateLibrary` and return each track's audio URL from
   `audioOf`. The template's song points at a URL that does not exist.
2. Ask for a server or account in `SettingsActivity`, and report who is signed in from
   `SourceService.whoIsHere`.
3. Optionally set `SourceIdentity.UPDATES` and `SourceIdentity.HOME`, so Andamp can
   report a newer version and say where the source comes from.

The contract is documented in the player's
[source-packs.md](https://github.com/mattijsf/andamp/blob/main/docs/source-packs.md).
[andamp-source-subsonic](https://github.com/mattijsf/andamp-source-subsonic) and
[andamp-source-jellyfin](https://github.com/mattijsf/andamp-source-jellyfin) are complete
sources to read.

## Build

JDK 17 and the Android SDK (compile SDK 36).

```bash
./gradlew assembleDebug      # the APK
./gradlew installDebug       # onto a connected phone, beside Andamp
./gradlew gate               # format, detekt and unit tests
```

`lefthook install` sets up the pre-push hook, which runs `gate`.

The app is built against Andamp's source SDK from Maven Central. To test an unreleased SDK
change, run `./gradlew publishToMavenLocal` in a checkout of the player; the local
artifacts take precedence.

Release signing is optional. A `keystore.properties` file in the project root, which git
ignores, may hold `storeFile` (a path relative to the project root), `keyAlias`,
`storePassword` and `keyPassword`. `SOURCE_STORE_PASSWORD` and `SOURCE_KEY_PASSWORD` in the
environment take precedence over the two passwords in the file.

- Without the file, or without `storeFile` in it, `assembleRelease` makes an unsigned APK.
- With `storeFile`, the APK is signed with that key. The build fails when a password is in
  neither the environment nor the file.

## License

Apache License 2.0; see [LICENSE](LICENSE). A source made from this may use any license.
