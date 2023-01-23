package com.vylo.main.activityfragment.presentation.adapter

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
import com.vylo.common.ext.toGone
import com.vylo.common.ext.toInvisible
import com.vylo.common.ext.toVisible
import com.vylo.common.util.Converter
import com.vylo.common.util.enums.VideoType
import com.vylo.main.R
import com.vylo.main.activityfragment.domain.entity.response.ActivityItem
import com.vylo.main.databinding.ItemActivityBinding

class ActivityAdapter(
    private val onFollowClick: (String, Boolean) -> Unit,
    private val onUserClick: (String) -> Unit,
    private val onVideoClick: (VideoData) -> Unit,
    private val onWebClick: (WebData) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    private val activityList = mutableListOf<ActivityItem>()
    private val followingIds = mutableListOf<String?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        return ActivityViewHolder.create(
            parent,
            onFollowClick,
            onUserClick,
            onVideoClick,
            onWebClick
        )
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val isFollowing = followingIds.binarySearch(activityList[position].publisherGlobalId) >= 0
        holder.bind(activityList[position], isFollowing)
    }

    override fun getItemCount() = activityList.size

    fun submitFollowingIds(list: List<String?>) {
        followingIds.addAll(list)
        notifyDataSetChanged()
    }

    fun submitList(list: List<ActivityItem>) {
        activityList.addAll(list)
        notifyDataSetChanged()
    }

    fun refreshData() {
        activityList.clear()
        followingIds.clear()
        notifyDataSetChanged()
    }

    fun removeActivity(id: String) {
        val index = activityList.indexOfFirst { it.newsGlobalId == id }
        activityList.removeAt(index)
        notifyItemRemoved(index)
    }

    fun getData() = activityList

    class ActivityViewHolder(
        private val binding: ItemActivityBinding,
        private val onFollowClick: (String, Boolean) -> Unit,
        private val onUserClick: (String) -> Unit,
        private val onNewsClick: (VideoData) -> Unit,
        private val onWebClick: (WebData) -> Unit,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        private var isFollow = false

        fun bind(item: ActivityItem, isUserFollowing: Boolean) {
            binding.apply {
                Picasso.get()
                    .load(item.publisherImage)
                    .placeholder(R.drawable.ic_empty_profileimage)
                    .into(imageOfReporter)

                labelFullInfo.text = createFullInfo(item)

                item.createdAt?.let {
                    labelOfTime.text = Converter.timeAgo(it)
                }

                isFollow = isUserFollowing

                when (item.typeActivity) {
                    4 -> {
                        buttonFollowing.toGone()
                        item.thumbnailUrl?.let {
                            newsImage.toVisible()

                            Picasso.get()
                                .load(it)
                                .placeholder(R.drawable.ic_empty_nopost)
                                .into(imageReportedNews)
                        }

                        item.publisherGlobalId?.let { id ->
                            containerOfReporterAvatar.setOnClickListener { onUserClick(id) }
                        }

                        if (!item.link.isNullOrEmpty()) {
                            item.link.let { link ->
                                labelFullInfo.setOnClickListener {
                                    onWebClick(WebData(url = link))
                                }
                                labelOfTime.setOnClickListener {
                                    onWebClick(WebData(url = link))
                                }
                                newsImage.setOnClickListener {
                                    onWebClick(WebData(url = link))
                                }
                            }
                        } else if (!item.newsGlobalId.isNullOrEmpty()) {
                            item.newsGlobalId.let { id ->
                                labelFullInfo.setOnClickListener {
                                    onNewsClick(VideoData(id, VideoType.NEWS))
                                }
                                labelOfTime.setOnClickListener {
                                    onNewsClick(VideoData(id, VideoType.NEWS))
                                }
                                newsImage.setOnClickListener {
                                    onNewsClick(VideoData(id, VideoType.NEWS))
                                }
                            }
                        }
                    }
                    5 -> {
                        newsImage.toInvisible()
                        buttonFollowing.toGone()
                        item.thumbnailUrl?.let {
                            newsImage.toVisible()

                            Picasso.get()
                                .load(it)
                                .placeholder(R.drawable.ic_empty_nopost)
                                .into(imageReportedNews)
                        }

                        item.publisherGlobalId?.let { id ->
                            containerOfReporterAvatar.setOnClickListener { onUserClick(id) }
                            labelFullInfo.setOnClickListener { onUserClick(id) }
                            labelOfTime.setOnClickListener { onUserClick(id) }
                        }

                        if (!item.link.isNullOrEmpty()) {
                            item.link.let { link ->
                                labelFullInfo.setOnClickListener {
                                    onWebClick(WebData(globalId = item.newsGlobalId, url = link))
                                }
                                labelOfTime.setOnClickListener {
                                    onWebClick(WebData(globalId = item.newsGlobalId, url = link))
                                }
                                newsImage.setOnClickListener {
                                    onWebClick(WebData(globalId = item.newsGlobalId, url = link))
                                }
                            }
                        } else if (!item.newsGlobalId.isNullOrEmpty()) {
                            item.newsGlobalId.let { id ->
                                labelFullInfo.setOnClickListener {
                                    onNewsClick(VideoData(id, VideoType.NEWS))
                                }
                                labelOfTime.setOnClickListener {
                                    onNewsClick(VideoData(id, VideoType.NEWS))
                                }
                                newsImage.setOnClickListener {
                                    onNewsClick(VideoData(id, VideoType.NEWS))
                                }
                            }
                        }
                    }
                    6 -> {
                        buttonFollowing.toVisible()
                        newsImage.toInvisible()
                        setFollowButtonText(isFollow)

                        item.publisherGlobalId?.let { id ->
                            containerOfReporterAvatar.setOnClickListener { onUserClick(id) }
                            labelFullInfo.setOnClickListener { onUserClick(id) }
                            labelOfTime.setOnClickListener { onUserClick(id) }

                            buttonFollowing.clickOnButton {
                                isFollow = !isFollow
                                setFollowButtonText(isFollow)
                                onFollowClick(id, isFollow)
                            }
                        }

                    }
                }
            }
        }

        private fun createFullInfo(item: ActivityItem): Spannable {
            val responseToText = SpannableString(context.getString(R.string.label_responded_to))
            responseToText.setSpan(
                ForegroundColorSpan(context.getColor(R.color.gray)),
                0,
                responseToText.length,
                0
            )

            val byText = SpannableString(context.getString(R.string.label_by))
            byText.setSpan(ForegroundColorSpan(context.getColor(R.color.gray)), 0, byText.length, 0)

            val uploadedText = SpannableString(context.getString(R.string.text_uploaded))
            uploadedText.setSpan(
                ForegroundColorSpan(context.getColor(R.color.gray)),
                0,
                uploadedText.length,
                0
            )

            val startedFollowing = SpannableString(context.getString(R.string.text_following))
            startedFollowing.setSpan(
                ForegroundColorSpan(context.getColor(R.color.gray)),
                0,
                startedFollowing.length,
                0
            )

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
//
//            val verifiedOriginProfileImage =
//                object : ImageSpan(context, R.drawable.ic_verified_profile) {
//                    override fun draw(
//                        canvas: Canvas,
//                        text: CharSequence?,
//                        start: Int,
//                        end: Int,
//                        x: Float,
//                        top: Int,
//                        y: Int,
//                        bottom: Int,
//                        paint: Paint
//                    ) {
//                        val b = drawable
//                        canvas.save()
//                        canvas.translate(x, (y - 12.dpToPx(context)).toFloat())
//                        b.draw(canvas)
//                        canvas.restore()
//                    }
//                }

            val ssb = SpannableStringBuilder()
            if (item.typeActivity == 4) {
                ssb.appendWithBold(item.getPublisherUserName())
                ssb.append(" ")
//                if (item.publisherIsApproved == true) {
//                    ssb.append(" ")
//                    ssb.setSpan(verifiedProfileImage, ssb.length - 1, ssb.length, 0)
//                    ssb.append(" ")
//                }
                ssb.append(responseToText)
                ssb.append(" ")
                ssb.appendWithBold(item.originTitle)
                ssb.append(" ")
                ssb.append(byText)
                ssb.append(" ")
                ssb.appendWithBold(item.getOriginalPublisherUserName())
                ssb.append(" ")
//                if (item.originPublisherIsApproved == true) {
//                    ssb.append(" ")
//                    ssb.setSpan(verifiedOriginProfileImage, ssb.length - 1, ssb.length, 0)
//                }
            } else if (item.typeActivity == 5) {
                ssb.appendWithBold(item.getPublisherUserName())
                ssb.append(" ")
                ssb.append(uploadedText)
                ssb.append(" ")
                ssb.appendWithBold(item.title)
//                if (item.publisherIsApproved == true) {
//                    ssb.append(" ")
//                    ssb.append(" ")
//                    ssb.setSpan(verifiedProfileImage, ssb.length - 1, ssb.length, 0)
//                }
            } else if (item.typeActivity == 6) {
                ssb.appendWithBold(item.getPublisherUserName())
                ssb.append(" ")
//                if (item.publisherIsApproved == true) {
//                    ssb.append(" ")
//                    ssb.setSpan(verifiedProfileImage, ssb.length - 1, ssb.length, 0)
//                    ssb.append(" ")
//                }
                ssb.append(startedFollowing)
            }
            ssb.append(" ")

            return ssb
        }

        private fun setFollowButtonText(isFollower: Boolean) {
            binding.buttonFollowing.apply {
                if (isFollower) {
                    setShape(com.vylo.common.R.drawable.shape_white_border)
                    setStyle(com.vylo.common.R.style.MainText_H8_1)
                    setTitle(resources.getString(com.vylo.common.R.string.button_following))
                } else {
                    setShape(com.vylo.common.R.drawable.shape_white_without_border)
                    setStyle(com.vylo.common.R.style.MainText_H8_1_Dark)
                    setTitle(resources.getString(com.vylo.common.R.string.button_follow))
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onFollowClick: (String, Boolean) -> Unit,
                onUserClick: (String) -> Unit,
                onNewsClick: (VideoData) -> Unit,
                onWebClick: (WebData) -> Unit
            ): ActivityViewHolder {
                return LayoutInflater.from(parent.context).let {
                    ActivityViewHolder(
                        ItemActivityBinding.inflate(it, parent, false),
                        onFollowClick,
                        onUserClick,
                        onNewsClick,
                        onWebClick,
                        parent.context
                    )
                }
            }
        }
    }
}