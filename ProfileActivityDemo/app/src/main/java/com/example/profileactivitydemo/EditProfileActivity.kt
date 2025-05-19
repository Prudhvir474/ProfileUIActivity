package com.example.profileactivitydemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {
    // UI Components
    private var backButton: ImageView? = null
    private var saveButton: TextView? = null
    private var profileImage: ImageView? = null
    private var editName: EditText? = null
    private var editEmail: EditText? = null
    private var editPhone: EditText? = null
    private var memberSinceText: TextView? = null
    private var profileCreatedText: TextView? = null
    private var lastUpdatedText: TextView? = null

    // Data fields
    private var originalName: String? = null
    private var memberSince: String? = null
    private var selectedImageUri: Uri? = null
    private var currentImageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_activity)

        // Initialize views
        initViews()

        // Get data from intent
        getIntentData()

        // Set click listeners
        setupClickListeners()
    }

    private fun initViews() {
        backButton = findViewById(R.id.btn_back)
        saveButton = findViewById(R.id.btn_save)
        profileImage = findViewById(R.id.profile_image)
        editName = findViewById(R.id.edit_name)
        editEmail = findViewById(R.id.edit_email)
        editPhone = findViewById(R.id.edit_phone)
        memberSinceText = findViewById(R.id.tv_member_since)
        profileCreatedText = findViewById(R.id.tv_profile_created)
        lastUpdatedText = findViewById(R.id.tv_last_updated)
    }

    private fun getIntentData() {
        val intent = intent
        if (intent != null) {
            // Get profile data passed from ProfileActivity
            originalName = intent.getStringExtra("PROFILE_NAME")
            memberSince = intent.getStringExtra("MEMBER_SINCE")
            currentImageUri = intent.getStringExtra("CURRENT_IMAGE_URI")

            // Set data to views
            if (originalName != null) {
                editName?.setText(originalName)
            }

            if (memberSince != null) {
                memberSinceText?.text = memberSince
            }

            // Load the current profile image if it exists
            if (currentImageUri != null && currentImageUri!!.isNotEmpty()) {
                try {
                    val uri = Uri.parse(currentImageUri)
                    profileImage?.setImageURI(uri)
                    selectedImageUri = uri // Set the current image as selected
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setupClickListeners() {
        // Back button click - returns to profile activity without saving
        backButton?.setOnClickListener {
            finish() // Close this activity and return to previous
        }

        // Save button click - validates and saves the profile changes
        saveButton?.setOnClickListener { saveProfileChanges() }

        // Profile image click - allows user to select a new profile image
        profileImage?.setOnClickListener { openImagePicker() }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            try {
                // Set the selected image to the ImageView
                profileImage?.setImageURI(selectedImageUri)

                // Persist the content URI permission to be able to access it later
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                contentResolver.takePersistableUriPermission(selectedImageUri!!, takeFlags)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveProfileChanges() {
        // Get values from input fields
        val name = editName?.text.toString().trim()
        val email = editEmail?.text.toString().trim()
        val phone = editPhone?.text.toString().trim()

        // Validate input fields
        if (TextUtils.isEmpty(name)) {
            editName?.error = "Name cannot be empty"
            return
        }

        if (!TextUtils.isEmpty(email) && !isValidEmail(email)) {
            editEmail?.error = "Enter a valid email address"
            return
        }

        if (!TextUtils.isEmpty(phone) && !isValidPhone(phone)) {
            editPhone?.error = "Enter a valid phone number"
            return
        }

        // Update last updated timestamp
        val currentDateTime = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault()).format(Date())
        lastUpdatedText?.text = currentDateTime

        // Create intent to return updated data to ProfileActivity
        val resultIntent = Intent()
        resultIntent.putExtra("UPDATED_NAME", name)
        resultIntent.putExtra("UPDATED_EMAIL", email)
        resultIntent.putExtra("UPDATED_PHONE", phone)

        // If image was selected, include its URI in the result
        if (selectedImageUri != null) {
            resultIntent.putExtra("UPDATED_IMAGE_URI", selectedImageUri.toString())
        }

        // Set result and finish
        setResult(RESULT_OK, resultIntent)

        // Show success message
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()

        // Close this activity and return to profile
        finish()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhone(phone: String): Boolean {
        // This is a simple validation - you might want more complex validation
        return phone.length >= 10
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
