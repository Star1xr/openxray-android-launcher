package com.openxray.launcher

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var gamePath: String
        get() = prefs.getString(KEY_GAME_PATH, "") ?: ""
        set(value) = prefs.edit().putString(KEY_GAME_PATH, value).apply()

    var gameMode: String
        get() = prefs.getString(KEY_GAME_MODE, MODE_COC) ?: MODE_COC
        set(value) = prefs.edit().putString(KEY_GAME_MODE, value).apply()

    fun isConfigured(): Boolean {
        return gamePath.isNotEmpty()
    }

    companion object {
        private const val PREFS_NAME = "openxray_prefs"
        private const val KEY_GAME_PATH = "game_path"
        private const val KEY_GAME_MODE = "game_mode"

        const val MODE_COC = "-coc"
        const val MODE_CS = "-cs"
        const val MODE_SOC = "-shoc"
    }
}
