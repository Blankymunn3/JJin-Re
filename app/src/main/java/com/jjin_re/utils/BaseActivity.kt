package com.jjin_re.utils

import android.app.Activity
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.util.*

open class BaseActivity : AppCompatActivity() {

    companion object {
        var actList = ArrayList<Activity>()
        fun clearActivity() {
            for (act in actList) {
                act.finish()
            }
            actList.clear()
        }
    }

    open fun progressON() {
        BaseApplication.instance.progressON(this@BaseActivity, null)
    }

    open fun progressON(message: String?) {
        BaseApplication.instance.progressON(this@BaseActivity, message)
    }

    open fun progressOFF() {
        BaseApplication.instance.progressOFF()
    }

    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes resId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView<T>(this, resId) }

}
