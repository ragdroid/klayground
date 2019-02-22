
object Versions {
    val kotlin_version = "1.3.11"

    val compileSdk = 28
    val minSdk = 21
    val retrofitVersion = "2.3.0"
    val support_library = "27.1.1"
    val material = "1.0.0"
    val okhttpVersion = "3.9.1"
    val constraint_layout = "2.0.0-alpha3"
    val navigationVersion = "1.0.0-rc01"
    val lifecycleEx = "2.0.0"
    val lifecycleRx = "2.0.0"
    val lifecycleCompiler = "2.0.0"
    val arch_comp       = "1.1.1"
    val arch_comp_paging= "2.1.0"
    val dagger          = "2.20"
    val ktlint          = "0.10.0"
    val leakcanary      = "1.6.2"
    val rx              = "2.1.6"
    val rxAndroid       = "2.1.0"
    val rxlint          = "1.6"
    val junit           = "4.12"
    val timberVersion   = "4.7.1"
    val glideVersion    = "4.8.0"
    val databinding     = "3.0.0"
    val reclaim         = "1.2.2"
    val rxBindings      = "2.1.1"
    val appCompat = "1.0.2"

    // test libraries
    val kluent = "1.4"
    val spek = "1.1.5"
    val mockitoKotlin = "1.5.0"
    val mockito = "2.8.9"
    val testRunner = "1.1.1"
    val espresso = "3.1.0"
    val crashlytics = "2.7.1"
}

object Deps {
    val coreKtx = "androidx.core:core-ktx:0.3"
    val kotlin_jdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin_version}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"
    val timber = "com.jakewharton.timber:timber:${Versions.timberVersion}"
    val reclaim = "com.github.fueled:reclaim:${Versions.reclaim}"
    val lifecycleEx = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleEx}"
    val lifecycleRx = "androidx.lifecycle:lifecycle-reactivestreams:${Versions.lifecycleRx}"
    val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycleRx}"
    val navigationFragment = "android.arch.navigation:navigation-fragment-ktx:${Versions.navigationVersion}"
    val navigationUIKtx = "android.arch.navigation:navigation-ui-ktx:${Versions.navigationVersion}"
    val pagingRuntime = "androidx.paging:paging-runtime:${Versions.arch_comp_paging}"
    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    val material = "com.google.android.material:material:${Versions.material}"
    val glide = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
    val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glideVersion}"
    val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    val daggerAndroid = "com.google.dagger:dagger-android:${Versions.dagger}"
    val daggerAndroidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    val rx = "io.reactivex.rxjava2:rxjava:${Versions.rx}"
    val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    val rxBindingKt = "com.jakewharton.rxbinding2:rxbinding-kotlin:${Versions.rxBindings}"
    val rxBindingRvKt = "com.jakewharton.rxbinding2:rxbinding-recyclerview-v7-kotlin:${Versions.rxBindings}"
    val rxBindingSupportV4 = "com.jakewharton.rxbinding2:rxbinding-support-v4-kotlin:${Versions.rxBindings}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofitVersion}"
    val retrofitRxJavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofitVersion}"
    val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttpVersion}"
    val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttpVersion}"
}

object TestDeps {
    val junit = "junit:junit:${Versions.junit}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    val testRunner = "androidx.test:runner:${Versions.testRunner}"
    val rxLint = "nl.littlerobots.rxlint:rxlint:${Versions.rxlint}"
    val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakcanary}"
    val leakCanaryNoOp = "com.squareup.leakcanary:leakcanary-android-no-op:${Versions.leakcanary}"
    val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}@aar"
    val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.okhttpVersion}"
    val kluent = "org.amshove.kluent:kluent:${Versions.kluent}"
    val spek = "org.jetbrains.spek:spek-api:${Versions.spek}"
    val spekJunitPlatformEngine = "org.jetbrains.spek:spek-junit-platform-engine:${Versions.spek}"
    val mockitoKotlin = "com.nhaarman:mockito-kotlin-kt1.1:${Versions.mockitoKotlin}"
    val mockito = "org.mockito:mockito-core:${Versions.mockito}"
}