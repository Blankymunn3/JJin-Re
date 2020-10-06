package com.jjin_re.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EditReviewData(
    @SerializedName("uid")
    @Expose
    var uId: String,

    @SerializedName("user_id")
    @Expose
    var userID: String,

    @SerializedName("user_name")
    @Expose
    var userName:String,

    @SerializedName("product_name")
    @Expose
    var productName: String,

    @SerializedName("contents")
    @Expose
    var contents: String,

    @SerializedName("img_url")
    @Expose
    var imgUrl: String,

    @SerializedName("category")
    @Expose
    var category: String,

    @SerializedName("category_etc")
    @Expose
    var categoryEtc: String,

    @SerializedName("rating")
    @Expose
    var rating: String
)