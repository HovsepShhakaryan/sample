package com.vylo.main.responsemain.upload.domain.livedata

import com.vylo.main.responsemain.upload.domain.entity.response.ColumnNewsData

sealed class NoParameter {
    data class ColumnNews(val columnNewsData: ColumnNewsData) : NoParameter()
    data class NewsRecordGlobalId(val newsRecordGlobalId: String) : NoParameter()
}