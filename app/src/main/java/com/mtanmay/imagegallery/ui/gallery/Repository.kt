package com.mtanmay.imagegallery.ui.gallery

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.mtanmay.imagegallery.PagingSource
import com.mtanmay.imagegallery.api.API
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: API
) {

    fun getPhotos() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PagingSource(api)
            }
        ).liveData

}