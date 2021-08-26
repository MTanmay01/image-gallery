package com.mtanmay.imagegallery.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mtanmay.imagegallery.api.Result
import com.mtanmay.imagegallery.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun getPhotos(): LiveData<PagingData<Result>>? {

        //check connectivity
        if (!NetworkUtils.isConnected()) {
            return null
        }
        return repository.getPhotos().cachedIn(viewModelScope)
    }


}