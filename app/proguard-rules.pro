# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\My zOthers\My Programs\Android SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn com.squareup.okhttp.**
-dontwarn org.apache.**

-keep class com.fenproductions.movietoday.entity.** { *; }
-keepclassmembers class com.fenproductions.movietoday.entity.** { *; }

-keep class sun.misc.Unsafe { *; }
-keep class org.apache.commons.logging.**
-keep class com.google.gson.examples.android.model.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }
-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }

-keep class org.apache.http.* { *; }
-keep class org.apache.http.client.** { *; }
-keep class org.apache.http.cookie.** { *; }
-keep class org.apache.http.impl.cookie.** { *; }
-keep class org.apache.http.message.** { *; }
-keep class org.apache.http.util.** { *; }

-keepattributes *Annotation*
-keepattributes Signature
