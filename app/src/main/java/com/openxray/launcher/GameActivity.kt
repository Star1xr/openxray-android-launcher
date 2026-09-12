package com.openxray.launcher

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import org.libsdl.app.SDLActivity

class GameActivity : SDLActivity() {

    private var gamePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gamePath = intent.getStringExtra("gamePath") ?: ""

        hideSystemUI()
    }

    override fun getArguments(): Array<String> {
        return arrayOf(
            "-fsltx", "$gamePath/fsgame.ltx",
            PrefsManager.MODE_COC
        )
    }

    override fun getLibraries(): Array<String> {
        return arrayOf("SDL2", "xr_3da")
    }

    override fun onBackPressed() {
    }

    private fun hideSystemUI() {
        window.insetsController?.let { controller ->
            controller.hide(WindowInsets.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }
}
