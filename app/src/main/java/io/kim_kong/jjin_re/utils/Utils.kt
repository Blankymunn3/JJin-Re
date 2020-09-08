package io.kim_kong.jjin_re.utils

import android.animation.Animator
import android.app.Activity
import android.os.Build
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import io.kim_kong.jjin_re.R


object Utils {

    const val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
    const val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"
    @JvmStatic
    fun setIconTintDark(activity: Activity?, hasToolBar: Boolean) {
        val window: Window = activity!!.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            if (hasToolBar) {
                window.statusBarColor = ContextCompat.getColor(activity, R.color.colorPrimary)
            } else {
                window.statusBarColor = ContextCompat.getColor(activity, R.color.colorGray)
            }

            val decor = activity.window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(activity, R.color.colorBlack)
        }
    }

    @JvmStatic
    fun showToast(message: String, activity: Activity) {
        val inflater = activity.layoutInflater
        val toastDesign =
            inflater.inflate(R.layout.toast_design, activity.findViewById(R.id.toast_design_root))
        val text = toastDesign.findViewById<TextView>(R.id.TextView_toast_design)
        text.text = message
        val toast = Toast(activity.applicationContext)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = toastDesign
        toast.show()
    }

    @JvmStatic
    fun visibleDeleteButton(
        activity: BaseActivity,
        editText: View,
        imageButton: View,
        item: MutableLiveData<String>
    ) {
        editText.setOnFocusChangeListener { _, it ->
            if (it)
                item.observe(activity) {
                    if (it.isNotEmpty()) imageButton.visibility = View.VISIBLE
                    else imageButton.visibility = View.GONE
                }
            else imageButton.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    fun revealView(view: View) {
        val centerY = view.measuredHeight / 2
        val centerX = view.measuredWidth / 2
        val animator: Animator = ViewAnimationUtils.createCircularReveal(
            view,
            centerX, centerY, 0f, view.width.toFloat()
        )
        animator.setDuration(400)
        view.visibility = View.VISIBLE
        animator.setStartDelay(0)
        animator.start()
    }
}