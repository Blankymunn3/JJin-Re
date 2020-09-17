package com.jjin_re.features.alarm

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.MutableLiveData
import com.google.firebase.messaging.FirebaseMessaging
import com.jjin_re.R
import com.jjin_re.data.PushData
import com.jjin_re.databinding.ActivityAlarmsBinding
import com.jjin_re.utils.*

class AlarmsActivity: BaseActivity() {
    val binding by binding<ActivityAlarmsBinding>(R.layout.activity_alarms)
    val viewModel by GetViewModel(AlarmsViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actList.add(this@AlarmsActivity)
        setSupportActionBar(binding.tbAlarms)
        Utils.setIconTintDark(this@AlarmsActivity, true)
        binding.lifecycleOwner = this@AlarmsActivity
        binding.viewModel = viewModel
        viewModel.mktPush.observe(this@AlarmsActivity, {
            if (it == 0) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("mkt_all")
                FirebaseMessaging.getInstance().unsubscribeFromTopic("mkt_android")
                FirebaseMessaging.getInstance().unsubscribeFromTopic("event_all")
                FirebaseMessaging.getInstance().unsubscribeFromTopic("event_android")
            } else {
                FirebaseMessaging.getInstance().subscribeToTopic("mkt_all")
                FirebaseMessaging.getInstance().subscribeToTopic("mkt_android")
                FirebaseMessaging.getInstance().subscribeToTopic("event_all")
                FirebaseMessaging.getInstance().subscribeToTopic("event_android")
            }
        })
        binding.tbAlarms.setNavigationOnClickListener {
            finish()
        }

        viewModel.eventPush.observe(this@AlarmsActivity, {
            viewModel.pushChange()
        })

        viewModel.reviewPush.observe(this@AlarmsActivity, {
            viewModel.pushChange()
        })

        viewModel.mktPush.observe(this@AlarmsActivity, {
            viewModel.pushChange()
        })
    }

    fun onSwitchClick(view: View) {
        when(view.id) {
            R.id.switch_event_push -> setSwitchItemChecked(viewModel.eventPush, binding.switchEventPush)
            R.id.switch_marketing_push -> setSwitchItemChecked(viewModel.mktPush, binding.switchMarketingPush)
            R.id.switch_review_push -> setSwitchItemChecked(viewModel.reviewPush, binding.switchReviewPush)
        }
    }

    private fun setSwitchItemChecked(liveDate: MutableLiveData<Int>, view: SwitchCompat) {
        if (liveDate.value!! == 0) {
            liveDate.postValue(1)
        }
        else {
            liveDate.postValue(0)
        }
        view.isChecked = !view.isChecked
        viewModel.pushChange()
    }

    override fun onPause() {
        super.onPause()
        actList.remove(this@AlarmsActivity)
    }
}