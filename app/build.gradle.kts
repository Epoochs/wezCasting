plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.wezcasting"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wezcasting"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        aidl = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.testng)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    val room_version = "2.6.1"

    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.room.compiler)

    // To use Kotlin annotation processing tool (kapt)
    kapt(libs.androidx.room.room.compiler)
    // To use Kotlin Symbol Processing (KSP)
    //ksp("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation (libs.play.services.location)
    implementation (libs.gson.v288)

    implementation (libs.play.services.maps.v1802)
    implementation(libs.play.services.maps)
    implementation (libs.osmdroid.android)
    implementation("androidx.core:core-splashscreen:1.0.0")

    testImplementation("androidx.test:core-ktx:1.5.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation ("org.robolectric:robolectric:4.8")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")



    testImplementation("app.cash.turbine:turbine:0.5.1")

    //implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("org.osmdroid:osmdroid-android:6.1.10")

    implementation("com.google.guava:guava:31.1-android")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
    // AndroidX Testing Libraries
    testImplementation ("androidx.arch.core:core-testing:2.1.0")  // For LiveData, StateFlow testing
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.7.2") // JUnit
    testImplementation ("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation ("org.mockito:mockito-core:4.6.1") // Mockito for mocking
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0") // Mockito Kotlin extensions
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2") // Coroutines Test
    implementation(kotlin("test"))

}