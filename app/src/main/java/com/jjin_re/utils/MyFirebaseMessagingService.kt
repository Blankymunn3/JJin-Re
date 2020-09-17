package com.jjin_re.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gun0912.tedpermission.util.ObjectUtils
import com.jjin_re.R
import com.jjin_re.features.main.MainActivity
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutionException


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var notifManager: NotificationManager? = null
    var mChannel: NotificationChannel? = null
    var TAG = "click"
    private val context: Context? = null
    private val count = 0

    @SuppressLint("InvalidWakeLockTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        BaseApplication.userModel = SharedPreferenceHelper.getUserDataToSharedPreference(applicationContext)
        if (wakeLock != null) {
            return
        }
        val pm = this.getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, ""
        )
        wakeLock!!.acquire(5000)
        if (wakeLock != null) {
            wakeLock!!.release()
            wakeLock = null
        }
        if (remoteMessage.notification != null) Log.d(
            "notification body : ", (remoteMessage.notification!!
                .body)!!
        )
        if (remoteMessage.data.size > 0) {
            if (true) {
                scheduleJob()
            } else {
                handleNow()
            }
            val data = remoteMessage.data
            val title = data["title"]
            val message = data["message"]
            val deepLink = data["linkurl"]
            val imgUrl = data["image"]
            showNotification(message, title, deepLink, imgUrl)
        }
        updateIconBadgeCount(this)
    }

    private fun scheduleJob() {
        val work: OneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance().beginWith(work).enqueue()
    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    fun updateIconBadgeCount(context: Context) {
        val mPref = PreferenceManager.getDefaultSharedPreferences(context)
        val callValue = mPref.getInt("badge_count", 0)
        var count = callValue
        ++count
        Log.d("count2", "" + count)
        Log.d("test", "updateIconBadgeCount")
        val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
        intent.putExtra("badge_count", count)
        intent.putExtra("badge_count_package_name", context.packageName)
        intent.putExtra("badge_count_class_name", getLauncherClassName(context))
        application.sendBroadcast(intent)
    }

    private fun getLauncherClassName(context: Context): String {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setPackage(packageName)
        val resolveInfoList: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
        return if (resolveInfoList != null && resolveInfoList.size > 0) {
            resolveInfoList.get(0).activityInfo.name
        } else ""
    }

    private fun showNotification(message: String?, title: String?, url: String?, imgUrl: String?) {
        var message = message
        var title = title
        var url = url
        if (message == null) message = ""
        if (title == null) title = "찐리 - 진짜리뷰"
        Log.e("push_message", message)
        Log.e("push_title", title)
        if (url == null) {
            url = ""
        }
        Log.e("push_url", url)
        if (message != "티켓확인") {
            val intent = Intent(this, MainActivity::class.java)
            val bundle = Bundle()
            intent.putExtra("deeplink", url)
            intent.putExtra("EXTRA_TEST_BODY", message)
            intent.putExtra("EXTRA_TEST_TITLE", title)
            intent.putExtra("EXTRA_CALL_DEEP_LINK", true)
            intent.putExtras(bundle)
            intent.addFlags(
                (Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
            val builder: NotificationCompat.Builder
            if (notifManager == null) {
                notifManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            val pendingIntent = PendingIntent.getActivity(
                this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT
            )
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                if (mChannel == null) {
                    val mChannel = NotificationChannel("0", title, importance)
                    mChannel.description = message
                    mChannel.enableVibration(true)
                    mChannel.vibrationPattern =
                        longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                    // mChannel.setShowBadge(true);
                    notifManager!!.createNotificationChannel(mChannel)
                }
                builder = NotificationCompat.Builder(this, "0")
                builder.setContentTitle(title) // flare_icon_30
                    .setSmallIcon(R.mipmap.ic_launcher) // required
                    .setContentText(message) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(message)
                    ) // .setBadgeIconType(1)
                    // .setNumber(22)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(
                        longArrayOf(
                            100, 200, 300, 400,
                            500, 400, 300, 200, 400
                        )
                    )
            } else {
                builder = NotificationCompat.Builder(this)
                builder.setContentTitle(title)
                    .setColor(-0x111112)
                    .setLights(173, 500, 2000)
                    .setSound(defaultSoundUri)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(message)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(message)
                    )
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(
                        longArrayOf(
                            100, 200, 300, 400, 500,
                            400, 300, 200, 400
                        )
                    ).priority = Notification.PRIORITY_HIGH
            }
            try {
                val img = URL(imgUrl)
                val glideImage = Glide.with(this).asBitmap().load(imgUrl).submit()
                val bitmap = glideImage.get()
                val bigPicture = BitmapFactory.decodeStream(img.openConnection().getInputStream())
                builder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bigPicture)
                        .bigLargeIcon(bigPicture)
                        .setBigContentTitle(title)
                        .setSummaryText(message)
                )
                    .setLargeIcon(bitmap)
                Glide.with(this).clear(glideImage)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
            val notification = builder.build()
            notifManager!!.notify(0, notification)
            if (!ObjectUtils.isEmpty(message)) {
                val dt = Date()
                val fullSdf = SimpleDateFormat("yyyy년MM월dd일 HH시mm분", Locale.getDefault())
                Log.d("DATE", fullSdf.format(dt))
                val notiTitle: String =
                    when (title) {
                    "리뷰 좋아요" -> fullSdf.format(dt) + "에 좋아요 눌렸어요"
                    "리뷰 싫어요" -> fullSdf.format(dt) + "에 싫어요 눌렸어요"
                    else -> fullSdf.format(dt) + "에 발송된 알림"
                }
                val notificationMap = LinkedHashMap<String?, Any?>()
                val notificationImageMap = LinkedHashMap<String?, Any?>()
                val notificationDateMap = LinkedHashMap<String?, Any?>()
                notificationMap[notiTitle] = message
                notificationImageMap[notiTitle] = imgUrl
                notificationDateMap[notiTitle] = fullSdf.format(dt)
                SharedPreferenceHelper.setNotificationMessage(applicationContext, notificationMap)
                SharedPreferenceHelper.setNotificationImage(
                    applicationContext,
                    notificationImageMap
                )
                SharedPreferenceHelper.setNotificationDate(applicationContext, notificationDateMap)
            }
        }
    }

    companion object {
        private var wakeLock: PowerManager.WakeLock? = null
    }
}
