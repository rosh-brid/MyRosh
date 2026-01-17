# ============================================================
# BASIC ANDROID CONFIGURATION
# ============================================================
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Keep important Android classes
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep parcelable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep R classes
-keep class **.R
-keep class **.R$* {
    <fields>;
}

# ============================================================
# KOTLIN SPECIFIC
# ============================================================
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-dontwarn org.jetbrains.annotations.**
-keepattributes Signature, InnerClasses, EnclosingMethod

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keep class kotlinx.coroutines.android.AndroidDispatcherFactory { *; }

# ============================================================
# ADMOB / GOOGLE PLAY SERVICES
# ============================================================
# Keep AdMob classes
-keep public class com.google.android.gms.ads.** {
    public *;
}
-keep class com.google.ads.** { *; }

# AdMob mediation
-keep class com.google.android.gms.ads.mediation.** { *; }
-keep class * implements com.google.android.gms.ads.mediation.customevent.CustomEvent {
    public *;
}

# Ad Request
-keep class com.google.android.gms.ads.AdRequest { *; }
-keep class com.google.android.gms.ads.AdSize { *; }
-keep class com.google.android.gms.ads.MobileAds { *; }

# Ad Listeners
-keepclassmembers class * {
    @com.google.android.gms.ads.annotation.KeepForSdk <methods>;
}

# Keep Ad ID
-keep class com.google.android.gms.ads.identifier.** { *; }

# Google Play Services
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# ============================================================
# CAMERAX
# ============================================================
# CameraX Core
-keep class androidx.camera.core.** { *; }
-keep class androidx.camera.camera2.** { *; }
-keep class androidx.camera.lifecycle.** { *; }
-keep class androidx.camera.view.** { *; }

# CameraX extensions
-keep class androidx.camera.extensions.** { *; }

# CameraX internal
-keep class androidx.camera.impl.** { *; }

# Camera2 API
-keep class android.hardware.camera2.** { *; }
-dontwarn android.hardware.camera2.**

# Keep CameraX View classes
-keep class androidx.camera.view.PreviewView { *; }
-keep class androidx.camera.view.CameraController { *; }

# ============================================================
# FILE OPERATIONS & STORAGE
# ============================================================
# File operations
-keep class java.io.** { *; }
-keep class java.nio.** { *; }

# Media files
-keep class android.media.** { *; }
-dontwarn android.media.**

# Storage Access Framework
-keep class android.provider.DocumentsContract { *; }
-keep class android.support.v4.provider.DocumentFile { *; }
-keep class androidx.documentfile.provider.DocumentFile { *; }

# ContentResolver
-keep class * implements android.content.ContentProvider { *; }

# ============================================================
# IMAGE PROCESSING & EDITING
# ============================================================
# Bitmap operations
-keep class android.graphics.** { *; }
-keep class android.graphics.Bitmap { *; }
-keep class android.graphics.BitmapFactory { *; }
-keep class android.graphics.BitmapFactory$Options { *; }

# Image editing libraries
-keep class androidx.exifinterface.** { *; }

# RenderScript (jika digunakan)
-keep class android.support.v8.renderscript.** { *; }
-keep class androidx.renderscript.** { *; }

# ============================================================
# UI & VIEW BINDING
# ============================================================
# View Binding
-keep class * implements androidx.viewbinding.ViewBinding {
    public static * inflate(android.view.LayoutInflater);
    public static * inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
    public static * bind(android.view.View);
}

# Data Binding
-keep class androidx.databinding.** { *; }
-dontwarn androidx.databinding.**

# Material Components
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# ConstraintLayout
-keep class androidx.constraintlayout.** { *; }

# ============================================================
# NETWORKING & INTERNET
# ============================================================
# OkHttp (jika digunakan)
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**

# Retrofit (jika digunakan)
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson (jika digunakan)
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# ============================================================
# GUAVA (yang sudah kamu gunakan)
# ============================================================
-keep class com.google.common.** { *; }
-keep class com.google.guava.** { *; }
-dontwarn com.google.common.**
-dontwarn com.google.guava.**

# ============================================================
# PERMISSIONS
# ============================================================
-keep class androidx.core.app.** { *; }
-keep class androidx.activity.result.** { *; }

# ============================================================
# LIFECYCLE & VIEWMODEL
# ============================================================
-keep class androidx.lifecycle.** { *; }
-keep class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}
-keepclasseswithmembers class * {
    @androidx.lifecycle.OnLifecycleEvent *;
}

# ============================================================
# THIRD-PARTY LIBRARIES (jika ada)
# ============================================================
# Glide (jika digunakan)
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Picasso (jika digunakan)
-keep class com.squareup.picasso.** { *; }

# Lottie (jika digunakan)
-keep class com.airbnb.lottie.** { *; }

# ============================================================
# CUSTOM APPLICATION CLASSES
# ============================================================
# Keep your application classes
-keep class rosh.myrosh.** { *; }
-keep class * extends rosh.myrosh.BaseActivity { *; }
-keep class * extends rosh.myrosh.BaseFragment { *; }

# Keep activities, fragments, services
-keep class * extends android.app.Activity
-keep class * extends androidx.fragment.app.Fragment
-keep class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver

# ============================================================
# DEBUGGING & LOGGING
# ============================================================
# Keep line numbers for stack traces
-keepattributes SourceFile,LineNumberTable

# Keep logging (optional)
-keep class * extends java.lang.Exception
-keep class * extends java.lang.Throwable

# ============================================================
# RESOURCE BINDING
# ============================================================
# Keep resource IDs
-keepclassmembers class **.R$* {
    public static <fields>;
}

# ============================================================
# MANIFEST & METADATA
# ============================================================
# Keep manifest attributes
-keepattributes Manifest
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

# ============================================================
# JNI & NATIVE LIBRARIES
# ============================================================
# JNI methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep native library names
-keep class * {
    @android.webkit.JavascriptInterface <methods>;
}

# ============================================================
# WEBVIEW (jika aplikasi punya WebView)
# ============================================================
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}

# ============================================================
# DON'T WARN FOR THESE
# ============================================================
# Suppress warnings
-dontwarn android.support.**
-dontwarn androidx.**
-dontwarn com.google.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.intellij.lang.annotations.**
-dontwarn org.jetbrains.annotations.**

# ============================================================
# KEEP ALL CLASSES IN SPECIFIC PACKAGES (untuk testing)
# ============================================================
# Uncomment jika ingin keep semua class di package tertentu
# -keep class rosh.myrosh.ui.** { *; }
# -keep class rosh.myrosh.data.** { *; }
# -keep class rosh.myrosh.util.** { *; }

# ============================================================
# FINAL CONFIGURATION
# ============================================================
# Rename source file attribute
-renamesourcefileattribute SourceFile

# Print mapping (untuk debugging)
# -printmapping mapping.txt
# -printseeds seeds.txt
# -printusage unused.txt