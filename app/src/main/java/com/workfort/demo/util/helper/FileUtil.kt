package com.workfort.demo.util.helper

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.workfort.demo.DemoApp
import java.io.File
import java.io.FileOutputStream
import java.util.*

class FileUtil {
    fun saveBitmap(bitmap: Bitmap): Uri {
        val id = Random().nextInt(10000)
        val file = File(DemoApp.getApplicationContext().cacheDir, "WW_$id.JPEG")

        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()

        return Uri.fromFile(file)
    }

    fun createEmptyFile(fileName: String): File {
        return File(DemoApp.getApplicationContext().cacheDir, fileName)
    }

    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = DemoApp.getApplicationContext()
            .contentResolver.query(uri, projection, null, null, null) ?: return null
        cursor.moveToFirst()
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)).also {
            cursor.close()
            return it
        }
    }

    fun getFileType(uri: Uri): String? {
        return DemoApp.getApplicationContext().contentResolver?.getType(uri)
    }
}
