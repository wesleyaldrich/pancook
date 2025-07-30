plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.wesleyaldrich.pancook"
    compileSdk = 35 // You have compileSdk = 35 and minSdk = 35, targetSdk = 35. Make sure your local setup supports this, as usually Android Studio defaults to 34 or 33.

    defaultConfig {
        applicationId = "com.wesleyaldrich.pancook"
        minSdk = 35 // Consider lowering to 24 or 21 if you need broader device compatibility
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Ensure this matches your Compose BOM version compatibility
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "35.0.0" // Ensure you have Build-Tools 35.0.0 installed in SDK Manager
}

// CORRECTED DEPENDENCIES BLOCK SYNTAX
dependencies {
    // Dependencies using Version Catalogs (libs.) - KEEP THESE AS IS
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.android) // This might be redundant if Material3 is covered by BOM
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)


    // Dependencies declared as string literals - CHANGE THESE TO USE THE CORRECT SYNTAX
    // This is the correct way to declare string dependencies in build.gradle.kts
    implementation("androidx.compose.material:material-icons-extended:1.6.8") // Use your Compose BOM version for consistency
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0")

    // ExoPlayer dependencies
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.19.1")

    // Coil dependencies
    implementation("io.coil-kt:coil-compose:2.7.0") // Using a more recent stable version (2.4.0 might be old)
    implementation("androidx.core:core-splashscreen:1.0.1") // This one is fine
}