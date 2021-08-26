package com.mtanmay.imagegallery.api

data class Response(
    val photos: Photo
) {

    data class Photo(
        val photo: List<Result>
    )
}