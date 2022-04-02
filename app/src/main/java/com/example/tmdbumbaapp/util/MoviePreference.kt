package com.example.tmdbumbaapp.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class MoviePreference() {


    companion object {
        val MY_PREF = "my_pref"

        lateinit var myPreference: SharedPreferences

        fun initPreference (activity: Activity){
            myPreference = activity.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        }


    }
}