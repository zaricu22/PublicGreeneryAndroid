# Public Greenery

![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-21%20(Android%205.0)-blue)
![Target SDK](https://img.shields.io/badge/targetSdk-35%20(Android%2015)-blue)
![AGP](https://img.shields.io/badge/AGP-8.8.1-orange)
![Gradle](https://img.shields.io/badge/Gradle-8.10.2-02303A?logo=gradle)
![Room](https://img.shields.io/badge/Room-2.6.1-7C4DFF)
![Language](https://img.shields.io/badge/language-Java-ED8B00?logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

Android application for managing municipal greenery work — job planning, status tracking, weather forecast, and field notes. **Public Greenery** is a field-worker application for city greenery services. Workers can log in, view and manage their assigned jobs on a map, track job statuses, consult a 3-day weather forecast, and keep calendar notes — all in one place.

---

## Features

- **Login** — credential-based authentication backed by a local Room database
- **Profile** — displays logged-in worker's name, location, and job title
- **Plan** — list of jobs filterable by status and month; tap a job to view it on Google Maps; add new jobs with location picking; cycle job status (Planned → In Progress → Done)
- **Services**
  - 3-day weather forecast fetched from OpenWeatherMap API
  - Calendar with per-day note taking (stored in local Room database)
- **Portrait & Landscape** — full dual-layout support via separate layout resources and Navigation Component

---

## Tech Stack

| Library | Version | Purpose |
|---------|---------|---------|
| AndroidX AppCompat | 1.7.0 | Base activity/fragment support |
| Material Components | 1.12.0 | UI components and theming |
| ConstraintLayout | 2.1.4 | Flexible layouts |
| Navigation Component | 2.7.7 | Fragment navigation |
| Lifecycle LiveData | 2.8.7 | Observable data for login |
| Lifecycle ViewModel | 2.8.7 | Lifecycle-aware components |
| Room | 2.6.1 | Local SQLite database ORM |
| Gson | 2.10.1 | JSON serialization (user passing between activities, weather API) |
| Google Maps SDK | 19.0.0 | Map display in ShowActivity |
| Play Services Maps | 19.0.0 | Google Maps integration |
| LocationPicker | 1.2 | Map-based location picking in AddActivity |
| Volley | 1.2.0 | HTTP (transitive dep of LocationPicker) |
| JetBrains Annotations | 23.0.0 | Nullability annotations |

**Build tools:** AGP 8.8.1 · Gradle 8.10.2 · Java 8 source/target

---

## Architecture

The app follows a simple **Activity + Fragment** pattern with the **Android Navigation Component**:

```
LoginActivity
    └── MainActivity
            ├── ProfileFragment
            ├── PlanFragment
            │       ├── StatusListener / MesecListener (Spinner)
            │       ├── ShowJobsTask (AsyncTask — background DB query)
            │       ├── JobsAdapter (RecyclerView)
            │       └── StatusUpdateTask (AsyncTask — background DB update)
            └── ServicesFragment
                    ├── PrognozaTask (AsyncTask — HTTP weather API)
                    ├── WeatherAdapter (RecyclerView)
                    └── PrikazTask / UnosTask (AsyncTask — background DB read/write)

AddActivity     — add a new job with map location picker
ShowActivity    — view job details + Google Map marker
```

**Database:** Room (SQLite) with three entities: `User`, `Job`, `Event`  
**Background work:** `AsyncTask` (deprecated in API 30 but functional for targetSdk 35)  
**Navigation:** Jetpack Navigation Component with separate portrait/landscape nav graphs

---

## Permissions

| Permission | Reason |
|---|---|
| `INTERNET` | Weather API, Google Maps tiles |
| `ACCESS_NETWORK_STATE` / `ACCESS_WIFI_STATE` | Online check before map/weather calls |
| `ACCESS_COARSE_LOCATION` / `ACCESS_FINE_LOCATION` | Location picker, GPS check |
| `READ_GSERVICES` | Google Maps SDK |

---

## Test Account

The app uses a **predefined test user** that is automatically seeded into the local Room database on every launch and bypasses the manual login screen entirely.

| Field | Value |
|---|---|
| Username | `Perax` |
| Password | `px` |
| First name | Petar |
| Last name | Peric |
| Location | Veternik |
| Job title | Orezivac |

> **There is no registration screen.** The only users that exist are those hardcoded in `LoginActivity.java`. The login system only checks whether the typed credentials match a user already in the database — it cannot create new ones.

- **Just run the app** — auto-login fires on every launch, no credentials needed.
- **Switch to manual login** — comment out the entire auto-login block and uncomment the `btnLogin.setOnClickListener` block. The button checks credentials against the database via `LiveData`, but **only users already seeded in code will be accepted** — typing unknown credentials will always fail.

---

## Prerequisites

| Tool | Required version | Check |
|------|-----------------|-------|
| Android Studio | Ladybug Feature Drop 2024.2.2+ | `Help → About` |
| Android SDK | API 35 | SDK Manager |
| Java (JDK) | 17+ (bundled with Android Studio) | `java -version` |
| Gradle | 8.10.2 (via wrapper — no install needed) | `./gradlew --version` |
| Google Play Services | Installed on device/emulator | required for Maps |

---

## Getting Started

### Clone & Build

```bash
git clone https://github.com/zaricu22/PublicGreeneryAndroid.git
cd PublicGreeneryAndroid
./gradlew assembleDebug
```

The debug APK will be output to `app/build/outputs/apk/debug/app-debug.apk`.

### Run on Device / Emulator

1. Open in Android Studio
2. Select a device or emulator running Android 5.0+ (API 21+)
3. Click **Run**

> **Note:** The weather forecast and map features require an active internet connection. The location picker requires GPS to be enabled.

---

## Environment & Secrets

Both API keys are stored in `local.properties` (git-ignored) and injected at build time — no secrets are hardcoded in source files.

| Secret | Key in `local.properties` | Injected via |
|--------|--------------------------|--------------|
| Google Maps API key | `MAPS_API_KEY` | `manifestPlaceholders` → `AndroidManifest.xml` |
| OpenWeatherMap API key | `OPENWEATHER_API_KEY` | `BuildConfig.OPENWEATHER_API_KEY` → `PrognozaTask.java` |

To run the project, ensure `local.properties` contains your real keys:
```properties
MAPS_API_KEY=your_maps_key_here
OPENWEATHER_API_KEY=your_openweather_key_here
```

---

## API Connection

The app makes two external network calls — both require an active internet connection. The app checks connectivity before each call and shows a Toast if offline.

### OpenWeatherMap — Weather Forecast

| | Value |
|---|---|
| Endpoint | `GET https://api.openweathermap.org/data/2.5/forecast` |
| Params | `q=Novi%20Sad`, `units=metric`, `APPID=<key>` |
| Called by | `PrognozaTask.java` (background thread) |
| Response | JSON mapped via Gson to `WeatherResult` → `Hour[]` |
| Used | First, 9th, and 17th forecast slot (~today, tomorrow, day after) |

> The API key is embedded in `PrognozaTask.java` and expires on the free tier. Replace it at [openweathermap.org](https://openweathermap.org/api) if the forecast stops loading.

Weather icon images are fetched separately per forecast entry:
```
GET https://openweathermap.org/img/wn/{icon}@2x.png
```

### Google Maps SDK

| | Value |
|---|---|
| Used in | `ShowActivity.java` (job location map), `AddActivity.java` (location picker) |
| API key source | `strings.xml` → `google_api_key` → `AndroidManifest.xml` meta-data |
| Required APIs | Maps SDK for Android, Places API (via LocationPicker) |

> Enable both APIs in the [Google Cloud Console](https://console.cloud.google.com/) for the key in `strings.xml`.

---

## Project Structure

```
app/src/main/
├── java/com/example/myapplication/
│   ├── activities/
│   │   ├── LoginActivity.java       # Entry point, DB login
│   │   ├── MainActivity.java        # Nav host, portrait + landscape
│   │   ├── AddActivity.java         # Add new job with location
│   │   ├── AddTask.java             # AsyncTask: insert job to DB
│   │   ├── ShowActivity.java        # Show job + Google Map
│   │   └── ShowTask.java            # AsyncTask: load job from DB
│   ├── database/
│   │   ├── AppDatabase.java         # Room database (v9)
│   │   ├── DatabaseDAO.java         # DAO interface
│   │   ├── DateConverter.java       # Room TypeConverter for Date
│   │   ├── User.java                # Entity
│   │   ├── Job.java                 # Entity
│   │   └── Event.java               # Entity (calendar notes)
│   └── fragments/
│       ├── profile/
│       │   ├── ProfileFragment.java
│       │   └── ProfileTask.java     # (unused — profile image loading)
│       ├── plan/
│       │   ├── PlanFragment.java
│       │   ├── JobsAdapter.java     # RecyclerView adapter
│       │   ├── ShowJobsTask.java    # AsyncTask: query jobs
│       │   ├── StatusUpdateTask.java# AsyncTask: update job status
│       │   ├── StatusListener.java  # Spinner listener
│       │   └── MesecListener.java   # Spinner listener
│       └── services/
│           ├── ServicesFragment.java
│           ├── PrognozaTask.java    # AsyncTask: fetch weather
│           ├── WeatherAdapter.java  # RecyclerView adapter
│           ├── PrikazTask.java      # AsyncTask: load calendar note
│           ├── UnosTask.java        # AsyncTask: save calendar note
│           └── gson/                # Weather API POJO models
├── res/
│   ├── layout/                      # Portrait layouts
│   ├── layout-land/                 # Landscape layouts
│   ├── navigation/mobile_navigation.xml
│   └── values/
└── AndroidManifest.xml
```

---

## Development Notes

### Pre-Build Fixes

The old version of the project required the following fixes before it could build successfully. These are already applied in the current codebase.

#### Build Infrastructure (10 fixes)

| # | Issue | Fix Applied |
|---|-------|-------------|
| 1 | `gradle/wrapper/gradle-wrapper.jar` missing | Copied from another local project |
| 2 | `gradle-wrapper.properties` missing | Created; points to Gradle 8.10.2 |
| 3 | AGP 3.5.1 incompatible with Gradle 8.x | Upgraded to AGP 8.8.1 (was already cached locally) |
| 4 | `jcenter()` repository shut down | Replaced with `mavenCentral()` |
| 5 | Avast Web/Mail Shield SSL inspection blocking all HTTPS downloads | Exported Avast root cert from Windows store → imported into JVM `cacerts` |
| 6 | `LocationPicker:v1.2` wrong JitPack tag | Changed to `LocationPicker:1.2` (no `v` prefix) |
| 7 | Transitive dep `volley:1.1.1` not published on any repo | Forced `volley:1.2.0` via `resolutionStrategy` |
| 8 | `package` attribute in `AndroidManifest.xml` | Removed; replaced by `namespace` in `app/build.gradle` (AGP 8.x requirement) |
| 9 | `minSdk 16` too low for new library versions | Raised to `21` |
| 10 | Missing `android:exported="true"` on `LoginActivity` | Added (required for activities with intent-filters on targetSdk ≥ 31) |


---

### Outdated Dependencies

Checked **2026-06-30**. Current versions are functional; updates listed for reference.

#### Build Config

| Setting | Current | Latest |
|---------|---------|--------|
| `compileSdk` / `targetSdk` | 35 | **36** (Android 16) |
| `minSdk` | 21 (Android 5.0) | — |
| Java source/target | 8 | **17** recommended |
| AGP | 8.8.1 | **9.2.1** (requires Gradle 9.x + Java 17) |
| Gradle | 8.10.2 | **9.6.1** |

#### Libraries

| Library | Current | Latest Stable |
|---------|---------|---------------|
| `appcompat` | 1.7.0 | 1.7.1 |
| `material` | 1.12.0 | **1.14.0** |
| `constraintlayout` | 2.1.4 | **2.2.1** |
| `vectordrawable` | 1.2.0 | 1.2.0 ✓ |
| `navigation-fragment/ui` | 2.7.7 | **2.9.8** |
| `lifecycle-livedata/viewmodel` | 2.8.7 | **2.11.0** |
| `annotation` | 1.9.1 | 1.10.0 |
| `room-runtime` / `room-compiler` | 2.6.1 | **2.8.4** |
| `gson` | 2.10.1 | **2.14.0** |
| `play-services-maps` | 19.0.0 | **20.0.0** |
| `volley` (forced) | 1.2.0 | 1.2.1 |
| `junit` | 4.13.2 | 4.13.2 ✓ |
| `test.ext:junit` | 1.2.1 | 1.3.0 |
| `espresso-core` | 3.6.1 | 3.7.0 |
| `org.jetbrains:annotations` | 23.0.0 | **26.1.0** |
| `LocationPicker` | 1.2 | 1.2 ✓ _(last release — unmaintained)_ |

> `LocationPicker` (`com.github.shivpujan12:LocationPicker`) has had no releases since 1.2. Consider replacing with the Google Places SDK or another maintained alternative.

---

### IDE & Tooling Versions

| | Version |
|---|---|
| **Android Studio** | Ladybug Feature Drop \| 2024.2.2 (`AI-242.23726.103.2422.13016713`) |

---

## Known Issues & TODO

- `AsyncTask` is deprecated since API 30 — consider migrating to `ExecutorService` + `Handler` or Kotlin Coroutines
- `LocationPicker` library is unmaintained — replace with Google Places SDK long-term
- `ProfileTask.java` (profile image loading) is implemented but disabled
- Auto-login and hardcoded user seed in `LoginActivity.java` must be removed before any production use
