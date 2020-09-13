package io.kim_kong.jjin_re.features.my_review

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import com.google.android.material.tabs.TabLayout
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.adapter.FragmentAdapter
import io.kim_kong.jjin_re.databinding.ActivityMainCategoryBinding
import io.kim_kong.jjin_re.databinding.ActivityMyReviewBinding
import io.kim_kong.jjin_re.features.add_review.AddReviewActivity
import io.kim_kong.jjin_re.features.category.CategoryViewModel
import io.kim_kong.jjin_re.features.category.category_fragment.CategoryFragment
import io.kim_kong.jjin_re.features.my_review.my_review_fragment.MyReviewFragment
import io.kim_kong.jjin_re.utils.*

class MyReviewActivity :BaseActivity() {

    val binding by binding<ActivityMyReviewBinding>(R.layout.activity_my_review)
    val viewModel by GetViewModel(MyReviewViewModel::class.java)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add_review, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.menu_add_review -> {
                val intent = Intent(this, AddReviewActivity::class.java)
                intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, 0)
                intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, 0)
                ActivityCompat.startActivity(this, intent, null)
                overridePendingTransition(0, 0)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@MyReviewActivity)
        Utils.setIconTintDark(this@MyReviewActivity, true)
        setSupportActionBar(binding.tbMyReview)
        binding.lifecycleOwner = this@MyReviewActivity
        binding.viewModel = viewModel
        viewModel.fragmentAdapter.value = FragmentAdapter(supportFragmentManager)
        val categoryArray = resources.getStringArray(R.array.str_review_category)
        for ((i, category) in categoryArray.withIndex()) {
            viewModel.fragmentAdapter.value!!.add(MyReviewFragment.newInstance(Bundle(), "$i"))
            binding.tabMyReviewMain.addTab(binding.tabMyReviewMain.newTab().setText(category))
        }
        binding.vpMyReviewMain.adapter = viewModel.fragmentAdapter.value

        binding.vpMyReviewMain.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabMyReviewMain))
        binding.tabMyReviewMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {

                binding.vpMyReviewMain.setCurrentItem(tab!!.position, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        val category = intent.getStringExtra(CategoryFragment.EXTRA_CATEGORY_DATA)!!.toInt()


        binding.tabMyReviewMain.setScrollPosition(category, category.toFloat(), true)
        binding.vpMyReviewMain.setCurrentItem(category, false)

    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@MyReviewActivity)
    }
}