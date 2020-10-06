package com.jjin_re.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jjin_re.model.ReviewModel

data class DownloadReviewListResponse(
    @SerializedName("code")
    @Expose
    var code : String,
    @SerializedName("message")
    @Expose
    var message : String,
    @SerializedName("data")
    @Expose
    var data: ArrayList<ReviewModel>
)