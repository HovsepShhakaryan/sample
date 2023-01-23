package com.vylo.main.globalsearchmain.searchnewsstand.presentation.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vylo.common.entity.VideoData
import com.vylo.common.entity.WebData
import com.vylo.common.ext.appendWithBold
import com.vylo.common.util.Converter
import com.vylo.globalsearch.searchnewsstand.domain.SearchNewsstandItem
import com.vylo.main.R
import com.vylo.main.databinding.ItemNewsStandGlobalSearchBinding

class NewsStandAdapter(
    adapterCallBack: AdapterCallBack,
    private val context: Context,
) : RecyclerView.Adapter<NewsStandAdapter.VyloViewHolder>() {

    interface AdapterCallBack {
        fun getNewsstandNews()
    }

    interface SearchResultCallBack {
        fun navigateToProfile(id: String?)
        fun navigateToNews(data: WebData)
        fun navigateToResponse(data: VideoData)
    }

    companion object {
        private var searchResultCallBack: SearchResultCallBack? = null

        fun setSearchResultCallBackListener(searchResultCallBack: SearchResultCallBack) {
            Companion.searchResultCallBack = searchResultCallBack
        }
    }

    private val callBack = adapterCallBack
    private var data = mutableListOf<SearchNewsstandItem>()

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VyloViewHolder {
        val binding = ItemNewsStandGlobalSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VyloViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VyloViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class VyloViewHolder(private val binding: ItemNewsStandGlobalSearchBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchNewsstandItem: SearchNewsstandItem) {
            searchNewsstandItem.publisherImage?.let {
                Picasso.get().load(searchNewsstandItem.publisherImage).into(binding.imageOfReporter)
            }

            searchNewsstandItem.thumbnailUrl?.let {
                Picasso.get().load(searchNewsstandItem.thumbnailUrl).into(binding.imageReportedNews)
            }

            binding.labelFullInfo.text = createFullInfo(searchNewsstandItem)

            searchNewsstandItem.published?.let {
                binding.labelOfTime.text = Converter.timeAgo(searchNewsstandItem.published)
            }

            binding.imageOfReporter.setOnClickListener {
                callBack.getNewsstandNews()
                searchResultCallBack?.navigateToProfile(searchNewsstandItem.publisherId)
            }

            if (!searchNewsstandItem.globalId.isNullOrEmpty() && !searchNewsstandItem.link.isNullOrEmpty()) {
                binding.labelFullInfo.setOnClickListener {
                    callBack.getNewsstandNews()
                    searchResultCallBack?.navigateToNews(
                        WebData(
                            globalId = searchNewsstandItem.globalId,
                            url = searchNewsstandItem.link,
                            title = searchNewsstandItem.title,
                            externalLink = searchNewsstandItem.externalLink,
                            categoryId = searchNewsstandItem.categoryId,
                            categoryName = searchNewsstandItem.categoryName
                        )
                    )
                }

                binding.newsImage.setOnClickListener {
                    callBack.getNewsstandNews()
                    searchResultCallBack?.navigateToNews(
                        WebData(
                            globalId = searchNewsstandItem.globalId,
                            url = searchNewsstandItem.link,
                            title = searchNewsstandItem.title,
                            externalLink = searchNewsstandItem.externalLink,
                            categoryId = searchNewsstandItem.categoryId,
                            categoryName = searchNewsstandItem.categoryName
                        )
                    )
                }
            }

            itemView.setOnClickListener {
                callBack.getNewsstandNews()
            }
        }

        private fun createFullInfo(searchNewsstandItem: SearchNewsstandItem): Spannable {
            val byText = SpannableString(context.getString(R.string.label_by))
            byText.setSpan(ForegroundColorSpan(context.getColor(R.color.gray)), 0, byText.length, 0)

//            val verifiedProfileImage = object : ImageSpan(context, R.drawable.ic_verified_profile) {
//                override fun draw(
//                    canvas: Canvas,
//                    text: CharSequence?,
//                    start: Int,
//                    end: Int,
//                    x: Float,
//                    top: Int,
//                    y: Int,
//                    bottom: Int,
//                    paint: Paint
//                ) {
//                    val b = drawable
//                    canvas.save()
//                    canvas.translate(x, (y - 12.dpToPx(context)).toFloat())
//                    b.draw(canvas)
//                    canvas.restore()
//                }
//            }

            val ssb = SpannableStringBuilder()
            ssb.appendWithBold(searchNewsstandItem.title)
            ssb.append(" ")
            ssb.append(byText)
            ssb.append(" ")
            ssb.appendWithBold(searchNewsstandItem.publisherName)
            if (searchNewsstandItem.publisherName != null && searchNewsstandItem.publisherName.isNotEmpty())
                ssb.append("  ")
            else ssb.append(" ")
//            if (searchNewsstandItem.publisherIsApproved != null && searchNewsstandItem.publisherIsApproved)
//                ssb.setSpan(verifiedProfileImage, ssb.length - 1, ssb.length, 0)

            return ssb
        }
    }

    fun setData(loads: List<SearchNewsstandItem>) {
        data.addAll(loads)
        notifyDataSetChanged()
    }

    fun getData() = data

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }
}