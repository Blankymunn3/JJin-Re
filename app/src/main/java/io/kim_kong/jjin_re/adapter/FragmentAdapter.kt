package io.kim_kong.jjin_re.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

class FragmentAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragment: MutableList<Fragment> = ArrayList()
    private val namePage: List<String> = ArrayList()
    fun add(fragment: Fragment) {
        this.fragment.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return namePage[position]
    }

    override fun getCount(): Int {
        return fragment.size
    }

}
