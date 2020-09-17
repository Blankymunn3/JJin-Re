package com.jjin_re.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserModel (
    @SerializedName("user_id")
    @Expose
    var userId: String = "",

    @SerializedName("user_passwd")
    @Expose
    var userPW: String = "",

    @SerializedName("user_name")
    @Expose
    var nickName: String = "",

    @SerializedName("user_phone")
    @Expose
    var phone: String = "",

    @SerializedName("user_img")
    @Expose
    var userImg: String = "",

    @SerializedName("user_token")
    @Expose
    var userToken: String ="",

    @SerializedName("user_type")
    @Expose
    var type: Int = 0,

    @SerializedName("social_type")
    @Expose
    var socialType: String = "",

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