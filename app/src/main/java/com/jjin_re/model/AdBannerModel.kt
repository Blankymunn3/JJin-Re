package com.jjin_re.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdBannerModel (
    @SerializedName("img_uri")
    @Expose
    var imgUri: String,

    @SerializedName("url")
    @Expose
    var url: String
)