package com.app.milkyway.activity

import android.app.Activity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.app.milkyway.fragment.ExploreFragment
import com.app.milkyway.fragment.HomeFragment
import com.app.milkyway.fragment.MoreFragment
import com.app.milkyway.fragment.MyteamFragment
import com.app.milkyway.fragment.PlanFragment
import com.app.milkyway.R
import com.app.milkyway.databinding.ActivityHomeBinding
import com.app.milkyway.helper.Constant
import com.app.milkyway.helper.Session
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.zoho.commons.InitConfig
import com.zoho.livechat.android.listeners.InitListener
import com.zoho.salesiqembed.ZohoSalesIQ
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {


    private var fragment_container: FrameLayout? = null
    private var bottomNavigationView: BottomNavigationView? = null
    lateinit var binding: ActivityHomeBinding

    private lateinit var activity: Activity
    private lateinit var session: com.app.milkyway.helper.Session

    private lateinit var fm: FragmentManager

    private lateinit var homeFragment: HomeFragment
    private lateinit var myteamFragment: MyteamFragment
    private lateinit var exploreFragment: ExploreFragment
    private lateinit var moreFragment: MoreFragment
    private lateinit var planFragment: PlanFragment

    val ONESIGNAL_APP_ID = "510e6dfc-a8e6-403a-9167-188e73d5c048"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        session = com.app.milkyway.helper.Session(activity)
        //tvTitle text is double color text "We" and "Agri"
        val tvTitle = "<font color='#F8B328'>Pocket</font> " + "<font color='#00B251'>Farm</font>"

        binding.tvTitle.text = Html.fromHtml(tvTitle)

        fm = supportFragmentManager
        homeFragment = HomeFragment()
        myteamFragment = MyteamFragment()
        exploreFragment = ExploreFragment()
        moreFragment = MoreFragment()
        planFragment = PlanFragment()



        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView!!.setOnItemSelectedListener(this)

        fm.beginTransaction().replace(R.id.fragment_container, homeFragment).commit()
        bottomNavigationView!!.selectedItemId = R.id.navHome


        binding.fab.setOnClickListener {
            fm.beginTransaction().replace(R.id.fragment_container, exploreFragment).commit()
            bottomNavigationView!!.selectedItemId = R.id.placeholder

        }


        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.Debug.logLevel = LogLevel.VERBOSE

        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)

        // requestPermission will show the native Android notification permission prompt.
        // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
            val externalId = session.getData(Constant.USER_ID)
            OneSignal.login(externalId)
        }


    }


    override fun onResume() {
        super.onResume()
        ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.NEVER)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction: FragmentTransaction = fm.beginTransaction()

        when (item.itemId) {
            R.id.navHome -> {




//                transaction.setCustomAnimations(
//                    R.anim.slide_in_top_right,
//                    R.anim.slide_out_bottom_left
//                )
                transaction.replace(R.id.fragment_container, homeFragment)
            }

            R.id.navplan -> {
//                transaction.setCustomAnimations(
//                    R.anim.slide_in_top_right,
//                    R.anim.slide_out_bottom_left
//                )
                transaction.replace(R.id.fragment_container, planFragment)
            }

//            R.id.navExplore -> {
//                transaction.replace(R.id.fragment_container, exploreFragment)
//            }

            R.id.navMyteam ->{
                transaction.replace(R.id.fragment_container,myteamFragment)
            }

            R.id.navMore ->{
                transaction.replace(R.id.fragment_container,moreFragment)
            }

        }



        transaction.commit()
        return true
    }

    // onBackPress method is not present in SplashScreen Activity.kt
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


}