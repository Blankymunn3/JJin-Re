package io.kim_kong.jjin_re.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.kim_kong.jjin_re.model.AdBannerModel

data class ADBannerResponse (
    @SerializedName("code")
    @Expose
    var code : String,
    @SerializedName("message")
    @Expose
    var message : String,
    @SerializedName("data")
    @Expose
    var data: ArrayList<AdBannerModel>
)