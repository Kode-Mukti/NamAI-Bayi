# Keep Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep Room entities
-keep class com.kodemukti.namaibayi.data.local.entity.** { *; }

# Keep Gson models
-keep class com.kodemukti.namaibayi.data.remote.dto.** { *; }

# Keep AI prompt schemas
-keep class com.kodemukti.namaibayi.domain.model.** { *; }
