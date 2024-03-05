package com.app.milkyway.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.milkyway.Phonepe.PaymentActivity
import com.app.milkyway.activity.InviteActivity
import com.app.milkyway.activity.HomeActivity
import com.app.milkyway.activity.MyProductionActivity
import com.app.milkyway.activity.TransactionActivity
import com.app.milkyway.activity.UpdateProfileActivity
import com.app.milkyway.activity.WithdrawalActivity
import com.app.milkyway.databinding.FragmentMoreBinding
import com.app.milkyway.helper.ApiConfig
import com.app.milkyway.helper.Constant
import com.app.milkyway.utils.DialogUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MoreFragment : Fragment() {


    lateinit var binding: FragmentMoreBinding
    lateinit var activity: Activity
    lateinit var session: com.app.milkyway.helper.Session


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)




        activity = getActivity() as Activity
        session = com.app.milkyway.helper.Session(activity)



        (activity as HomeActivity).binding.rlToolbar.visibility = View.GONE
        userdetails()

        binding.llInvite.setOnClickListener {

            startActivity(Intent(activity, InviteActivity::class.java))


        }

        binding.llScartchCard.setOnClickListener {
            Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show()
            //startActivity(Intent(activity, com.app.pocketfarm.activity.ScartchcardActivity::class.java))
        }


        binding.tvMobile.text = session.getData(com.app.milkyway.helper.Constant.MOBILE)
        binding.tvName.text = session.getData(com.app.milkyway.helper.Constant.NAME)

        binding.cvWithdraw.setOnClickListener {
            startActivity(Intent(activity, WithdrawalActivity::class.java))
        }

        binding.rlmyBank.setOnClickListener {
            startActivity(Intent(activity, com.app.milkyway.activity.UpdatebankActivity::class.java))
        }

        binding.llMyProduction.setOnClickListener{
            startActivity(Intent(activity,MyProductionActivity::class.java))
        }

        binding.llServices.setOnClickListener{
            startActivity(Intent(activity, com.app.milkyway.activity.SupportActivity::class.java))
        }

        binding.cvRecharge.setOnClickListener {
            startActivity(Intent(activity, PaymentActivity::class.java))
        }

        binding.rlhistory.setOnClickListener {
            startActivity(Intent(activity, TransactionActivity::class.java))
        }

        binding.rlUpdateprofile.setOnClickListener{
            startActivity(Intent(activity, UpdateProfileActivity::class.java))
        }

        binding.rlwithdrawhistory.setOnClickListener{
            startActivity(Intent(activity, com.app.milkyway.activity.WithdrawalStatusActivity::class.java))
        }

        binding.rlCutomerSupport.setOnClickListener{


        }


        binding.rlLogout.setOnClickListener{
            session.logoutUser(activity)
        }

        binding.tvTotalIncome.text = "₹ " + session.getData(com.app.milkyway.helper.Constant.TOTAL_INCOME)
        binding.tvTotalRecharge.text = "₹ " + session.getData(com.app.milkyway.helper.Constant.RECHARGE)
        binding.tvTodayIncome.text = "₹ " + session.getData(com.app.milkyway.helper.Constant.TODAY_INCOME)
        binding.tvTotalAssests.text = "₹ " + session.getData(com.app.milkyway.helper.Constant.TOTAL_ASSETS)
        binding.tvTotalWithdrawal.text = "₹ " + session.getData(com.app.milkyway.helper.Constant.TOTAL_WITHDRAWAL)
        binding.tvTeamIncome.text = "₹ " + session.getData(com.app.milkyway.helper.Constant.TEAM_INCOME)



        return binding.root
    }

    private fun userdetails() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray =
                            jsonObject.getJSONArray(Constant.DATA)



                        session!!.setData(
                            Constant.ACCOUNT_NUM,
                            jsonArray.getJSONObject(0).getString(Constant.ACCOUNT_NUM)
                        )
                        session!!.setData(
                            Constant.HOLDER_NAME,
                            jsonArray.getJSONObject(0).getString(Constant.HOLDER_NAME)
                        )
                        session!!.setData(
                            Constant.BANK,
                            jsonArray.getJSONObject(0).getString(Constant.BANK)
                        )
                        session!!.setData(
                            Constant.BRANCH,
                            jsonArray.getJSONObject(0).getString(Constant.BRANCH)
                        )
                        session!!.setData(
                            Constant.IFSC,
                            jsonArray.getJSONObject(0).getString(Constant.IFSC)
                        )
                        session!!.setData(
                            Constant.AGE,
                            jsonArray.getJSONObject(0).getString(Constant.AGE)
                        )
                        session!!.setData(
                            Constant.CITY,
                            jsonArray.getJSONObject(0).getString(Constant.CITY)
                        )
                        session!!.setData(
                            Constant.STATE,
                            jsonArray.getJSONObject(0).getString(Constant.STATE)
                        )
                        session!!.setData(
                            Constant.DEVICE_ID,
                            jsonArray.getJSONObject(0).getString(Constant.DEVICE_ID)
                        )
                        session!!.setData(
                            Constant.RECHARGE, jsonArray.getJSONObject(0).getString(
                                Constant.RECHARGE))
                        session!!.setData(
                            Constant.TODAY_INCOME, jsonArray.getJSONObject(0).getString(
                                Constant.TODAY_INCOME))
                        session!!.setData(
                            Constant.TOTAL_INCOME, jsonArray.getJSONObject(0).getString(
                                Constant.TOTAL_INCOME))
                        session!!.setData(
                            Constant.BALANCE, jsonArray.getJSONObject(0).getString(
                                Constant.BALANCE))
                        session!!.setData(
                            Constant.WITHDRAWAL_STATUS, jsonArray.getJSONObject(0).getString(
                                Constant.WITHDRAWAL_STATUS))
                        session!!.setData(
                            Constant.TEAM_SIZE, jsonArray.getJSONObject(0).getString(
                                Constant.TEAM_SIZE))
                        session!!.setData(
                            Constant.VALID_TEAM, jsonArray.getJSONObject(0).getString(
                                Constant.VALID_TEAM))
                        session!!.setData(
                            Constant.TOTAL_WITHDRAWAL, jsonArray.getJSONObject(0).getString(
                                Constant.TOTAL_WITHDRAWAL))
                        session!!.setData(
                            Constant.TEAM_INCOME, jsonArray.getJSONObject(0).getString(
                                Constant.TEAM_INCOME))
                        session!!.setData(
                            Constant.TOTAL_ASSETS, jsonArray.getJSONObject(0).getString(
                                Constant.TOTAL_ASSETS))
                        session!!.setData(
                            Constant.BANK, jsonArray.getJSONObject(0).getString(
                                Constant.BANK))
                        session!!.setData(
                            Constant.BRANCH, jsonArray.getJSONObject(0).getString(
                                Constant.BRANCH))
                        session!!.setData(Constant.IFSC, jsonArray.getJSONObject(0).getString(
                                Constant.IFSC))
                           session!!.setData(Constant.ACCOUNT_NUM, jsonArray.getJSONObject(0).getString(
                                Constant.ACCOUNT_NUM))
                        session!!.setData(Constant.HOLDER_NAME, jsonArray.getJSONObject(0).getString(
                                Constant.HOLDER_NAME))



                        binding.tvTotalIncome.text = "₹ " + session.getData(com.app.milkyway.helper.Constant.TOTAL_INCOME)
                        binding.tvTotalRecharge.text = "₹ " + session.getData(com.app.milkyway.helper.Constant.RECHARGE)
                        binding.tvTodayIncome.text = "₹ " + session.getData(com.app.milkyway.helper.Constant.TODAY_INCOME)
                        binding.tvTotalAssests.text = "₹ " + session.getData(com.app.milkyway.helper.Constant.TOTAL_ASSETS)
                        binding.tvTotalWithdrawal.text = "₹ " + session.getData(com.app.milkyway.helper.Constant.TOTAL_WITHDRAWAL)
                        binding.tvTeamIncome.text = "₹ " + session.getData(com.app.milkyway.helper.Constant.TEAM_INCOME)




                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.USER_DETAILS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }



}