package com.vylo.main.profilefragment.presentation.paging

//class UserProfileNewsDataSource(
//    private val useCase: ProfileUseCase,
//    private val userId: String? = null
//) : PagingSource<String, FeedItem>() {
//
//    override fun getRefreshKey(state: PagingState<String, FeedItem>): String? {
//        return null
//    }
//
//    override val keyReuseSupported: Boolean = true
//
//    override suspend fun load(params: LoadParams<String>): LoadResult<String, FeedItem> {
//        return try {
//            val response = useCase.getUserProfileNews(userId.orEmpty(), params.key).data
//
//            LoadResult.Page(
//                data = response?.results ?: listOf(),
//                prevKey = null,
//                nextKey = getPagingKey(response?.next)
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    private fun getPagingKey(url: String?): String? {
//        return url?.let {
//            Uri.parse(url).getQueryParameter("cursor")
//        } ?: run {
//            null
//        }
//    }
//}