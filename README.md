# MediMate, Medication Manager & Interaction Checker

CIS4034-N Mobile App Development · Teesside University · Deepak Vanukuru (S3533898)
Package: `uk.ac.tees.mad.s3533898`

MediMate is an offline first Android app that helps people who take several
medicines manage them safely: add medicines (by typing or scanning the box
barcode), get authoritative drug information from the US FDA, screen for possible
drug interactions, and receive dose reminders, with all health data kept on the
device behind a biometric lock.

## Features
- **Local account** with a PBKDF2-hashed PIN and fingerprint/face unlock (BiometricPrompt)
- **My Medicines** dashboard (Room, offline first)
- **Search** the FDA openFDA drug database over HTTPS, or **scan a barcode** (CameraX + ML Kit)
- **Medicine detail** with dosage, warnings and interactions, cached for offline
- **Interaction screening** across all your medicines (label based, with a clear disclaimer)
- **Dose reminders** (AlarmManager + notifications, re armed after reboot)
- **Privacy & security**: SQLCipher encrypted database, HTTPS only, least privilege
  permissions, GDPR "delete all my data", and an on screen medical disclaimer

## Tech stack
Kotlin · Jetpack Compose (Material 3) · MVVM + Repository · Hilt · Room ·
Retrofit/OkHttp/Gson · Coroutines/Flow · CameraX + ML Kit Barcode ·
AndroidX Biometric · DataStore · SQLCipher · openFDA & RxNorm web services.

## Build & run
1. Open the `MediMate/app` folder in Android Studio (Giraffe/Koala or newer).
2. Ensure an Android SDK with **API 36** is installed (the project targets compileSdk 36, minSdk 26).
3. Let Gradle sync (AGP 8.13, Gradle 8.13, Kotlin 2.0). `local.properties` must point to your SDK.
4. Run on an emulator or device (API 26+). No API keys are required, openFDA and RxNorm are free and keyless.

Command line:
```
cd MediMate/app
./gradlew :app:assembleDebug        # build the debug APK
./gradlew :app:testDebugUnitTest    # run unit tests
```

## Data sources
- openFDA Drug Label API, https://api.fda.gov/drug/label.json
- RxNorm / RxNav, https://rxnav.nlm.nih.gov/

MediMate is an information aid, **not** a medical device. Always consult a
qualified professional.
