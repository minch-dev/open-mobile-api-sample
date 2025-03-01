plugins {
    id("com.android.application")
}

android {
    namespace = "org.simalliance.openmobileapi.test"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.simalliance.openmobileapi.test"
        minSdk = 27
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"
		multiDexEnabled = false
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}