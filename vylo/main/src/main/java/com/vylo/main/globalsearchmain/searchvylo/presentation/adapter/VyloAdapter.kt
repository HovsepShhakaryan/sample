package com.vylo.main.globalsearchmain.searchvylo.presentation.adapter

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
import com.vylo.common.util.enums.VideoType
import com.vylo.globalsearch.searchvylo.domain.entity.response.SearchVyloItem
import com.vylo.main.R
import com.vylo.main.databinding.ItemVyloBinding


class VyloAdapter(
    adapterCallBack: AdapterCallBack,
    private val context: Context,
) : RecyclerView.Adapter<VyloAdapter.VyloViewHolder>() {

    interface AdapterCallBack {
        fun getVyloNews()
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
    private var data = mutableListOf<SearchVyloItem>()

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VyloViewHolder {
        val binding = ItemVyloBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VyloViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VyloViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class VyloViewHolder(private val binding: ItemVyloBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchVyloItem: SearchVyloItem) {
            searchVyloItem.publisherImage?.let {
                Picasso.get().load(searchVyloItem.publisherImage).into(binding.imageOfReporter)
            }

            searchVyloItem.thumbnailUrl?.let {
                Picasso.get().load(searchVyloItem.thumbnailUrl).into(binding.imageReportedNews)
            }

            binding.labelFullInfo.text = createFullInfo(searchVyloItem)

            searchVyloItem.published?.let {
                binding.labelOfTime.text = Converter.timeAgo(searchVyloItem.published)
            }

            itemView.setOnClickListener {
                callBack.getVyloNews()
            }

            binding.imageOfReporter.setOnClickListener {
                callBack.getVyloNews()
                searchResultCallBack?.navigateToProfile(searchVyloItem.publisherId)
            }

            if (!searchVyloItem.globalId.isNullOrEmpty() && !searchVyloItem.link.isNullOrEmpty()) {
                binding.labelFullInfo.setOnClickListener {
                    callBack.getVyloNews()
                    searchResultCallBack?.navigateToNews(
                        WebData(
                            globalId = searchVyloItem.globalId,
                            url = searchVyloItem.link,
                            title = searchVyloItem.title,
                            externalLink = searchVyloItem.externalLink,
                            categoryId = searchVyloItem.categoryId,
                            categoryName = searchVyloItem.categoryName
                        )
                    )
                }

                binding.newsImage.setOnClickListener {
                    callBack.getVyloNews()
                    searchResultCallBack?.navigateToNews(
                        WebData(
                            globalId = searchVyloItem.globalId,
                            url = searchVyloItem.link,
                            title = searchVyloItem.title,
                            externalLink = searchVyloItem.externalLink,
                            categoryId = searchVyloItem.categoryId,
                            categoryName = searchVyloItem.categoryName
                        )
                    )
                }
            }

            if (!searchVyloItem.globalId.isNullOrEmpty() && !searchVyloItem.contentUrl.isNullOrEmpty()) {
                binding.labelFullInfo.setOnClickListener {
                    callBack.getVyloNews()
                    searchResultCallBack?.navigateToResponse(
                        VideoData(
                            globalId = searchVyloItem.globalId,
                            videoType = VideoType.NEWS
                        )
                    )
                }

                binding.newsImage.setOnClickListener {
                    callBack.getVyloNews()
                    searchResultCallBack?.navigateToResponse(
                        VideoData(
                            globalId = searchVyloItem.globalId,
                            videoType = VideoType.NEWS
                        )
                    )
                }
            }
        }

        private fun createFullInfo(searchVyloItem: SearchVyloItem): Spannable {
            val responseToText = SpannableString(context.getString(R.string.label_responded_to))
            responseToText.setSpan(
                ForegroundColorSpan(context.getColor(R.color.gray)),
                0,
                responseToText.length,
                0
            )
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
            if (searchVyloItem.responseTo != null) {
                ssb.appendWithBold(searchVyloItem.publisherName)
                ssb.append("  ")
//                if (searchVyloItem.publisherIsApproved == true)
//                    ssb.setSpan(verifiedProfileImage, ssb.length - 1, ssb.length, 0)
                ssb.append("  ")
                ssb.append(responseToText)
                ssb.append("  ")
                ssb.appendWithBold(searchVyloItem.responseTo.title)
                ssb.append("  ")
                ssb.append(byText)
                ssb.append("  ")
                ssb.appendWithBold(searchVyloItem.responseTo.publisherName)
                ssb.append("  ")
//                if (searchVyloItem.responseTo.publisherIsApproved != null && searchVyloItem.responseTo.publisherIsApproved == "True")
//                    ssb.setSpan(verifiedProfileImage, ssb.length - 1, ssb.length, 0)
            } else {
                ssb.appendWithBold(searchVyloItem.title)
                ssb.append("  ")
                ssb.append(byText)
                ssb.append("  ")
                ssb.appendWithBold(searchVyloItem.publisherName)
                ssb.append("  ")
//                if (searchVyloItem.publisherIsApproved == true)
//                    ssb.setSpan(verifiedProfileImage, ssb.length - 1, ssb.length, 0)
            }

            return ssb
        }
    }

    fun setData(loads: List<SearchVyloItem>) {
        data.addAll(loads)
        notifyDataSetChanged()
    }

    fun getData() = data

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }
}