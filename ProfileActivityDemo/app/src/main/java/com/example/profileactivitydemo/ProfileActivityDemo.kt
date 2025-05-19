package com.example.profileactivitydemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivityDemo : AppCompatActivity() {
    private var profileImage: ImageView? = null
    private var profileName: TextView? = null
    private var memberSince: TextView? = null
    private var editProfileButton: ImageView? = null
    private var backButton: ImageView? = null
    private var supportButton: LinearLayout? = null   // <-- FIXED TYPE

    // Store profile image URI
    private var profileImageUri: Uri? = null

    // Layouts
    private var layoutCreditScore: LinearLayout? = null
    private var layoutLifetimeCashback: LinearLayout? = null
    private var layoutBankBalance: LinearLayout? = null
    private var layoutCashbackBalance: LinearLayout? = null
    private var layoutCoins: LinearLayout? = null
    private var layoutWinReward: LinearLayout? = null
    private var layoutAllTransactions: LinearLayout? = null
    private var layoutCredGarage: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        initViews()

        // Set click listeners
        setClickListeners()
    }

    private fun initViews() {
        // Profile section
        profileImage = findViewById(R.id.profile_image)
        profileName = findViewById(R.id.tv_profile_name)
        memberSince = findViewById(R.id.tv_member_since)
        editProfileButton = findViewById(R.id.btn_edit_profile)

        // Header
        backButton = findViewById(R.id.btn_back)
        supportButton = findViewById(R.id.btn_support)   // <-- FIXED TYPE

        // Layouts
        layoutCredGarage = findViewById(R.id.layout_cred_garage)
        layoutCreditScore = findViewById(R.id.layout_credit_score)
        layoutLifetimeCashback = findViewById(R.id.layout_lifetime_cashback)
        layoutBankBalance = findViewById(R.id.layout_bank_balance)
        layoutCashbackBalance = findViewById(R.id.layout_cashback_balance)
        layoutCoins = findViewById(R.id.layout_coins)
        layoutWinReward = findViewById(R.id.layout_win_reward)
        layoutAllTransactions = findViewById(R.id.layout_all_transactions)
    }

    private fun setClickListeners() {
        // Clicking on profile image opens edit profile
        profileImage?.setOnClickListener { openEditProfileActivity() }

        // Clicking on profile name opens edit profile
        profileName?.setOnClickListener { openEditProfileActivity() }

        // Edit profile button
        editProfileButton?.setOnClickListener { openEditProfileActivity() }

        // Back button
        backButton?.setOnClickListener {
            finish() // Close this activity
        }

        // Support button
        supportButton?.setOnClickListener {
            // Handle support button click
            // You can implement support functionality here
        }

        // Section click listeners
        layoutCredGarage?.setOnClickListener {
            // Handle CRED garage click
        }

        layoutCreditScore?.setOnClickListener {
            // Handle credit score click
        }

        layoutLifetimeCashback?.setOnClickListener {
            // Handle lifetime cashback click
        }

        layoutBankBalance?.setOnClickListener {
            // Handle bank balance click
        }

        layoutCashbackBalance?.setOnClickListener {
            // Handle cashback balance click
        }

        layoutCoins?.setOnClickListener {
            // Handle coins click
        }

        layoutWinReward?.setOnClickListener {
            // Handle win reward click
        }

        layoutAllTransactions?.setOnClickListener {
            // Launch transaction activity
            openTransactionActivity()
        }
    }

    private fun openEditProfileActivity() {
        val intent = Intent(this@ProfileActivityDemo, EditProfileActivity::class.java)

        // Pass current profile data to edit activity
        intent.putExtra("PROFILE_NAME", profileName?.text.toString())
        intent.putExtra("MEMBER_SINCE", memberSince?.text.toString().replace("member since ", ""))

        // Pass current profile image URI if available
        if (profileImageUri != null) {
            intent.putExtra("CURRENT_IMAGE_URI", profileImageUri.toString())
        }

        // Start activity for result to get updated data when user returns
        startActivityForResult(intent, 100)
    }

    private fun openTransactionActivity() {
        val intent = Intent(this@ProfileActivityDemo, TransactionsActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check if result is from edit profile activity
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // Update profile data with returned values
            val updatedName = data.getStringExtra("UPDATED_NAME")
            if (!updatedName.isNullOrEmpty()) {
                profileName?.text = updatedName
            }

            // Handle the updated image URI
            val updatedImageUri = data.getStringExtra("UPDATED_IMAGE_URI")
            if (!updatedImageUri.isNullOrEmpty()) {
                try {
                    profileImageUri = Uri.parse(updatedImageUri)
                    // Set the image to the ImageView
                    profileImage?.setImageURI(null) // Clear the ImageView first
                    profileImage?.setImageURI(profileImageUri) // Set the new image
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // If there are other profile fields to update, add them here
        }
    }

    override fun onResume() {
        super.onResume()
        // You can refresh profile data here if needed
    }
}
