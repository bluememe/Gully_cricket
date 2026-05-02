package com.example.gullycricket

import android.app.Application
import com.example.gullycricket.core.database.AppDatabase

class GullyCricketApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    
    override fun onCreate() {
        super.onCreate()
    }
}
