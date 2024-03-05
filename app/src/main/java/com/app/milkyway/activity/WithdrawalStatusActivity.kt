package com.app.milkyway.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.milkyway.adapter.WithdrawalAdapter
import com.app.milkyway.model.Withdrawal
import com.app.milkyway.databinding.ActivityWithdrawalStatusBinding
import com.app.milkyway.helper.Constant
import com.app.milkyway.utils.DialogUtils
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class WithdrawalStatusActivity : AppCompatActivity() {

    lateinit var binding: ActivityWithdrawalStatusBinding
    private lateinit var activity: Activity
    private lateinit var session: com.app.milkyway.helper.Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWithdrawalStatusBinding.inflate(layoutInflater)
        activity = this
        session = com.app.milkyway.helper.Session(activity)

        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }


        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvHistory.layoutManager = linearLayoutManager


         swipeRefreshLayout.setOnRefreshListener { history(swipeRefreshLayout) }

          history(swipeRefreshLayout)


        setContentView(binding.root)
    }

    private fun history(swipeRefreshLayout:SwipeRefreshLayout) {
        val params: MutableMap<String, String> = HashMap()
        params["user_id"] = session.getData(Constant.USER_ID)
        com.app.milkyway.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.milkyway.helper.Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(com.app.milkyway.helper.Constant.DATA)

                        val g = Gson()
                        val withdrawal: java.util.ArrayList<Withdrawal> =
                            java.util.ArrayList<Withdrawal>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Withdrawal = g.fromJson(jsonObject1.toString(), Withdrawal::class.java)
                                withdrawal.add(group)
                            } else {
                                break
                            }
                        }

                        val adapter = WithdrawalAdapter(activity, withdrawal)
                        binding.rvHistory.adapter = adapter
                        swipeRefreshLayout.isRefreshing = false


                    } else {
                        swipeRefreshLayout.isRefreshing = false
                        DialogUtils.showCustomDialog(this, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(activity, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
            }
        }, activity, com.app.milkyway.helper.Constant.WITHDRAWAL_LIST, params, true)
    }


}