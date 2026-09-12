package com.openxray.launcher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {

    private lateinit var prefs: PrefsManager
    private lateinit var tvPath: TextView
    private lateinit var btnPlay: Button
    private lateinit var rgGameMode: RadioGroup

    private val folderPickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        if (uri != null) {
            handleSelectedFolder(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        prefs = PrefsManager(this)

        tvPath = findViewById(R.id.tvPath)
        btnPlay = findViewById(R.id.btnPlay)
        rgGameMode = findViewById(R.id.rgGameMode)

        val btnSelectFolder = findViewById<Button>(R.id.btnSelectFolder)
        btnSelectFolder.setOnClickListener {
            folderPickerLauncher.launch(null)
        }

        btnPlay.setOnClickListener {
            launchGame()
        }

        restoreState()
    }

    private fun handleSelectedFolder(uri: Uri) {
        contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )

        val path = uriToPath(uri)
        if (path.isNotEmpty()) {
            prefs.gamePath = path
            tvPath.text = path
            btnPlay.isEnabled = true
        } else {
            Toast.makeText(this, "Dizin okunamadı", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uriToPath(uri: Uri): String {
        val docId = uri.lastPathSegment ?: return ""
        return if (docId.startsWith("primary:")) {
            "/storage/emulated/0/${docId.removePrefix("primary:")}"
        } else {
            "/storage/$docId"
        }
    }

    private fun restoreState() {
        if (prefs.isConfigured()) {
            tvPath.text = prefs.gamePath
            btnPlay.isEnabled = true

            when (prefs.gameMode) {
                PrefsManager.MODE_COC -> rgGameMode.check(R.id.rbCoc)
                PrefsManager.MODE_CS -> rgGameMode.check(R.id.rbCs)
                PrefsManager.MODE_SOC -> rgGameMode.check(R.id.rbSoc)
            }
        } else {
            tvPath.text = "Oyun dizini seçilmedi"
            btnPlay.isEnabled = false
        }
    }

    private fun launchGame() {
        val gameMode = when (rgGameMode.checkedRadioButtonId) {
            R.id.rbCs -> PrefsManager.MODE_CS
            R.id.rbSoc -> PrefsManager.MODE_SOC
            else -> PrefsManager.MODE_COC
        }
        prefs.gameMode = gameMode

        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("gamePath", prefs.gamePath)
            putExtra("gameMode", gameMode)
        }
        startActivity(intent)
    }
}
