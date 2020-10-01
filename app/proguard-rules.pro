# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class androidx.datastore.*.** {*;}
#noinspection ShrinkerUnresolvedReference


-keepattributes Signature

-keep class com.github.harmittaa.touchobserver.model.SingleEvent {
    <fields>;
    <init>();
    <methods>;
}

-keep class com.github.harmittaa.touchobserver.model.TouchGesture {
    <fields>;
    <init>();
    <methods>;
}

-keep class com.github.harmittaa.touchobserver.model.UserData {
    <fields>;
    <init>();
    <methods>;
}

-keepclassmembers class com.github.harmittaa.touchobserver.logic.** { *; }
-keepclassmembers class com.github.harmittaa.touchobserver.repository.** { *; }
