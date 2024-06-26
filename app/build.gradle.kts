

plugins {
    id("com.android.application")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

}

android {
    namespace = "ca.cmput301t05.placeholder"
    compileSdk = 34


    packaging {
        resources.excludes.addAll(
                listOf(
                        "META-INF/LICENSE.md",
                        "META-INF/LICENSE-notice.md",

        )
        )
    }

    tasks.withType<Test>{
        useJUnitPlatform()
    }






    defaultConfig {
        applicationId = "ca.cmput301t05.placeholder"
        minSdk = 24
        targetSdk = 34
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


    // For notifications
    implementation("com.google.code.gson:gson:2.10.1")
//    implementation(fileTree(mapOf("dir" to "C:\\Users\\antho\\AppData\\Local\\Android\\Sdk\\platforms\\android-34", "include" to listOf("*.aar", "*.jar"))))

    // Junit 5
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testCompileOnly ("junit:junit:4.13.2")
    testRuntimeOnly ("org.junit.vintage:junit-vintage-engine:5.10.2")
    androidTestImplementation("org.junit.jupiter:junit-jupiter:5.10.2")

    // Junit 4
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test:rules:1.4.0")

    // Robolectric
    testImplementation ("org.robolectric:robolectric:4.12")
    // Material UI Library
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    testImplementation ("org.mockito:mockito-inline:5.0.0")

    implementation ("com.github.yuriy-budiyev:code-scanner:2.3.0") // The dependency for QR code scanning

    //Implement ViewPager2 Swipe View
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("me.relex:circleindicator:2.1.6")


    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-firestore:24.11.0")
    implementation("com.google.firebase:firebase-storage")


    //Import the Zxing library for the QRcode generator
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")


    //implement the image downloading library
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("androidx.databinding:databinding-runtime:8.3.1")

    // This is a local solution to generate javadoc for the project- do not delete!
//    implementation(fileTree(mapOf("dir" to "C:\\Users\\antho\\AppData\\Local\\Android\\Sdk\\platforms\\android-34", "include" to listOf("*.aar", "*.jar"))))
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")



    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")

    androidTestImplementation("org.mockito:mockito-android:5.11.0")
    androidTestImplementation("androidx.test:monitor:1.6.1")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")

    androidTestImplementation ("androidx.fragment:fragment-testing:1.6.2")

    //this is for image picker for uploading event poster
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //Import the image picker library from Github
    implementation ("com.github.dhaval2404:imagepicker:2.1")

    //for push notifications
    implementation("com.google.firebase:firebase-messaging")

    // Import google play service for geolocation
    implementation ("com.google.android.gms:play-services-location:21.2.0")

    // Import street map view
    implementation ("org.osmdroid:osmdroid-android:6.1.18")

    //for sending http requests to google cloud function for notifications
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    //object -> json
    implementation ("com.google.code.gson:gson:2.10.1")
    // swipe down to refresh
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")


}
