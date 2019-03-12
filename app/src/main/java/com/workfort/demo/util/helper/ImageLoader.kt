package com.workfort.demo.util.helper
//
//import android.graphics.Bitmap
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.text.TextUtils
//import android.view.View
//import android.widget.ImageView
//import android.widget.ProgressBar
//import androidx.annotation.Nullable
//import androidx.core.content.ContextCompat
//import androidx.palette.graphics.Palette
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.DataSource
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.engine.GlideException
//import com.bumptech.glide.request.RequestListener
//import com.bumptech.glide.request.RequestOptions
//import com.bumptech.glide.request.target.Target
//import com.workfort.wallpaperworld.WallpaperWorldApp
//import com.workfort.wallpaperworld.R
//
///*
//*  ****************************************************************************
//*  * Created by : Arhan Ashik on 12/14/2018 at 4:50 PM.
//*  * Email : ashik.pstu.cse@gmail.com
//*  *
//*  * Last edited by : Arhan Ashik on 12/14/2018.
//*  *
//*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
//*  ****************************************************************************
//*/
//
//class ImageLoader {
//    companion object {
//        fun load(location: Int, imageView: ImageView) {
//            val requestOptions = RequestOptions()
//                .timeout(20000)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .placeholder(R.drawable.ic_logo)
//                .error(R.drawable.ic_search_broken_link)
//
//            Glide.with(imageView.context)
//                .load(if (location > 0) location else R.drawable.ic_search_broken_link)
//                .apply(requestOptions)
//                .into(imageView)
//        }
//
//        fun load(location: String?, imageView: ImageView) {
//            val requestOptions = RequestOptions()
//                .timeout(20000)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.ic_logo)
//                .error(R.drawable.ic_search_broken_link)
//
//            Glide.with(imageView.context)
//                .load(if (TextUtils.isEmpty(location)) R.drawable.ic_search_broken_link else location)
//                .apply(requestOptions)
//                .into(imageView)
//        }
//
//        fun load(location: Uri?, imageView: ImageView) {
//            val requestOptions = RequestOptions()
//                .timeout(5000)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .placeholder(R.drawable.ic_logo)
//                .error(R.drawable.ic_search_broken_link)
//
//            Glide.with(imageView.context)
//                .load(location ?: R.drawable.ic_search_broken_link)
//                .apply(requestOptions)
//                .into(imageView)
//        }
//
//        fun load(location: String, imageView: ImageView, placeholder: Int) {
//            val requestOptions = RequestOptions()
//                .timeout(20000)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(placeholder)
//                .error(placeholder)
//
//            Glide.with(imageView.context)
//                .load(if (TextUtils.isEmpty(location)) placeholder else location)
//                .apply(requestOptions)
//                .into(imageView)
//        }
//
//        fun load(location: String?, imageView: ImageView, progressBar: ProgressBar) {
//            progressBar.visibility = View.VISIBLE
//
//            val options = RequestOptions()
//                .timeout(20000)
//                .dontTransform()
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .placeholder(R.drawable.ic_logo)
//                .error(R.drawable.ic_search_broken_link)
//
//            Glide.with(imageView.context)
//                .load(if (TextUtils.isEmpty(location)) R.drawable.ic_search_broken_link else location)
//                .apply(options)
//                .listener(object : RequestListener<Drawable> {
//                    override fun onLoadFailed(
//                        @Nullable e: GlideException?, model: Any, target: Target<Drawable>,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        progressBar.visibility = View.GONE
//                        return false
//                    }
//
//                    override fun onResourceReady(
//                        resource: Drawable, model: Any, target: Target<Drawable>,
//                        dataSource: DataSource, isFirstResource: Boolean
//                    ): Boolean {
//                        progressBar.visibility = View.GONE
//                        return false
//                    }
//                })
//                .apply(options)
//                .into(imageView)
//        }
//
//        fun loadWithPlatter(url: String?, imageView: ImageView, view: View?) {
//            val requestOptions = RequestOptions()
//                .timeout(20000)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.ic_logo)
//                .error(R.drawable.ic_search_broken_link)
//
//            Glide.with(imageView.context)
//                .asBitmap()
//                .load(if (TextUtils.isEmpty(url)) R.drawable.ic_search_broken_link else url)
//                .apply(requestOptions)
//                .listener(object : RequestListener<Bitmap> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: com.bumptech.glide.request.target.Target<Bitmap>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        return false
//                    }
//
//                    override fun onResourceReady(
//                        resource: Bitmap?,
//                        model: Any,
//                        target: com.bumptech.glide.request.target.Target<Bitmap>?,
//                        dataSource: DataSource,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        if (resource != null) {
//                            val p = Palette.from(resource).generate()
//                            val colorDefault = ContextCompat.getColor(
//                                WallpaperWorldApp.getApplicationContext(), R.color.colorPrimary)
//                            var color = p.getVibrantColor(colorDefault)
//                            if(color == colorDefault) color = p.getMutedColor(colorDefault)
//
//                            view!!.setBackgroundColor(color)
//                        }
//                        return false
//                    }
//                })
//                .into(imageView)
//        }
//    }
//}