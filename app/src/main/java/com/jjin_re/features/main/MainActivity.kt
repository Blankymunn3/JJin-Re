package com.jjin_re.features.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.jjin_re.R
import com.jjin_re.adapter.FragmentAdapter
import com.jjin_re.databinding.ActivityMainBinding
import com.jjin_re.features.add_review.AddReviewActivity
import com.jjin_re.features.category.CategoryMainActivity
import com.jjin_re.features.category.category_fragment.CategoryFragment.Companion.EXTRA_CATEGORY_DATA
import com.jjin_re.features.main.fragment.home.HomeFragment
import com.jjin_re.features.main.fragment.more.MoreFragment
import com.jjin_re.features.search.SearchActivity
import com.jjin_re.utils.*
import com.jjin_re.utils.RevealAnimation.Companion.EXTRA_CIRCULAR_REVEAL_X
import com.jjin_re.utils.RevealAnimation.Companion.EXTRA_CIRCULAR_REVEAL_Y
import org.json.JSONObject
import java.util.*


class MainActivity : BaseActivity() {
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    val viewModel by GetViewModel(MainViewModel::class.java)
    private var fragmentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@MainActivity)
        Utils.setIconTintDark(this@MainActivity, true)
        binding.lifecycleOwner = this@MainActivity
        binding.viewModel = viewModel
        setSupportActionBar(binding.tbMain)
        initActivityView()
        binding.fabMainAdd.setOnClickListener {
            presentActivity(it)
        }

        viewModel.userToken.observe(this@MainActivity, {
            if (!it.isNullOrEmpty()) viewModel.userTokenChange()
        })
    }

    private fun presentActivity(view: View) {
        val intent = Intent(this, AddReviewActivity::class.java)
        intent.putExtra(EXTRA_CIRCULAR_REVEAL_X, (view.x + view.width / 2).toInt())
        intent.putExtra(EXTRA_CIRCULAR_REVEAL_Y, (view.y + view.height / 2).toInt())
        ActivityCompat.startActivity(this, intent, null)
        overridePendingTransition(0, 0)
    }

    private fun initActivityView() {
        FirebaseMessaging.getInstance().subscribeToTopic("mkt_all")
        FirebaseMessaging.getInstance().subscribeToTopic("mkt_android")
        FirebaseMessaging.getInstance().subscribeToTopic("event_all")
        FirebaseMessaging.getInstance().subscribeToTopic("event_android")
        viewModel.fragmentAdapter.value = FragmentAdapter(supportFragmentManager)
        viewModel.fragmentAdapter.value!!.add(HomeFragment.newInstance(Bundle()))
        viewModel.fragmentAdapter.value!!.add(MoreFragment.newInstance(Bundle()))
        binding.vpMain.adapter = viewModel.fragmentAdapter.value

        binding.vpMain.setCurrentItem(0, false)
        binding.vpMain.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.bottomNavigationView.selectedItemId = R.id.menu_home
                    2 -> binding.bottomNavigationView.selectedItemId = R.id.menu_more
                }
            }
        })

        try {
            val token = FirebaseInstanceId.getInstance().token
            val setting = getSharedPreferences("setting", MODE_PRIVATE)
            val editor = setting.edit()
            editor.putString("user_token", token)
            editor.apply()
            BaseApplication.firebaseToken = token!!
            viewModel.userToken.postValue(BaseApplication.firebaseToken)
            FirebaseMessaging.getInstance().isAutoInitEnabled = true
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    fragmentPosition = 0
                    binding.vpMain.setCurrentItem(0, false)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_more -> {
                    binding.abMain.setExpanded(true)
                    fragmentPosition = 2
                    binding.vpMain.setCurrentItem(2, false)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            true
        }
    }

    fun onCategoryItemClick(view: View) {
        var category = "0"
        when (view.id) {
            R.id.layout_home_category_furniture -> category = "1"
            R.id.layout_home_category_digital -> category = "2"
            R.id.layout_home_category_pet -> category = "3"
            R.id.layout_home_category_beauty -> category = "4"
            R.id.layout_home_category_movie -> category = "5"
            R.id.layout_home_category_clothing -> category = "6"
            R.id.layout_home_category_hobby -> category = "7"
            R.id.layout_home_category_food -> category = "8"
            R.id.layout_home_category_etc -> category = "9"
        }
        val intent = Intent(this@MainActivity, CategoryMainActivity::class.java)
        intent.putExtra(EXTRA_CATEGORY_DATA, category)
        startActivity(intent)
    }

    fun onSearchViewClick(view: View) {
        startActivity(Intent(this@MainActivity, SearchActivity::class.java))
    }

    fun onRefreshNotification() {
        val lastNoticeString: List<String> =
            ArrayList<String>(SharedPreferenceHelper.getLastNotificationCheck(this@MainActivity))
        val notificationMap: Map<String?, Any?> =
            SharedPreferenceHelper.getNotificationMessage(this@MainActivity)
        val jsonObject = JSONObject(notificationMap)
        val keys = jsonObject.keys()
        val keyArray = ArrayList<Any>()
        var count = 0
        while (keys.hasNext()) {
            val key = keys.next()
            keyArray.add(key)
            for (noticeItem in lastNoticeString) {
                if (noticeItem == key) {
                    count++
                    break
                }
            }
        }
        count = keyArray.size - count
        if (count > 0) {
            if (count > 99) count = 99
            binding.tvNotificationCnt.text = count.toString()
            binding.tvNotificationCnt.visibility = View.VISIBLE
        } else {
            binding.tvNotificationCnt.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        //onRefreshNotification()
    }

    override fun onBackPressed() {
        if (fragmentPosition != 0) {
            binding.vpMain.setCurrentItem(0, false)
        } else {
            super.onBackPressed()
        }

    }
}