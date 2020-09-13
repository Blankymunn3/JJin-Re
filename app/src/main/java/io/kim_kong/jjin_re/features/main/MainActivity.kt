package io.kim_kong.jjin_re.features.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.google.firebase.messaging.FirebaseMessaging
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.adapter.FragmentAdapter
import io.kim_kong.jjin_re.databinding.ActivityMainBinding
import io.kim_kong.jjin_re.features.add_review.AddReviewActivity
import io.kim_kong.jjin_re.features.category.CategoryMainActivity
import io.kim_kong.jjin_re.features.category.category_fragment.CategoryFragment.Companion.EXTRA_CATEGORY_DATA
import io.kim_kong.jjin_re.features.main.fragment.home.HomeFragment
import io.kim_kong.jjin_re.features.main.fragment.more.MoreFragment
import io.kim_kong.jjin_re.features.search.SearchActivity
import io.kim_kong.jjin_re.utils.BaseActivity
import io.kim_kong.jjin_re.utils.BaseApplication
import io.kim_kong.jjin_re.utils.Extra.EXTRA_MY_REVIEW
import io.kim_kong.jjin_re.utils.GetViewModel
import io.kim_kong.jjin_re.utils.RevealAnimation.Companion.EXTRA_CIRCULAR_REVEAL_X
import io.kim_kong.jjin_re.utils.RevealAnimation.Companion.EXTRA_CIRCULAR_REVEAL_Y
import io.kim_kong.jjin_re.utils.Utils


class MainActivity : BaseActivity() {
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    val viewModel by GetViewModel(MainViewModel::class.java)
    var fragmentPosition: Int = 0

    private var defaultElevation = 0.0f
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
    }

    private fun presentActivity(view: View) {
        val intent = Intent(this, AddReviewActivity::class.java)
        intent.putExtra(EXTRA_CIRCULAR_REVEAL_X, (view.x + view.width / 2).toInt())
        intent.putExtra(EXTRA_CIRCULAR_REVEAL_Y, (view.y + view.height / 2).toInt())
        ActivityCompat.startActivity(this, intent, null)
        overridePendingTransition(0, 0)
    }

    private fun initActivityView() {
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

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    fragmentPosition = 0
                    setElevation(defaultElevation)
                    binding.vpMain.setCurrentItem(0, false)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_more -> {
                    fragmentPosition = 2
                    setElevation(0.0f)
                    binding.vpMain.setCurrentItem(2, false)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            true
        }
    }

    private fun setElevation(elevation: Float) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            binding.abMain.elevation = elevation
        }
    }

    fun onCategoryItemClick(view: View) {
        var category = "0"
        var userID = ""
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
        intent.putExtra(EXTRA_MY_REVIEW, userID)
        intent.putExtra(EXTRA_CATEGORY_DATA, category)
        startActivity(intent)
    }

    fun onSearchViewClick(view: View) {
        startActivity(Intent(this@MainActivity, SearchActivity::class.java))
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            defaultElevation = binding.abMain.elevation
        }
    }

    override fun onBackPressed() {
        if (fragmentPosition != 0) {
            binding.vpMain.setCurrentItem(0, false)
        } else {
            super.onBackPressed()
        }

    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@MainActivity)
    }
}