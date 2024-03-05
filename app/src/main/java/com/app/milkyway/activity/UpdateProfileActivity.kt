package com.app.milkyway.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import com.app.milkyway.R
import com.app.milkyway.databinding.ActivityUpdateProfileBinding
import com.app.milkyway.helper.ApiConfig
import com.app.milkyway.helper.Constant
import com.app.milkyway.helper.Session
import com.app.milkyway.utils.DialogUtils
import org.json.JSONException
import org.json.JSONObject

class UpdateProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityUpdateProfileBinding
    lateinit var activity: Activity
    lateinit var session: Session


    private val indianStates = arrayOf(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
        "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka",
        "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
        "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
        "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
    activity = this

        session = Session(activity)



        binding.etName.setText(session.getData(Constant.NAME))
        binding.etEmail.setText(session.getData(Constant.EMAIL))
        binding.etAge.setText(session.getData(Constant.AGE))
        binding.etCity.setText(session.getData(Constant.CITY))


        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, indianStates)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.ibBack.setOnClickListener {
          onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            if (binding.etName.text.toString().isEmpty()) {
                binding.etName.error = "Enter Name"
                return@setOnClickListener
            } else if(binding.etName.text.toString().length <4){
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
            } else {
                update()
            }
        }

    setContentView(binding.root)


    }

    private fun update() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val age = binding.etAge.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val state = session.getData(Constant.STATE)

        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        params[Constant.MOBILE] = session.getData(Constant.MOBILE)
        params[Constant.NAME] = name
        params[Constant.EMAIL] = email
        params[Constant.AGE] = age
        params[Constant.CITY] = city
        params[Constant.STATE] = state

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(Constant.DATA)
                        session.setBoolean("is_logged_in", true)
                        Toast.makeText(this, "" + jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()

                        session.setData(
                            Constant.NAME, jsonArray.getJSONObject(0).getString(
                                Constant.NAME))
                        session.setData(
                            Constant.EMAIL, jsonArray.getJSONObject(0).getString(
                                Constant.EMAIL))
                        session.setData(
                            Constant.AGE, jsonArray.getJSONObject(0).getString(
                                Constant.AGE))
                        session.setData(
                            Constant.CITY, jsonArray.getJSONObject(0).getString(
                                Constant.CITY))
                        session.setData(
                            Constant.STATE, jsonArray.getJSONObject(0).getString(
                                Constant.STATE))


                        // reload the page
                        startActivity(Intent(this, UpdateProfileActivity::class.java))
                        finish()


                    } else {

                        DialogUtils.showCustomDialog(this, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
            }
        }, this, Constant.UPDATE_PROFILE, params, true)



    }

}