

plugins {
    id("com.android.application")
}

android {
    namespace = "com.mugswpp.gathernow"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.mugswpp.gathernow"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.json:json:20210307")
    implementation("androidx.test.espresso:espresso-intents:3.5.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")


    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.fragment:fragment-testing:1.6.2")
    androidTestImplementation ("org.mockito:mockito-core:5.5.0")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.squareup.retrofit2:converter-gson:2.4.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    // NAVER Map SDK
    implementation("com.naver.maps:map-sdk:3.17.0")

    testImplementation("org.robolectric:robolectric:4.11.1")
    androidTestImplementation("junit:junit:4.12")
}
