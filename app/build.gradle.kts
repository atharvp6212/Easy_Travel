plugins {
    id("com.android.application")
}

android {
    namespace = "com.atharv.image_capt_tran"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.atharv.image_capt_tran"
        minSdk = 26
        targetSdk = 33
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("de.hdodenhof:circleimageview:2.1.0")
    implementation("com.google.mlkit:text-recognition:16.0.0")
    implementation("com.google.mlkit:language-id:17.0.5")
    implementation("com.google.mlkit:text-recognition-devanagari:16.0.0")
    implementation("com.google.mlkit:translate:17.0.2")
    implementation("com.soundcloud.android:android-crop:1.0.1@aar")
    implementation("com.google.android.gms:play-services-maps:18.0.1")
}