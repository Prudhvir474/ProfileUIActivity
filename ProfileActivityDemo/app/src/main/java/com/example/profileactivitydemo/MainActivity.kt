package com.example.profileactivitydemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupStatusBar()
        setupClickListener()
    }

    private fun setupStatusBar() {
        // Set status bar color to dark
        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_background)
    }

    private fun setupClickListener() {
        findViewById<Button>(R.id.btn_open_profile).setOnClickListener {
            val intent = Intent(this, ProfileActivityDemo::class.java)
            startActivity(intent)
        }
    }
}