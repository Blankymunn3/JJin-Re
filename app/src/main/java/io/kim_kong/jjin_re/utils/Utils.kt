package io.kim_kong.jjin_re.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import io.kim_kong.jjin_re.R
import java.io.IOException


object Utils {

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
    fun visibleDeleteButton(activity: BaseActivity, editText: View, imageButton: View, item: MutableLiveData<String>) {
        editText.setOnFocusChangeListener { _, it ->
            if (it)
                item.observe(activity) {
                    if (it.isNotEmpty()) imageButton.visibility = View.VISIBLE
                    else imageButton.visibility = View.GONE
                }
            else imageButton.visibility = View.GONE
        }
    }

    fun getOrientationOfImage(filepath: String?): Int {
        val exif: ExifInterface?
        exif = try {
            ExifInterface(filepath!!)
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }
        val orientation = exif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }
    @JvmStatic
    fun getRotatedBitmap(bitmap: Bitmap?, degrees: Int): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0) return bitmap
        val m = Matrix()
        m.setRotate(degrees.toFloat(), bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }
}