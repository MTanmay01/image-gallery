package com.mtanmay.imagegallery

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mtanmay.imagegallery.api.API
import com.mtanmay.imagegallery.api.Result
import com.mtanmay.imagegallery.utils.Logger
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "PagingSource"

class PagingSource(
    private val api: API
) : PagingSource<Int, Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {

        val position = params.key ?: 1

        return try {
            val response = api.getPhotos()
            val results = response.photos.photo

            LoadResult.Page(
                data = results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (results.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            Logger.log(TAG, "IOEXCEPTION")
            if (BuildConfig.DEBUG)
                exception.printStackTrace()

            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Logger.log(TAG, "HTTP EXCEPTION")
            if (BuildConfig.DEBUG)
                exception.printStackTrace()

            LoadResult.Error(exception)
        } catch (exception: Exception) {
            if (BuildConfig.DEBUG)
                exception.printStackTrace()

            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? = null

}