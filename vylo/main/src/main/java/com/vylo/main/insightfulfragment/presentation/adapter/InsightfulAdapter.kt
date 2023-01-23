package com.vylo.main.insightfulfragment.presentation.adapter

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
import com.vylo.common.ext.toGone
import com.vylo.common.ext.toVisible
import com.vylo.common.util.Converter
import com.vylo.common.util.enums.VideoType
import com.vylo.main.R
import com.vylo.main.databinding.ItemInsightfulBinding
import com.vylo.main.insightfulfragment.domain.entity.response.InsightfulItem

class InsightfulAdapter(
    private val onUserClick: (String) -> Unit,
    private val onNewsClick: (VideoData) -> Unit
) : RecyclerView.Adapter<InsightfulAdapter.InsightfulViewHolder>() {

    private val insightfulList = mutableListOf<InsightfulItem>()
    private var isRefresh = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsightfulViewHolder {
        return InsightfulViewHolder.create(parent, onUserClick, onNewsClick)
    }

    override fun onBindViewHolder(holder: InsightfulViewHolder, position: Int) {
        holder.bind(insightfulList[position])
    }

    override fun getItemCount() = insightfulList.size

    fun submitList(list: List<InsightfulItem>) {
        insightfulList.clear()
        insightfulList.addAll(list)
        notifyDataSetChanged()
    }

    fun refreshData() {
        insightfulList.clear()
        notifyDataSetChanged()
    }

    class InsightfulViewHolder(
        private val binding: ItemInsightfulBinding,
        private val onUserClick: (String) -> Unit,
        private val onNewsClick: (VideoData) -> Unit,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InsightfulItem) {
            binding.apply {
                Picasso.get().load(item.publisherImage)
                    .placeholder(R.drawable.ic_empty_profileimage)
                    .into(imageOfReporter)

                item.published?.let {
                    binding.labelOfTime.text = Converter.timeAgo(it)
                }

                labelFullInfo.text = createFullInfo(item)

                item.publisherGlobalId?.let { id ->
                    containerOfReporterAvatar.setOnClickListener { onUserClick(id) }
                    labelFullInfo.setOnClickListener { onUserClick(id) }
                    labelOfTime.setOnClickListener { onUserClick(id) }
                }

                item.originGlobalId?.let { id ->
                    newsImage.toVisible()
                    Picasso.get().load(item.originThumbnailUrl)
                        .placeholder(R.drawable.ic_empty_nopost)
                        .into(imageReportedNews)
                    newsImage.setOnClickListener {
                        onNewsClick(VideoData(id, VideoType.NEWS))
                    }
                } ?: run {
                    newsImage.toGone()
                }
            }
        }

        private fun createFullInfo(item: InsightfulItem): Spannable {
            val responseToText = SpannableString(context.getString(R.string.label_responded_to))
            responseToText.setSpan(
                ForegroundColorSpan(context.getColor(R.color.gray)),
                0,
                responseToText.length,
                0
            )
            val byText = SpannableString(context.getString(R.string.label_by))
            byText.setSpan(ForegroundColorSpan(context.getColor(R.color.gray)), 0, byText.length, 0)

            val ssb = SpannableStringBuilder()
            if (item.typeEvent == 4) {
                ssb.append(item.publisherUsername)
                ssb.append("  ")
//                if (item.publisherIsApproved == true) {
//                    ssb.setSpan(
//                        ImageSpan(context, R.drawable.ic_verified_profile),
//                        ssb.length - 1,
//                        ssb.length,
//                        0
//                    )
//                    ssb.append("  ")
//                }
                ssb.append(responseToText)
                ssb.append("  ")
                ssb.append(item.originTitle)
                ssb.append("  ")
                ssb.append(byText)
                ssb.append("  ")
                ssb.append(item.originPublisherUsername)
                ssb.append("  ")
//                if (item.responseTo.publisherIsApproved != null && item.responseTo.publisherIsApproved == "True")
//                    ssb.setSpan(ImageSpan(context, R.drawable.ic_verified_profile), ssb.length - 1, ssb.length, 0)
            } else if (item.typeEvent == 5) {
                ssb.append(item.title)
                ssb.append("  ")
                ssb.append(byText)
                ssb.append("  ")
                ssb.append(item.publisherUsername)
                ssb.append("  ")
//                if (item.publisherIsApproved == true)
//                    ssb.setSpan(ImageSpan(context, R.drawable.ic_verified_profile), ssb.length - 1, ssb.length, 0)
            }

            return ssb
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onUserClick: (String) -> Unit,
                onNewsClick: (VideoData) -> Unit
            ): InsightfulViewHolder {
                return LayoutInflater.from(parent.context).let {
                    InsightfulViewHolder(
                        ItemInsightfulBinding.inflate(it, parent, false),
                        onUserClick,
                        onNewsClick,
                        parent.context
                    )
                }
            }
        }
    }
}