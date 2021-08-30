package com.mtanmay.imagegallery.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result (
    val id: String,
    val url_s: String
) : Parcelable