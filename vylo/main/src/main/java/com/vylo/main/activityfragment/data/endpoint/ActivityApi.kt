package com.vylo.main.activityfragment.data.endpoint

import com.vylo.main.activityfragment.domain.entity.response.ActivityData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ActivityApi {

    @GET(ACTIVITY)
    suspend fun getActivity(
        @Query("page")
        cursor: Int?
    ): Response<ActivityData>

    companion object {
        private const val ACTIVITY = "activity"
    }
}