# Keep Gson model classes used by Retrofit (openFDA / RxNorm DTOs)
-keep class uk.ac.tees.mad.s3533898.data.remote.dto.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Retrofit / OkHttp
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
