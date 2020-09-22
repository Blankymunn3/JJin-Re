package com.jjin_re.utils

import android.graphics.Bitmap

class Photo(
    var photo: Bitmap?,
    var isClickable: Boolean,
    var isDelActive: Boolean
){
    override fun equals(other: Any?): Boolean {
        (other as? Photo)?.let {
            return false
        } ?: return super.equals(other)}

}