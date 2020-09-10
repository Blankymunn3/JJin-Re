package io.kim_kong.jjin_re.features.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.adapter.FragmentAdapter
import io.kim_kong.jjin_re.databinding.ActivityMainBinding
import io.kim_kong.jjin_re.features.add_review.AddReviewActivity
import io.kim_kong.jjin_re.features.main.fragment.home.HomeFragment
import io.kim_kong.jjin_re.features.main.fragment.more.MoreFragment
import io.kim_kong.jjin_re.utils.BaseActivity
import io.kim_kong.jjin_re.utils.BaseApplication
import io.kim_kong.jjin_re.utils.GetViewModel
import io.kim_kong.jjin_re.utils.RevealAnimation.Companion.EXTRA_CIRCULAR_REVEAL_X
import io.kim_kong.jjin_re.utils.RevealAnimation.Companion.EXTRA_CIRCULAR_REVEAL_Y
import io.kim_kong.jjin_re.utils.Utils


class MainActivity : BaseActivity() {
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    val viewModel by GetViewModel(MainViewModel::class.java)

    private lateinit var search: MenuItem
    private var defaultElevation = 0.0f

    private var isOpen = false
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)
        search = menu!!.findItem(R.id.search)
        search.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                return true
            }

        })

        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Utils.showToast(query, this@MainActivity)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {

                return true
            }

        })
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@MainActivity)
        Utils.setIconTintDark(this@MainActivity, true)
        binding.viewModel = viewModel
        setSupportActionBar(binding.tbMain)
        initActivityView()
        Utils.showSnackBar("${BaseApplication.userModel.nickName}님 반갑습니다", binding.root, false)

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
        viewModel.fragmentActivity.value = FragmentAdapter(supportFragmentManager)
        viewModel.fragmentActivity.value!!.add(HomeFragment.newInstance(Bundle()))
        viewModel.fragmentActivity.value!!.add(MoreFragment.newInstance(Bundle()))
        binding.vpMain.adapter = viewModel.fragmentActivity.value

        binding.vpMain.setCurrentItem(0, false)
        binding.vpMain.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.bottomNavigationMain.selectedItemId = R.id.menu_home
                    2 -> binding.bottomNavigationMain.selectedItemId = R.id.menu_more
                }
            }

        })

        binding.bottomNavigationMain.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    search.isVisible = true
                    setElevation(defaultElevation)
                    binding.vpMain.setCurrentItem(0, false)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_more -> {
                    search.isVisible = false
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            defaultElevation = binding.abMain.elevation
        }
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@MainActivity)
    }
}