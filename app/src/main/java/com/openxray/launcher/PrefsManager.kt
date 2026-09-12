package com.openxray.launcher

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var gamePath: String
        get() = prefs.getString(KEY_GAME_PATH, "") ?: ""
        set(value) = prefs.edit().putString(KEY_GAME_PATH, value).apply()

    fun isConfigured(): Boolean {
        return gamePath.isNotEmpty()
    }

    companion object {
        private const val PREFS_NAME = "openxray_prefs"
        private const val KEY_GAME_PATH = "game_path"

        const val MODE_COC = "-coc"
    }
}
