package com.wingsupfinal

import android.app.Application
import android.content.Context

class AppWingsUp: Application() {
    companion object{
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context =applicationContext


    }

}