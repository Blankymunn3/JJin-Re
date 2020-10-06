package com.jjin_re.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PushData(
    @SerializedName("user_id")
    @Expose
    var userID: String = "",

    @SerializedName("review_push")
    @Expose
    var reviewPush: Int = 0,

    @SerializedName("mkt_push")
    @Expose
    var mktPush: Int = 0,

    @SerializedName("event_push")
    @Expose
    var eventPush: Int = 0
)