package com.devansh.noteapp.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.net.toUri


fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true // App is installed
    } catch (e: PackageManager.NameNotFoundException) {
        false // App is not installed
    }
}

fun Context.openApp(packageName: String) {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    if (intent != null) {
        startActivity(intent)
    } else {
        // If App B is not installed, redirect to Play Store
        try {
            val playStoreIntent = Intent(Intent.ACTION_VIEW,
                "market://details?id=$packageName".toUri())
            playStoreIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(playStoreIntent)
        } catch (e: Exception) {
            // Handle Play Store redirection failure
            Toast.makeText(this, "Unable to open Play Store", Toast.LENGTH_SHORT).show()
        }
    }
}

fun Context.shareApp() {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(
        Intent.EXTRA_TEXT,
        "https://play.google.com/store/apps/details?id=$packageName"
    )
    sendIntent.type = "text/plain"
    startActivity(sendIntent)
}

fun Context.ratingApp() {
    val url1 = "https://play.google.com/store/apps/details?id=$packageName"
    val i1 = Intent(Intent.ACTION_VIEW)
    i1.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    i1.data = url1.toUri()
    startActivity(i1)
}

fun Context.moreApp() {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/developer?id=UniqueApp Technologies".toUri()
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun Context.feedBackApp() {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        val recipients = arrayOf("mobileappxperts3@gmail.com")
        intent.putExtra(Intent.EXTRA_EMAIL, recipients)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
        intent.type = "text/html"
        intent.setPackage("com.google.android.gm")
        startActivity(Intent.createChooser(intent, "Send mail"))
    } catch (e: java.lang.Exception) {
        Toast.makeText(this, "Not installed Gmail", Toast.LENGTH_SHORT).show()
    }
}
