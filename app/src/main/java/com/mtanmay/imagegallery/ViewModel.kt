package com.mtanmay.imagegallery

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
    private val repository: Repository,
) : ViewModel() {

    fun getRecentPhotos(): LiveData<PagingData<Result>>? {

        //check connectivity
        if (!NetworkUtils.isConnected())
            return null
        return repository.getRecentPhotos().cachedIn(viewModelScope)
    }

    fun getSearchPhotos(query: String): LiveData<PagingData<Result>>? {

        //check connectivity
        if(!NetworkUtils.isConnected())
            return null
        return repository.getSearchPhotos(query).cachedIn(viewModelScope)
    }

}