package com.jjin_re.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReadReviewData (
    @SerializedName("user_id")
    @Expose
    var userID: String,

    @SerializedName("category")
    @Expose
    var category: String = "",

    @SerializedName("uid")
    @Expose
    var uId: String = "",

    @SerializedName("page")
    @Expose
    var page: Int = 1,

    @SerializedName("limit")
    @Expose
    var limit: String = "20",

    @SerializedName("search")
    @Expose
    var search: String = ""
)
