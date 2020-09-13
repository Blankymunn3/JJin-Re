package io.kim_kong.jjin_re.features.main.fragment.more

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.databinding.FragmentMoreBinding
import io.kim_kong.jjin_re.features.category.CategoryMainActivity
import io.kim_kong.jjin_re.features.category.category_fragment.CategoryFragment
import io.kim_kong.jjin_re.features.edit_profile.EditProfileActivity
import io.kim_kong.jjin_re.features.login.LoginActivity
import io.kim_kong.jjin_re.features.main.MainActivity
import io.kim_kong.jjin_re.features.my_review.MyReviewActivity
import io.kim_kong.jjin_re.utils.BaseApplication
import io.kim_kong.jjin_re.utils.Extra
import io.kim_kong.jjin_re.utils.GetViewModel
import io.kim_kong.jjin_re.utils.SharedPreferenceHelper


class MoreFragment: Fragment() {
    lateinit var binding : FragmentMoreBinding
    private lateinit var activity: MainActivity
    private val viewModel by GetViewModel(MoreViewModel::class.java)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as MainActivity
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): MoreFragment {
            val fragment = MoreFragment()
            fragment.arguments = bundle
            return fragment
        }

        @JvmStatic
        fun newInstance() = MoreFragment()
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_more, null, false)
        binding = FragmentMoreBinding.bind(view)
        binding.lifecycleOwner = this@MoreFragment
        binding.viewModel = viewModel

        viewModel.userModel.observe(this@MoreFragment, {
            if (it.userId.isNotEmpty()) viewModel.getUserReviewCnt()
        })

        binding.layoutFragmentMoreProfile.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }

        binding.layoutFragmentMoreUserReview.setOnClickListener {
            val intent = Intent(activity, MyReviewActivity::class.java)
            intent.putExtra(CategoryFragment.EXTRA_CATEGORY_DATA, "0")
            startActivity(intent)
        }

        return binding.root
    }
}