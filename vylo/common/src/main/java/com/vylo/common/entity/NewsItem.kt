package com.vylo.common.entity

data class NewsItem(
    var id: Long? = null,
    var globalId: String? = null,
    var isUserNews: Boolean? = null,
    var status: Int? = null,
    var categoryId: String? = null,
    var categoryName: String? = null,
    var title: String? = null,
    var responseToGlobalId: String? = null,
    var responseToTitle: String? = null,
    var responseToCategoryId: String? = null,
    var responseToCategoryName: String? = null,
    var contentUrl: String? = null,
    var externalLink: String? = null
)