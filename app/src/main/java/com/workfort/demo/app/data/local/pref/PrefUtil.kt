package com.workfort.demo.app.data.local.pref

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.workfort.demo.DemoApp

object PrefUtil {
    val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(
        DemoApp.getApplicationContext()
    )

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    private operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String -> edit {
                if(value == PrefProp.ACTION_DELETE) it.remove(key)
                else it.putString(key, value)
            }
            is Int -> edit { it.putInt(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Long -> edit { it.putLong(key, value) }
//            is UserEntity -> edit { it.putString(key, GsonUtil().toJson(value)) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
//            UserEntity::class -> GsonUtil().fromJson(
//                getString(key, defaultValue as? String)!!, UserEntity::class.java
//            ) as T?

            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * This method writes data in SharedPreference
     *
     * @param key key to write
     * @param value value to be written
     * */
    fun set(key: String, value: Any) {
        preferences[key] = value
    }

    /**
     * This method reads data from SharedPreference
     *
     * @param key key to fetch data
     * @param defaultValue default value to be returned if there is any error
     * */
    inline fun <reified T : Any> get(key: String, defaultValue: T? = null): T? {
        return preferences[key, defaultValue]
    }

    /**
     * This method provides a state if the SharedPreference contains the key
     *
     * @param key key to be searched
     * @return [Boolean] state
     * */
    operator fun contains(key: String): Boolean {
        return preferences.contains(key)
    }
}