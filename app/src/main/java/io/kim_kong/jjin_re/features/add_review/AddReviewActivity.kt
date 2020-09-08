package io.kim_kong.jjin_re.features.add_review

import android.os.Bundle
import android.util.Log
import androidx.coordinatorlayout.widget.CoordinatorLayout
import io.kim_kong.jjin_re.R
import io.kim_kong.jjin_re.databinding.ActivityAddReviewBinding
import io.kim_kong.jjin_re.utils.BaseActivity
import io.kim_kong.jjin_re.utils.GetViewModel
import io.kim_kong.jjin_re.utils.RevealAnimation
import io.kim_kong.jjin_re.utils.RevealAnimation.Companion.EXTRA_CIRCULAR_REVEAL_X
import io.kim_kong.jjin_re.utils.RevealAnimation.Companion.EXTRA_CIRCULAR_REVEAL_Y
import io.kim_kong.jjin_re.utils.Utils


open class AddReviewActivity: BaseActivity() {
    val binding by binding<ActivityAddReviewBinding>(R.layout.activity_add_review)
    val viewModel by GetViewModel(AddReviewViewModel::class.java)

    private lateinit var rootLayout : CoordinatorLayout
    private lateinit var mRevealAnimation: RevealAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@AddReviewActivity)
        Utils.setIconTintDark(this@AddReviewActivity, true)
        binding.viewModel = viewModel

        rootLayout = binding.layoutAddReviewMain
        val x = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0)
        val y = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0)

        mRevealAnimation = RevealAnimation(rootLayout, x, y, this@AddReviewActivity)


    }

    override fun onBackPressed() {
        mRevealAnimation.unRevealActivity()
    }

}