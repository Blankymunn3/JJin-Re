package io.kim_kong.jjin_re.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AccelerateInterpolator


class RevealAnimation(val view: View, val revealX: Int, val revealY: Int, private val activity: Activity) {

    fun revealActivity(x: Int, y: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val finalRadius = (view.width.coerceAtLeast(view.height) * 1.1).toFloat()
            val circularReveal: Animator =
                ViewAnimationUtils.createCircularReveal(view, x, y, 0f, finalRadius)
            circularReveal.duration = 500
            circularReveal.interpolator = AccelerateInterpolator()

            view.visibility = View.VISIBLE
            circularReveal.start()
        } else {
            activity.finish()
        }
    }

    fun unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.finish()
        } else {
            val finalRadius = (view.width.coerceAtLeast(view.height) * 1.1).toFloat()
            val circularReveal: Animator = ViewAnimationUtils.createCircularReveal(
                view, revealX, revealY, finalRadius, 0f
            )
            circularReveal.duration = 300
            circularReveal.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.visibility = View.INVISIBLE
                    activity.finish()
                    activity.overridePendingTransition(0, 0)
                }
            })
            circularReveal.start()
        }
    }

    companion object {
        const val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
        const val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.visibility = View.INVISIBLE
            val viewTreeObserver: ViewTreeObserver = view.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        revealActivity(revealX, revealY)
                        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        } else {
            view.visibility = View.VISIBLE
        }
    }
}