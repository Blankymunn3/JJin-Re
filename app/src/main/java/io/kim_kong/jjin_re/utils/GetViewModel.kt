package io.kim_kong.jjin_re.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import kotlin.reflect.KProperty

class GetViewModel<out T : ViewModel>(private val clazz: Class<T>) {

    private var value: T? = null

    operator fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        if (value == null) {
            value = ViewModelProviders.of(thisRef).get(clazz)
        }

        return value!!
    }

    operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (value == null) {
            value = ViewModelProviders.of(thisRef).get(clazz)
        }

        return value!!
    }
}

