package com.infrastructure.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.File

fun configureFirebase() {
    val path = System.getenv("FIREBASE_CREDENTIALS_PATH")
        ?: run {
            println("Firebase disabled (no credentials)")
            return
        }

    if (FirebaseApp.getApps().isNotEmpty()) return

    val serviceAccount = File(path).inputStream()

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    FirebaseApp.initializeApp(options)
}
