package com.workfort.weatherkit.util.helper

import android.content.Context
import android.widget.Toast

class Toaster(val context: Context) {
    fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(message: Int){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}