plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "pepes.co.libfox"
    compileSdk = 36

    defaultConfig {
        applicationId = "pepes.co.libfox"
        minSdk = 26
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // RecyclerView & ViewPager2
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // CardView & CoordinatorLayout
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
