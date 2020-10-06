package com.jjin_re.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jjin_re.model.NoticeModel

data class NoticeResponse(

    @SerializedName("code")
    @Expose
    var code: String = "",

    @SerializedName("data")
    @Expose
    var data: List<NoticeModel> = emptyList()
)