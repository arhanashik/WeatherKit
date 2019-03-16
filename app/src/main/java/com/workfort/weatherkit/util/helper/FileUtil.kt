package com.workfort.weatherkit.util.helper

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.workfort.weatherkit.WeatherKitApp
import java.io.File
import java.io.FileOutputStream
import java.util.*

class FileUtil {
    fun saveBitmap(bitmap: Bitmap): Uri {
        val id = Random().nextInt(10000)
        val file = File(WeatherKitApp.getApplicationContext().cacheDir, "WW_$id.JPEG")

        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()

        return Uri.fromFile(file)
    }

    fun createEmptyFile(fileName: String): File {
        return File(WeatherKitApp.getApplicationContext().cacheDir, fileName)
    }

    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = WeatherKitApp.getApplicationContext()
            .contentResolver.query(uri, projection, null, null, null) ?: return null
        cursor.moveToFirst()
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)).also {
            cursor.close()
            return it
        }
    }

    fun getFileType(uri: Uri): String? {
        return WeatherKitApp.getApplicationContext().contentResolver?.getType(uri)
    }
}
