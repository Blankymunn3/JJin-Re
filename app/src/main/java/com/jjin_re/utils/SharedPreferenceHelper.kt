package com.jjin_re.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.jjin_re.model.UserModel
import org.json.JSONException
import org.json.JSONObject
import java.util.*

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
        editor.putString("user_phone", userModel.phone)
        editor.putString("user_img", userModel.userImg)
        editor.putInt("user_type", userModel.type)
        editor.putString("social_type", userModel.socialType)
        editor.putString("user_token", userModel.userToken)
        editor.putInt("review_push", userModel.reviewPush)
        editor.putInt("mkt_push", userModel.mktPush)

        BaseApplication.userModel = userModel

        editor.apply()
    }
    @JvmStatic
    fun getUserDataToSharedPreference(activity: Context): UserModel {
        val setting: SharedPreferences =
            activity.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val model = UserModel()
        model.userId = setting.getString("user_id", "")!!
        model.nickName = setting.getString("user_name", "")!!
        model.userImg = setting.getString("user_img", "")!!
        model.phone = setting.getString("user_phone", "")!!
        model.type = setting.getInt("user_type", 1)
        model.socialType = setting.getString("social_type", "")!!
        model.userToken = setting.getString("user_token", "")!!
        model.reviewPush = setting.getInt("review_push", 1)
        model.mktPush = setting.getInt("mkt_push", 1)

        return model
    }
    @JvmStatic
    fun clearUserDataToSharedPreference(activity: BaseActivity) {
        val setting: SharedPreferences =
            activity.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = setting.edit()

        editor.putString("user_id", "")
        editor.putString("user_name", "")
        editor.putString("user_img", "")
        editor.putString("user_phone", "")
        editor.putInt("user_type", 0)
        editor.putString("social_type", "")
        editor.putString("user_token", "")
        editor.putInt("review_push", 0)
        editor.putInt("mkt_pusth", 0)
        editor.putStringSet("notice", HashSet())

        editor.apply()
    }


    fun getLastNoticeCheck(context: Context): MutableSet<String?> {
        val setting = context.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        return HashSet(setting.getStringSet("notice", HashSet()))
    }

    fun setLastNoticeCheck(context: Context, lastUid: String?) {
        val lastNoticeCheck = getLastNoticeCheck(context)
        lastNoticeCheck.add(lastUid)
        val setting = context.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val editor = setting.edit()
        editor.putStringSet("notice", lastNoticeCheck)
        editor.apply()
    }


    fun getLastNotificationCheck(context: Context): MutableSet<String?> {
        val setting = context.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        return HashSet(setting.getStringSet("notification_check", HashSet()))
    }

    fun setLastNotificationCheck(context: Context, lastUid: String?) {
        val lastNoticeCheck = getLastNotificationCheck(context)
        lastNoticeCheck.add(lastUid)
        val setting = context.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val editor = setting.edit()
        editor.putStringSet("notification_check", lastNoticeCheck)
        editor.apply()
    }

    fun getNotificationMessage(context: Context): LinkedHashMap<String?, Any?> {
        val setting = context.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val notificationMap = LinkedHashMap<String?, Any?>()
        try {
            val jsonString = setting.getString("notification", JSONObject().toString())
            val jsonObject = JSONObject(jsonString)
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                notificationMap[key] = jsonObject[key]
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return notificationMap
    }

    fun setNotificationMessage(context: Context, notificationMap: LinkedHashMap<String?, Any?>?) {
        val setting = context.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val jsonObject = JSONObject(notificationMap)
        val keys = jsonObject.keys()
        val editor = setting.edit()
        val notificationMessage = getNotificationMessage(context)
        val key = keys.next()
        try {
            notificationMessage[key] = jsonObject[key]
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val notificationObject = JSONObject(notificationMessage)
        val jsonString = notificationObject.toString()
        editor.putString("notification", jsonString)
        editor.apply()
    }

    fun getNotificationImage(context: Context): LinkedHashMap<String?, Any?> {
        val setting = context.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val notificationMap = LinkedHashMap<String?, Any?>()
        try {
            val jsonString = setting.getString("notification_image", JSONObject().toString())
            val jsonObject = JSONObject(jsonString)
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                notificationMap[key] = jsonObject[key]
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return notificationMap
    }

    fun setNotificationImage(context: Context, notificationMap: LinkedHashMap<String?, Any?>?) {
        val setting = context.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val jsonObject = JSONObject(notificationMap)
        val keys = jsonObject.keys()
        val editor = setting.edit()
        val notificationMessage = getNotificationImage(context)
        val key = keys.next()
        try {
            notificationMessage[key] = jsonObject[key]
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val notificationObject = JSONObject(notificationMessage)
        val jsonString = notificationObject.toString()
        editor.putString("notification_image", jsonString)
        editor.apply()
    }

    fun getNotificationDate(context: Context): LinkedHashMap<String?, Any?> {
        val setting = context.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val notificationMap = LinkedHashMap<String?, Any?>()
        try {
            val jsonString = setting.getString("notification_date", JSONObject().toString())
            val jsonObject = JSONObject(jsonString)
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                notificationMap[key] = jsonObject[key]
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return notificationMap
    }

    fun setNotificationDate(context: Context, notificationMap: LinkedHashMap<String?, Any?>?) {
        val setting = context.getSharedPreferences("setting", Activity.MODE_PRIVATE)
        val jsonObject = JSONObject(notificationMap)
        val keys = jsonObject.keys()
        val editor = setting.edit()
        val notificationMessage = getNotificationDate(context)
        val key = keys.next()
        try {
            notificationMessage[key] = jsonObject[key]
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val notificationObject = JSONObject(notificationMessage)
        val jsonString = notificationObject.toString()
        editor.putString("notification_date", jsonString)
        editor.apply()
    }
}