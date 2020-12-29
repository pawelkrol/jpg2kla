object ProguardConf {

  val jpg2kla =
"""
-dontnote
-dontskipnonpubliclibraryclasses
-dontwarn
-ignorewarnings
-keepattributes Signature
-optimizations "code/allocation/*,code/merging,code/removal/*,code/simplification/*,class/marking/*,class/merging/*,class/unboxing/*,field/*,method/inlining/*,method/marking/*,method/propagation/*,method/removal/*"
-optimizationpasses 5

-keepclassmembers class * {
  ** MODULE$;
}

-keepclassmembernames class scala.concurrent.forkjoin.ForkJoinPool {
  volatile long ctl;
  volatile long stealCount;
  volatile int plock;
  volatile int indexSeed;
}

-keepclassmembernames class scala.concurrent.forkjoin.ForkJoinPool$WorkQueue {
  volatile int qlock;
}

-keepclassmembernames class scala.concurrent.forkjoin.ForkJoinTask {
  int status;
}

-keep public class ij.plugin.ClassChecker {
}
"""
}
