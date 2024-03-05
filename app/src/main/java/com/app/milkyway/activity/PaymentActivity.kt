package com.app.milkyway.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.milkyway.R
import com.app.milkyway.adapter.RechargeHistoryAdapter
import com.app.milkyway.databinding.ActivityPaymentBinding
import com.app.milkyway.helper.ApiConfig
import com.app.milkyway.helper.Constant
import com.app.milkyway.model.Recharge
import com.app.milkyway.utils.DialogUtils
import com.canhub.cropper.CropImage
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File


class PaymentActivity : AppCompatActivity() {
    lateinit var binding: ActivityPaymentBinding
    lateinit var activity: Activity
    lateinit var session: com.app.milkyway.helper.Session

    var filePath1: String? = null
    var imageUri: Uri? = null

    private val REQUEST_IMAGE_GALLERY = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        activity = this
        session = com.app.milkyway.helper.Session(activity)

        binding.cvImage.setOnClickListener {
            pickImageFromGallery()

        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }


        val animation: Animation =
            AnimationUtils.loadAnimation(applicationContext, com.app.milkyway.R.anim.blink_anim)
        binding.tvtiming.startAnimation(animation)


        binding.btnUpload.setOnClickListener {
            if (filePath1 != null) {
                recharge()
            } else {
                Toast.makeText(activity, "Please select image", Toast.LENGTH_SHORT).show()
            }
        }


        binding.tvOpen.setOnClickListener {
            // open QrActivity
            val intent = Intent(activity, QrActivity::class.java)
            startActivity(intent)

        }

        transaction()

        apicall()

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvHistory.layoutManager = linearLayoutManager

        setContentView(binding.root)

    }





    private fun transaction() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        com.app.milkyway.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(com.app.milkyway.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.app.milkyway.helper.Constant.DATA)
                        val g = Gson()
                        val recharge: java.util.ArrayList<Recharge> = java.util.ArrayList<Recharge>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Recharge = g.fromJson(jsonObject1.toString(), Recharge::class.java)
                                recharge.add(group)
                            } else {
                                break
                            }
                        }

                        val adapter = RechargeHistoryAdapter(activity, recharge)
                        binding.rvHistory.adapter = adapter


                    } else {

                       binding.rlHistory.visibility = View.GONE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
            }
        }, this, com.app.milkyway.helper.Constant.RECHARGE_HISTORY, params, true)


    }

    private fun pickImageFromGallery() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_GALLERY
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY) {
                imageUri = data?.data
                CropImage.activity(imageUri)
                    .start(activity)
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result: CropImage.ActivityResult = CropImage.getActivityResult(data)!!
                filePath1 = result.getUriFilePath(activity, true)
                val imgFile: File = File(filePath1)
                if (imgFile.exists()) {
                    binding.btnUpload.isEnabled = true // Corrected method name
                    val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    binding.ivImage.setImageBitmap(myBitmap)
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun recharge() {
        val params: MutableMap<String, String> = HashMap()
        params[com.app.milkyway.helper.Constant.USER_ID] = session.getData(com.app.milkyway.helper.Constant.USER_ID)
        val FileParams: MutableMap<String, String> = HashMap()
        FileParams[com.app.milkyway.helper.Constant.IMAGE] = filePath1!!
        com.app.milkyway.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.milkyway.helper.Constant.SUCCESS)) {



                        showCustomDialog()

                    } else {
                        DialogUtils.showCustomDialog(this, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, activity, com.app.milkyway.helper.Constant.RECHARGE_URL, params, FileParams)
    }


    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView: View = layoutInflater.inflate(R.layout.custom_dialog, null)

        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.setOnDismissListener {
            // This code will be executed when the dialog is dismissed
            moveToHomeActivity()
        }
        dialog.show()
    }

    private fun moveToHomeActivity() {
        val intent = Intent(this, com.app.milkyway.activity.HomeActivity::class.java)
        startActivity(intent)
        finish() // Optional: finish the current activity if you don't want to keep it in the stack
    }


    private fun apicall() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)

                        val pay_video = jsonArray.getJSONObject(0).getString("pay_video")

                        binding.btnDemoVideo.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pay_video))
                            startActivity(intent)
                        }


                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, activity, Constant.SETTINGS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }

}