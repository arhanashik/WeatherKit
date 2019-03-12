package com.workfort.demo.util.helper

import android.content.Context
import androidx.appcompat.widget.PopupMenu
import android.content.pm.PackageManager
import android.util.Base64
import com.workfort.demo.DemoApp
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class AndroidUtil {
    data class DisplayParams(val width: Int, val height: Int)

    fun getDisplayParams(context: Context): DisplayParams {
        val config = context.resources.configuration

        return DisplayParams(config.screenWidthDp, config.screenHeightDp)
    }

    fun setForceShowIcon(popupMenu: PopupMenu) {
        try {
            val fields = popupMenu::class.java.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field.get(popupMenu)
                    val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                    val setForceIcons =
                        classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType!!)
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    fun getKeyHash() {
        try {
            val info = DemoApp.getApplicationContext().packageManager
                .getPackageInfo(DemoApp.getApplicationContext().packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Timber.e("KeyHash:%s", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            //something
            Timber.e("KeyHash:%s", e.message)
        } catch (ex: NoSuchAlgorithmException) {
            //something
            Timber.e("KeyHash:%s", ex.message)
        }
    }
}