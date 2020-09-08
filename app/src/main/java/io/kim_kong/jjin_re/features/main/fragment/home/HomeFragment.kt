package io.kim_kong.jjin_re.features.main.fragment.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.databinding.FragmentHomeBinding
import io.kim_kong.jjin_re.features.main.MainActivity
import io.kim_kong.jjin_re.utils.GetViewModel

class HomeFragment: Fragment() {
    private lateinit var activity: MainActivity
    private val viewModel by GetViewModel(HomeViewModel::class.java)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as MainActivity
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): HomeFragment {
            val fragment = HomeFragment()
            fragment.arguments = bundle
            return fragment
        }
        @JvmStatic
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_home, null, false)
        val binding = FragmentHomeBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}