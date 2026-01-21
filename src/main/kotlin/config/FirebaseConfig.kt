package com.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.File

fun configureFirebase() {
    if (FirebaseApp.getApps().isNotEmpty()) return

    val serviceAccountPath = System.getenv("FIREBASE_CREDENTIALS_PATH")
        ?: error("FIREBASE_CREDENTIALS_PATH not set")

    val serviceAccount = File(serviceAccountPath).inputStream()

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    FirebaseApp.initializeApp(options)
}
