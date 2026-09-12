package com.openxray.launcher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {

    private lateinit var prefs: PrefsManager
    private lateinit var tvPath: TextView
    private lateinit var btnPlay: Button

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
            Toast.makeText(this, R.string.dir_read_error, Toast.LENGTH_SHORT).show()
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
        } else {
            tvPath.text = getString(R.string.game_dir_not_selected)
            btnPlay.isEnabled = false
        }
    }

    private fun launchGame() {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("gamePath", prefs.gamePath)
        }
        startActivity(intent)
    }
}
