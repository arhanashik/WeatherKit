package com.workfort.weatherkit.util.helper

//import android.app.Activity
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.Canvas
//import android.graphics.PorterDuff
//import android.graphics.drawable.BitmapDrawable
//import android.graphics.drawable.Drawable
//import android.graphics.drawable.GradientDrawable
//import android.net.Uri
//import androidx.core.content.ContextCompat
//import com.workfort.demo.R
//import com.yalantis.ucrop.UCrop
//import com.yalantis.ucrop.model.AspectRatio
//import timber.log.Timber
//import java.io.IOException
//import java.util.*
//
//class ImageUtil {
//
//    fun cropImage(activity: Activity, uri: Uri) {
//        try {
//            val id = Random().nextInt(10000)
//            val desImg = FileUtil().createEmptyFile("WW_$id.JPEG")
//            if (desImg.exists()) {
//                if (desImg.delete()) {
//                    Timber.e("Old cache cleared")
//                }
//            }
//
//            val displayParams = AndroidUtil().getDisplayParams(activity)
//
//            val options = UCrop.Options()
//            options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
//            options.setCompressionQuality(100)
//            options.setFreeStyleCropEnabled(false)
//            val ratio = AspectRatio("Fixed", displayParams.width.toFloat(), displayParams.height.toFloat())
//            options.setAspectRatioOptions(0, ratio)
//
//            options.setToolbarTitle(activity.getString(R.string.title_activity_edit_wallpaper))
//
//            // Color palette
//            options.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary))
//            options.setToolbarWidgetColor(ContextCompat.getColor(activity, android.R.color.white))
//            options.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
//            options.setActiveWidgetColor(ContextCompat.getColor(activity, R.color.colorPrimary))
//
//            UCrop.of(uri, Uri.fromFile(desImg))
//                .withOptions(options)
//                .start(activity)
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//    }
//
//    fun getBackgroundGradient(color1: Int, color2: Int): GradientDrawable {
//        return GradientDrawable(
//            GradientDrawable.Orientation.TOP_BOTTOM,
//            intArrayOf(color1, color2, color1)
//        )
//    }
//
//    fun adjustOpacity(bitmap: Bitmap, opacity: Int): Bitmap {
//        val mutableBitmap = if (bitmap.isMutable)
//            bitmap
//        else
//            bitmap.copy(Bitmap.Config.ARGB_8888, true)
//        val canvas = Canvas(mutableBitmap)
//        val colour = opacity and 0xFF shl 24
//        canvas.drawColor(colour, PorterDuff.Mode.DST_IN)
//        return mutableBitmap
//    }
//
//    fun drawableToBitmap(drawable: Drawable): Bitmap {
//        if (drawable is BitmapDrawable) {
//            return drawable.bitmap
//        }
//
//        var width = drawable.intrinsicWidth
//        width = if (width > 0) width else 1
//        var height = drawable.intrinsicHeight
//        height = if (height > 0) height else 1
//
//        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        drawable.setBounds(0, 0, canvas.width, canvas.height)
//        drawable.draw(canvas)
//
//        return bitmap
//    }
//
//    fun uriToBitmap(context: Context, selectedFileUri: Uri): Bitmap? {
//        var bitmap: Bitmap? = null
//        try {
//            val parcelFileDescriptor = context.contentResolver
//                .openFileDescriptor(selectedFileUri, "r")
//            val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
//            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
//
//            parcelFileDescriptor.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        return bitmap
//    }
//}