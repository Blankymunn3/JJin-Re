package io.kim_kong.jjin_re.features.main.fragment.more

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.databinding.FragmentMoreBinding
import io.kim_kong.jjin_re.features.login.LoginActivity
import io.kim_kong.jjin_re.features.main.MainActivity
import io.kim_kong.jjin_re.utils.GetViewModel
import io.kim_kong.jjin_re.utils.SharedPreferenceHelper


class MoreFragment: Fragment() {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_more, null, false)
        val binding = FragmentMoreBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.btnLogout.setOnClickListener {
            SharedPreferenceHelper.clearUserDataToSharedPreference(activity)
            SharedPreferenceHelper.setSharedAutoLogin(activity, false)
            startActivity(Intent(activity, LoginActivity::class.java))
            activity.finish()
        }

        return binding.root
    }
}