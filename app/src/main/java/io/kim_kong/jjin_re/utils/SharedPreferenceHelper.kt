package io.kim_kong.jjin_re.utils

import android.app.Activity
import android.content.SharedPreferences
import io.kim_kong.jjin_re.model.UserModel
import io.kim_kong.jjin_re.utils.BaseActivity
import io.kim_kong.jjin_re.utils.BaseApplication

object SharedPreferenceHelper {

    @JvmStatic
    fun setSharedAutoLogin(activity: Activity, isAutoLogin: Boolean) {
        val setting = activity.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val editor = setting.edit()
        editor.putBoolean("is_auto_login", isAutoLogin)
        editor.apply()
    }

    @JvmStatic
    fun isSharedAutoLogin(activity: Activity): Boolean {
        val setting = activity.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        return setting.getBoolean("is_auto_login", false)
    }

    @JvmStatic
    fun setUserDataToSharedPreference(activity: BaseActivity, userModel: UserModel) {
        val setting: SharedPreferences =
            activity.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = setting.edit()

        editor.putString("user_id", userModel.userId)
        editor.putString("user_name", userModel.nickName)
        editor.putString("user_email", userModel.email)
        editor.putString("user_phone", userModel.phone)
        editor.putString("user_area", userModel.area)
        editor.putString("user_type", userModel.type)
        editor.putString("onPush", userModel.isPushAllow)
        editor.putString("mktAllow", userModel.isMktAllow)

        BaseApplication.userModel = userModel

        editor.apply()
    }
    @JvmStatic
    fun getUserDataToSharedPreference(activity: BaseActivity): UserModel {
        val setting: SharedPreferences =
            activity.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val model = UserModel()
        model.userId = setting.getString("user_id", "")!!
        model.email = setting.getString("user_email", "")!!
        model.nickName = setting.getString("user_name", "")!!
        model.phone = setting.getString("user_phone", "")!!
        model.area = setting.getString("user_area", "")!!
        model.type = setting.getString("user_type", "")!!
        model.isPushAllow = setting.getString("onPush", "")!!
        model.isMktAllow = setting.getString("mktAllow", "")!!

        return model
    }
    @JvmStatic
    fun clearUserDataToSharedPreference(activity: BaseActivity) {
        val setting: SharedPreferences =
            activity.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = setting.edit()

        editor.putString("user_id", "")
        editor.putString("user_name", "")
        editor.putString("user_email", "")
        editor.putString("user_phone", "")
        editor.putString("user_area", "")
        editor.putString("user_type", "")
        editor.putString("onPush", "")
        editor.putString("mktAllow", "")

        editor.apply()
    }
}