package com.jjin_re.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NoticeModel {

    @SerializedName("uid")
    @Expose
    lateinit var uId: String

    @SerializedName("title")
    @Expose
    lateinit var title: String

    @SerializedName("created_at")
    @Expose
    lateinit var created_at: String

    @SerializedName("contents")
    @Expose
    lateinit var contents: String
}