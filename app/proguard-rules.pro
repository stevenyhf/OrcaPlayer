-keepattributes Signature,Annotation,InnerClasses,EnclosingMethod
-dontwarn dagger.hilt.**
-keep class,hide **$$HiltComponents
-keep,allowobfuscation @dagger.hilt.*
-keepclassmembers,allowobfuscation class * {
    @androidx.room.* <methods>;
}
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-keep class androidx.media3.** { *; }
-keep interface androidx.media3.** { *; }
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
