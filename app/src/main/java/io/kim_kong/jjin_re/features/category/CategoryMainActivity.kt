package io.kim_kong.jjin_re.features.category

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.adapter.FragmentAdapter
import io.kim_kong.jjin_re.databinding.ActivityMainCategoryBinding
import io.kim_kong.jjin_re.features.category.category_fragment.CategoryFragment
import io.kim_kong.jjin_re.features.category.category_fragment.CategoryFragment.Companion.EXTRA_CATEGORY_DATA
import io.kim_kong.jjin_re.utils.BaseActivity
import io.kim_kong.jjin_re.utils.GetViewModel
import io.kim_kong.jjin_re.utils.Utils

class CategoryMainActivity: BaseActivity() {
    val binding by binding<ActivityMainCategoryBinding>(R.layout.activity_main_category)
    val viewModel by GetViewModel(CategoryViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@CategoryMainActivity)
        Utils.setIconTintDark(this@CategoryMainActivity, true)
        binding.lifecycleOwner = this@CategoryMainActivity
        binding.viewModel = viewModel
        viewModel.fragmentAdapter.value = FragmentAdapter(supportFragmentManager)
        val categoryArray = resources.getStringArray(R.array.str_review_category)
        for ((i, category) in categoryArray.withIndex()) {
            viewModel.fragmentAdapter.value!!.add(CategoryFragment.newInstance(Bundle(), "$i"))
            binding.tabCategoryMain.addTab(binding.tabCategoryMain.newTab().setText(category))
        }
        binding.vpCategoryMain.adapter = viewModel.fragmentAdapter.value

        binding.vpCategoryMain.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabCategoryMain))
        binding.tabCategoryMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {

                binding.vpCategoryMain.setCurrentItem(tab!!.position, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        val category = intent.getStringExtra(EXTRA_CATEGORY_DATA)!!.toInt()


        binding.tabCategoryMain.setScrollPosition(category, category.toFloat(), true)
        binding.vpCategoryMain.setCurrentItem(category, false)

    }
}