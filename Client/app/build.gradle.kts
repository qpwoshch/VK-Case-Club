plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

android {
    namespace = "com.fiveBoys.rustore"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.fiveBoys.rustore"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures { compose = true }
}

dependencies {
    implementation(libs.androidx.runtime)
    testImplementation(libs.junit.junit)
    val composeBom = platform("androidx.compose:compose-bom:2024.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.androidx.core.ktx.v1131)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // --- Compose ---
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    // --- Material Components (для XML тем и AppCompat атрибутов) ---
    implementation(libs.material.v1130)

    // --- ConstraintLayout (для layout_constraint*) ---
    implementation(libs.androidx.constraintlayout.v221)

    // --- Navigation (для NavHostFragment, defaultNavHost и т.п.) ---
    implementation(libs.androidx.navigation.fragment.ktx.v277)
    implementation(libs.androidx.navigation.ui.ktx.v277)

    // --- DataStore ---
    implementation(libs.androidx.datastore.preferences)

    // --- Network ---
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.logging.interceptor)

    // --- Images ---
    implementation(libs.coil.compose)
}
