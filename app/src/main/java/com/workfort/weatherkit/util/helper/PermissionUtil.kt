package com.workfort.weatherkit.util.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

import java.util.ArrayList

import androidx.fragment.app.Fragment

class PermissionUtil private constructor() {

    fun request(activity: Activity, vararg permissions: String): Boolean {
        return request(activity, REQUEST_CODE_PERMISSION_DEFAULT, *permissions)
    }

    fun request(activity: Activity?, requestCode: Int, vararg permissions: String): Boolean {
        if (activity == null) return false

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }

        val finalArgs = ArrayList<String>()
        for (permission in permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                finalArgs.add(permission)
            }
        }

        if (finalArgs.isEmpty()) {
            return true
        }

        activity.requestPermissions(finalArgs.toTypedArray(), requestCode)

        return false
    }

    fun request(fragment: Fragment, vararg permissions: String): Boolean {
        return request(fragment, REQUEST_CODE_PERMISSION_DEFAULT, *permissions)
    }

    fun request(fragment: Fragment?, requestCode: Int, vararg permissions: String): Boolean {
        if (fragment == null || fragment.context == null) {
            return false
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }

        val finalArgs = ArrayList<String>()
        for (aStr in permissions) {
            if (fragment.context!!.checkSelfPermission(aStr) != PackageManager.PERMISSION_GRANTED) {
                finalArgs.add(aStr)
            }
        }

        if (finalArgs.isEmpty()) {
            return true
        }

        fragment.requestPermissions(finalArgs.toTypedArray(), requestCode)

        return false
    }

    fun isAllowed(context: Context?, str: String): Boolean {
        if (context == null) return false

        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            true
        } else context.checkSelfPermission(str) == PackageManager.PERMISSION_GRANTED

    }

    companion object {
        val REQUEST_CODE_PERMISSION_DEFAULT = 1
        val REQUEST_CODE_STORAGE = 2
        private var invokePermission: PermissionUtil? = null

        fun on(): PermissionUtil {
            if (invokePermission == null) {
                invokePermission = PermissionUtil()
            }

            return invokePermission as PermissionUtil
        }
    }
}