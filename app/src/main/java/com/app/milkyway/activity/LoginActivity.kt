package com.app.milkyway.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.milkyway.R
import com.app.milkyway.databinding.ActivityLoginBinding
import com.app.milkyway.helper.Constant

class LoginActivity : AppCompatActivity() {


    lateinit var binding: ActivityLoginBinding

    private lateinit var activity: Activity
    private lateinit var session: com.app.milkyway.helper.Session


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        activity = this
        session = com.app.milkyway.helper.Session(activity)


//        binding.btnLogin.setOnClickListener {
//            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, OtpActivity::class.java))
//        }


        setupViews()


        setContentView(binding.root)


    }

    private fun setupViews() {
        binding.btnLogin.setOnClickListener {
            if (binding.etPhoneNumber.text.toString().isEmpty()) {
                binding.etPhoneNumber.error = "Please enter mobile number"
                binding.etPhoneNumber.requestFocus()
            } else if (binding.etPhoneNumber.text.toString().length != 10) {
                binding.etPhoneNumber.error = "Please enter valid mobile number"
                binding.etPhoneNumber.requestFocus()
            } else {
                session.setData(Constant.MOBILE, binding.etPhoneNumber.text.toString())
                val intent = Intent(activity, OtpActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }






}