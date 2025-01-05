plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.capston_camera"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.capston_camera"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // OpenCV 의존성 추가
    implementation("org.opencv:opencv-android:4.5.1")

    // AndroidX 라이브러리 의존성 추가
    implementation("androidx.core:core-ktx:1.10.0") // androidx.core.ktx
    implementation("androidx.appcompat:appcompat:1.3.0") // androidx.appcompat
    implementation("com.google.android.material:material:1.9.0") // androidx.material
    implementation("androidx.activity:activity-ktx:1.7.2") // androidx.activity
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("commons-io:commons-io:2.4")
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view) // androidx.constraintlayout

    // 테스트 라이브러리 의존성 추가
    testImplementation("junit:junit:4.13.2") // junit
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // androidx.junit
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // androidx.espresso.core
}
