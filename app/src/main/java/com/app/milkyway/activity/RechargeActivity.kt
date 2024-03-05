package com.app.milkyway.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.milkyway.R
import com.app.milkyway.databinding.ActivityRechargeBinding

class RechargeActivity : AppCompatActivity() {

    lateinit var binding: ActivityRechargeBinding
    private lateinit var activity: Activity
    private lateinit var session: com.app.milkyway.helper.Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRechargeBinding.inflate(layoutInflater)
        activity = this
        session = com.app.milkyway.helper.Session(activity)


        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.rlAmont1.setOnClickListener {
            binding.rlAmont1.setBackgroundResource(R.drawable.bg_rounded_blue)
            binding.rlAmont2.setBackgroundResource(R.drawable.edittext)
            binding.rlAmont3.setBackgroundResource(R.drawable.edittext)

            binding.tvAmount1.setTextColor(resources.getColor(R.color.white))
            binding.tvAmount2.setTextColor(resources.getColor(R.color.black))
            binding.tvAmount3.setTextColor(resources.getColor(R.color.black))

            binding.etAmount.setText(binding.tvAmount1.text.toString())



        }

        binding.rlAmont2.setOnClickListener {
            binding.rlAmont1.setBackgroundResource(R.drawable.edittext)
            binding.rlAmont2.setBackgroundResource(R.drawable.bg_rounded_blue)
            binding.rlAmont3.setBackgroundResource(R.drawable.edittext)

            binding.tvAmount1.setTextColor(resources.getColor(R.color.black))
            binding.tvAmount2.setTextColor(resources.getColor(R.color.white))
            binding.tvAmount3.setTextColor(resources.getColor(R.color.black))

            binding.etAmount.setText(binding.tvAmount2.text.toString())

        }



        binding.rlAmont3.setOnClickListener {
            binding.rlAmont1.setBackgroundResource(R.drawable.edittext)
            binding.rlAmont2.setBackgroundResource(R.drawable.edittext)
            binding.rlAmont3.setBackgroundResource(R.drawable.bg_rounded_blue)

            binding.tvAmount1.setTextColor(resources.getColor(R.color.black))
            binding.tvAmount2.setTextColor(resources.getColor(R.color.black))
            binding.tvAmount3.setTextColor(resources.getColor(R.color.white))

            binding.etAmount.setText(binding.tvAmount3.text.toString())

        }



        setContentView(binding.root)

    }
}