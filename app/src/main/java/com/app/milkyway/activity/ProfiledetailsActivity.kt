package com.app.milkyway.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.milkyway.R
import com.app.milkyway.databinding.ActivityProfiledetailsBinding
import com.app.milkyway.helper.ApiConfig
import com.app.milkyway.helper.Constant
import com.app.milkyway.helper.Session
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Pattern


class ProfiledetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfiledetailsBinding
    lateinit var activity: Activity
    lateinit var session: Session

    var mobilenumber: String? = null


    private val indianStates = arrayOf(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
        "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka",
        "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
        "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
        "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfiledetailsBinding.inflate(layoutInflater)


        binding.ibBack.setOnClickListener() {
            onBackPressed()
        }

        activity = this
        session = Session(activity)


        com.google.firebase.Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link

                    val extractedString = extractStringAfterLastSlash(deepLink.toString())

                    binding.etReferCode.setText(extractedString + "")
                }

                // Handle the deep link. For example, open the linked
                // content, or apply promotional credit to the user's
                // account.
                // ...
            }
            .addOnFailureListener(this) { e -> Log.w(ApiConfig.TAG, "getDynamicLink:onFailure", e) }







        mobilenumber = session.getData(Constant.MOBILE)


                val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, indianStates)
                binding.autoCompleteTextView.setAdapter(arrayAdapter)


                binding.btnSave.setOnClickListener {
                    if (binding.etName.text.toString().isEmpty()) {
                        binding.etName.error = "Enter Name"
                        return@setOnClickListener
                    } else if (binding.etName.text.toString().length < 4) {
                        binding.etName.error = "Name should be atleast 4 characters"
                        return@setOnClickListener
                    } else if (binding.etEmail.text.toString().isEmpty()) {
                        binding.etEmail.error = "Enter Email"
                        return@setOnClickListener
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text).matches()) {
                        binding.etEmail.error = "Enter a valid Email address"
                        return@setOnClickListener
                    } else if (binding.etAge.text.toString().isEmpty()) {
                        binding.etAge.error = "Enter Age"
                        return@setOnClickListener
                    } else if (binding.etCity.text.toString().isEmpty()) {
                        binding.etCity.error = "Enter City"
                        return@setOnClickListener
                    } else if (!indianStates.contains(binding.autoCompleteTextView.text.toString())) {
                        // Check if the entered state is not in the list of Indian states
                        binding.autoCompleteTextView.error = "Select a valid state"
                        return@setOnClickListener
                    } else if (binding.etReferCode.text.toString().isEmpty()) {
                        binding.etReferCode.error = "Enter Referral Code"
                        return@setOnClickListener
                    } else {
                        register()
                    }
                }

                setContentView(binding.root)



    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
    fun extractStringAfterLastSlash(url: String): String {
        val lastSlashIndex = url.lastIndexOf('/')
        return if (lastSlashIndex != -1 && lastSlashIndex < url.length - 1) {
            url.substring(lastSlashIndex + 1)
        } else {
            "No characters found after the last slash."
        }
    }


    private fun register() {

        val params = HashMap<String, String>()
        params[Constant.MOBILE] = mobilenumber.toString().trim()
        params[Constant.NAME] = binding.etName.text.toString().trim()
        params[Constant.EMAIL] = binding.etEmail.text.toString().trim()
        params[Constant.AGE] = binding.etAge.text.toString().trim()
        params[Constant.CITY] = binding.etCity.text.toString().trim()
        params[Constant.STATE] = binding.autoCompleteTextView.text.toString().trim()
        params[Constant.REFERRED_BY] = binding.etReferCode.text.toString().trim()
        params[Constant.DEVICE_ID] = Constant.getDeviceId(activity)


        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray =
                            jsonObject.getJSONArray(Constant.DATA)

                        session.setBoolean("is_logged_in", true)
                        session.setData(Constant.USER_ID, jsonArray.getJSONObject(0).getString(Constant.ID))
                        session.setData(Constant.NAME, jsonArray.getJSONObject(0).getString(Constant.NAME))
                        session.setData(Constant.MOBILE, jsonArray.getJSONObject(0).getString(Constant.MOBILE))
                        session.setData(Constant.EMAIL, jsonArray.getJSONObject(0).getString(Constant.EMAIL))
                        session.setData(Constant.AGE, jsonArray.getJSONObject(0).getString(Constant.AGE))
                        session.setData(Constant.CITY, jsonArray.getJSONObject(0).getString(Constant.CITY))
                        session.setData(Constant.STATE, jsonArray.getJSONObject(0).getString(Constant.STATE))
                        session.setData(Constant.REFER_CODE,jsonArray.getJSONObject(0).getString(Constant.REFER_CODE))
                        val intent = Intent(activity, SplashScreenActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {

                        Toast.makeText(
                            this,
                            "" + jsonObject.getString(Constant.MESSAGE),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    this,
                    java.lang.String.valueOf(response) + java.lang.String.valueOf(result),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, this, Constant.REGISTER, params, true)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Exit")
        alertDialogBuilder.setMessage("Are you sure you want to exit?")
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
            finishAffinity() // Close the app
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        // Set text color for positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(R.color.primary_color))
        // Set text color for negative button
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.primary_color))
    }




}