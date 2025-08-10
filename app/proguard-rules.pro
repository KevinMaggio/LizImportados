# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# ========== FIREBASE RULES ==========
# Keep Firebase classes
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Firestore models - Keep all data classes used with Firestore
-keep class com.refactoringlife.lizimportadosv2.core.dto.** { *; }
-keepclassmembers class com.refactoringlife.lizimportadosv2.core.dto.** {
    <fields>;
    <init>(...);
}

# ========== JETPACK COMPOSE RULES ==========
# Keep Compose classes
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** {
    <fields>;
    <methods>;
}

# ========== KOTLIN COROUTINES ==========
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class * {
    @kotlinx.coroutines.DelicateCoroutinesApi *;
}

# ========== AUTHENTICATION MODELS ==========
-keep class com.refactoringlife.lizimportadosv2.core.auth.AuthUser { *; }
-keep class com.refactoringlife.lizimportadosv2.core.auth.AuthResult { *; }

# ========== KEEP SOURCE FILE INFO FOR DEBUGGING ==========
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ========== GOOGLE PLAY SERVICES ==========
-keep class com.google.android.gms.auth.api.signin.** { *; }
-keep interface com.google.android.gms.auth.api.signin.** { *; }

# ========== SERIALIZATION ==========
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,RuntimeVisibleTypeAnnotations

# ========== PREVENT CRASHES ==========
-keep class * extends java.lang.Exception
-dontwarn javax.annotation.**
-dontwarn org.checkerframework.**

# ========== OKHTTP (if used by Firebase) ==========
-dontwarn okhttp3.**
-dontwarn okio.**