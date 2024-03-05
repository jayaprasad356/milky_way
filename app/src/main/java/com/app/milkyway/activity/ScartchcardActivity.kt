package com.app.milkyway.activity

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anupkumarpanwar.scratchview.ScratchView
import com.anupkumarpanwar.scratchview.ScratchView.IRevealListener
import com.app.milkyway.databinding.ActivityScartchcardBinding
import com.app.milkyway.helper.ApiConfig
import com.app.milkyway.helper.Constant
import com.app.milkyway.utils.DialogUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class ScartchcardActivity : AppCompatActivity() {

    lateinit var binding: ActivityScartchcardBinding
    lateinit var activity: Activity
    lateinit var session: com.app.milkyway.helper.Session

    var dialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScartchcardBinding.inflate(layoutInflater)
        activity = this
        session = com.app.milkyway.helper.Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
            finish()
        }



        dialog = Dialog(this)
        binding.scratchView.setRevealListener(object : IRevealListener {
            override fun onRevealed(scratchView: ScratchView) {
                Toast.makeText(
                    this@ScartchcardActivity,
                    "Revealed!", Toast.LENGTH_SHORT
                ).show()

                // overlay is removed when the scratch is revealed

                scratchView.visibility = View.GONE

            }

            override fun onRevealPercentChangedListener(scratchView: ScratchView, percent: Float) {
                Log.d("Revealed", percent.toString())
            }
        })

        apicall()

        return setContentView(binding!!.root)
    }

    private fun apicall() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray =
                            jsonObject.getJSONArray(Constant.DATA)


                        binding.tvAmount.text = jsonArray.getJSONObject(0).getString("amount")


                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.SCRATCH_CARD, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }

}

