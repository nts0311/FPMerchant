package com.sonnt.fpmerchant.data.local

import android.content.Context
import com.sonnt.fpmerchant.di.AppModule

class SharedPreferencesApi {
    val PREF_NAME = "SharedPreferences"

    private val sharedPreferences =
        AppModule.provideApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val gson = AppModule.provideGson()

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, clazz: Class<T>): T {
        return when (clazz) {
            String::class.java -> sharedPreferences.getString(key, "") as T
            Boolean::class.java -> java.lang.Boolean.valueOf(sharedPreferences.getBoolean(key, false)) as T
            Float::class.java -> java.lang.Float.valueOf(sharedPreferences.getFloat(key, 0f)) as T
            Int::class.java -> Integer.valueOf(sharedPreferences.getInt(key, 0)) as T
            Long::class.java -> java.lang.Long.valueOf(sharedPreferences.getLong(key, 0)) as T
            else -> gson.fromJson(sharedPreferences.getString(key, ""), clazz)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, clazz: Class<T>, default: T): T {
        return when (clazz) {
            String::class.java -> sharedPreferences.getString(key, default as String) as T
            Boolean::class.java -> java.lang.Boolean.valueOf(sharedPreferences.getBoolean(key, default as Boolean)) as T
            Float::class.java -> java.lang.Float.valueOf(sharedPreferences.getFloat(key, default as Float)) as T
            Int::class.java -> Integer.valueOf(sharedPreferences.getInt(key, default as Int)) as T
            Long::class.java -> java.lang.Long.valueOf(sharedPreferences.getLong(key, default as Long)) as T
            else -> gson.fromJson(sharedPreferences.getString(key, default as String), clazz)
        }
    }

    fun <T> put(key: String, data: T) {
        val editor = sharedPreferences.edit()
        when (data) {
            is String -> editor.putString(key, data as String)
            is Boolean -> editor.putBoolean(key, data as Boolean)
            is Float -> editor.putFloat(key, data as Float)
            is Int -> editor.putInt(key, data as Int)
            is Long -> editor.putLong(key, data as Long)
            else -> editor.putString(key, gson.toJson(data))
        }
        editor.apply()
    }

    fun remove(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun delete(context: Context, name: String?) {
        var name = name
        if (name == null || name == "") {
            name = PREF_NAME
        }
        val preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
}
