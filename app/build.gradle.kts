plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.cowlar.task.mqqtsampletask"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cowlar.task.mqqtsampletask"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:4.3.1")
    testImplementation( "org.mockito:mockito-inline:4.3.1")
    testImplementation( "org.robolectric:robolectric:4.7.3")

    testImplementation( "org.jetbrains.kotlin:kotlin-test-junit:1.6.21")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.4")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
}