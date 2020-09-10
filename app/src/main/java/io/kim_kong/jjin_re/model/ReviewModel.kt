package io.kim_kong.jjin_re.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewModel(
    @SerializedName("user_id")
    @Expose
    var userId: String,

    @SerializedName("user_name")
    @Expose
    var userName: String,

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
    var rating: String,

    @SerializedName("like_cnt")
    @Expose
    var likeCnt: String,

    @SerializedName("angry_cnt")
    @Expose
    var angryCnt: String,

    @SerializedName("created_at")
    @Expose
    var createdAt: String
)