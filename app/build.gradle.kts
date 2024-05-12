plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mapkitresultproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mapkitresultproject"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
}

dependencies {

    //firebase
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")

    //Retrofit

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("androidx.compose.runtime:runtime-livedata:1.4.0-alpha04")
    implementation("com.google.firebase:firebase-database:20.3.0")

    //Room
    val room_version = "2.6.0"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:2.6.0")

    //jetpack navigation component
    val nav_version = "2.7.6"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")


    //Splash Api
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")

    //Dagger Hilt
    val hilt_version = "2.48"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //map kit
    implementation ("com.yandex.android:maps.mobile:4.5.0-full")
    // RxJava
    implementation ("io.reactivex.rxjava2:rxjava:2.2.7")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //Compose -->
    //This dependency integrates Jetpack Compose with the Activity class, enabling the use of Compose in Android activities.
    implementation("androidx.activity:activity-compose:1.8.1")
//Compose Bill of Materials (BOM), which helps manage versions of Compose libraries. In this case, it's using version 2023.03.00 for Compose dependencies.
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
//This dependency includes the fundamental UI elements and features provided by Jetpack Compose.
    implementation("androidx.compose.ui:ui")
//This includes tooling and preview functionalities for Compose, assisting with development and debugging UI components.
    implementation("androidx.compose.ui:ui-tooling-preview")
//This dependency includes the Material Design 3 components and styles adapted for Jetpack Compose, allowing the implementation of Material Design principles in your app's UI
    implementation("androidx.compose.material3:material3")
}