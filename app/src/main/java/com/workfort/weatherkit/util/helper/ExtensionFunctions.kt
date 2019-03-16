package com.workfort.weatherkit.util.helper

import android.net.Uri
import android.view.View
import android.widget.ImageView

/*
*  ****************************************************************************
*  * Created by : Arhan Ashik on 1/2/2019 at 6:56 PM.
*  * Email : ashik.pstu.cse@gmail.com
*  * 
*  * Last edited by : Arhan Ashik on 1/2/2019.
*  * 
*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>  
*  ****************************************************************************
*/

fun ImageView.load(res: Int){
    ImageLoader.load(res, this)
}

fun ImageView.load(url: String?){
    ImageLoader.load(url, this)
}

fun ImageView.load(url: Uri?){
    ImageLoader.load(url, this)
}

fun ImageView.loadWithPlatter(url: String?, view: View?){
    ImageLoader.loadWithPlatter(url, this, view)
}