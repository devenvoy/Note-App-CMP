package com.devansh.noteapp

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

class NoteApp : Application() {

    companion object{
        lateinit var AppContext : Context
    }

    override fun onCreate() {
        super.onCreate()
        AppContext = this
        FirebaseApp.initializeApp(this)
    }
}