# ── Hilt ──
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# ── Room ──
-keep class com.kodemukti.namaibayi.data.local.entity.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# ── Gson ──
-keep class com.kodemukti.namaibayi.data.remote.dto.** { *; }
-keep class com.kodemukti.namaibayi.data.remote.gemini.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ── Retrofit ──
-keep,allowobfuscation interface com.kodemukti.namaibayi.data.remote.api.NamAIApi
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# ── OkHttp ──
-dontwarn okhttp3.**
-dontwarn okio.**

# ── Kotlin Coroutines ──
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# ── Domain Models (JSON parsing via Gson) ──
-keep class com.kodemukti.namaibayi.domain.model.** { *; }

# ── DataStore ──
-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite { *; }
