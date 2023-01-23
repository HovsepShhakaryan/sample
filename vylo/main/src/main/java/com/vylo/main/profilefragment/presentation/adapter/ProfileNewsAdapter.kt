package com.vylo.main.profilefragment.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vylo.common.entity.VideoData
import com.vylo.common.entity.WebData
import com.vylo.common.ext.*
import com.vylo.common.util.Converter
import com.vylo.common.util.enums.VideoType
import com.vylo.common.widget.MainButton
import com.vylo.main.R
import com.vylo.main.component.events.domain.entity.request.TimingModelProfile
import com.vylo.main.profilefragment.domain.entity.response.ProfileNewsItem
import java.util.*

class ProfileNewsAdapter(
    context: Context,
    private val onVideoClick: ((VideoData, ProfileNewsItem) -> Unit)? = null,
    private val onNewsClick: ((WebData, ProfileNewsItem) -> Unit)? = null,
    private val onKebabClick: (ProfileNewsItem) -> Unit,
    private val onRespondClick: (String?, String?, String?, String?) -> Unit,
    private val onResponsesClick: ((VideoData) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val newsList = mutableListOf<ProfileNewsItem>()
    private val viewTypeNews = 1
    private val viewTypeResponse = 2
    private val ctx = context
    private val timingList = mutableListOf<TimingModelProfile>()

    override fun getItemViewType(position: Int): Int {
        if (newsList[position].responseTo == null)
            return viewTypeNews
        return viewTypeResponse
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == viewTypeNews) {
            return NewsViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_feed_news, parent, false),
                onKebabClick
            )
        }
        return ResponseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_feed_report, parent, false),
            onKebabClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = newsList[position]
        when (holder) {
            is NewsViewHolder -> holder.bind(item, ctx)
            is ResponseViewHolder -> holder.bind(item)
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        val currentTime = (Calendar.getInstance().timeInMillis / 1000).toInt()
        timingList.add(
            TimingModelProfile(
                holder.layoutPosition,
                currentTime,
                null,
                when (holder) {
                    is NewsViewHolder -> holder.getFeedItem()
                    is ResponseViewHolder -> holder.getFeedItem()
                    else -> null
                }
            )
        )
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        val currentTime = (Calendar.getInstance().timeInMillis / 1000).toInt()
        val timeModelList = mutableListOf<TimingModelProfile>()
        timeModelList.addAll(timingList)
        for (item in timeModelList) {
            if (item.position == holder.layoutPosition) {
                val newModel = TimingModelProfile(
                    position = item.position,
                    addedTime = item.addedTime,
                    destroyedTime = currentTime,
                    item = item.item
                )
                timingList.remove(item)
                timingList.add(newModel)
            }
        }
    }

    fun getTimingList() = timingList

    internal inner class NewsViewHolder(itemView: View, private val onKebabClick: (ProfileNewsItem) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val imageOfReporter = itemView.findViewById<ImageView>(R.id.image_of_reporter)
        private val labelNameOfReporter = itemView.findViewById<TextView>(R.id.label_name)
        private val labelNicknameOfReporter = itemView.findViewById<TextView>(R.id.label_subname)
        private val labelOfTiming = itemView.findViewById<TextView>(R.id.label_of_timing)
        private val imageOfVideoNews = itemView.findViewById<ImageView>(R.id.image_of_video_news)
        private val imageOfAudioNews = itemView.findViewById<ImageView>(R.id.image_of_audio_news)
        private val imageContainer = itemView.findViewById<ConstraintLayout>(R.id.image_container)
        private val imageVverified = itemView.findViewById<ImageView>(R.id.image_verified)
        private val imageMmenu = itemView.findViewById<ImageView>(R.id.image_menu)
        private val labelOfNews = itemView.findViewById<TextView>(R.id.label_of_news)
        private val buttonRespond = itemView.findViewById<MainButton>(R.id.button_respond)
        private val buttonResponses = itemView.findViewById<MainButton>(R.id.button_responses)
        private val labelOfTime = itemView.findViewById<TextView>(R.id.label_of_time)
        private val layoutOfPrivate = itemView.findViewById<ConstraintLayout>(R.id.layout_private)
        private val layoutOfPrivateAudio = itemView.findViewById<ConstraintLayout>(R.id.layout_private_audio)
        private val labelOfTimeAudio = itemView.findViewById<TextView>(R.id.label_of_time_audio)
        private val containerOfAudio = itemView.findViewById<ConstraintLayout>(R.id.container_of_audio)
        private val layoutSound = itemView.findViewById<ConstraintLayout>(R.id.layout_sound_x)
        private var feedItem: ProfileNewsItem? = null

        fun getFeedItem() = feedItem

        fun bind(feedItem: ProfileNewsItem, context: Context) {

            this.feedItem = feedItem

            buttonRespond.squareRoundedWhiteButtonStyle(context, context.resources.getString(R.string.label_respond))
            buttonRespond.setOnClickListener {
                onRespondClick(
                    feedItem.globalId,
                    feedItem.title,
                    feedItem.categoryId,
                    feedItem.categoryName
                )
            }

            buttonResponses.squareRoundedGrayButtonStyle(context, context.resources.getString(R.string.label_respond))
            feedItem.globalId?.let { id ->
                if (feedItem.responseCounter.orZero() > 0) {
                    buttonResponses.setOnClickListener {
                        onResponsesClick?.let {
                            it(VideoData(id, videoType = VideoType.RESPONSES))
                        }
                    }
                }
            }

            feedItem.publisherImage?.let {
                Picasso.get().load(feedItem.publisherImage).into(imageOfReporter)
            }

            labelNameOfReporter.text = feedItem.publisherName
            labelNicknameOfReporter.text = feedItem.getUserName()
            feedItem.published?.let {
                labelOfTiming.text = Converter.timeAgo(feedItem.published!!)
            }
            if (feedItem.contentType == "mp3") {
                layoutSound.toGone()
                feedItem.thumbnailUrl?.let {
                    Picasso.get().load(feedItem.thumbnailUrl).into(imageOfAudioNews)
                }
                containerOfAudio.toVisible()
                imageOfAudioNews.toVisible()
                imageOfVideoNews.toGone()
            } else {
                layoutSound.toVisibleOrGone(feedItem.isUserNews.orFalse())
                feedItem.thumbnailUrl?.let {
                    Picasso.get().load(feedItem.thumbnailUrl).into(imageOfVideoNews)
                }
                containerOfAudio.toGone()
                imageOfAudioNews.toGone()
                imageOfVideoNews.toVisible()
            }
            labelOfNews.text = feedItem.title

            if (feedItem.responseCounter!! > 0) {
                val responsesText = String.format(
                    context.getString(com.vylo.common.R.string.responses_with_counter),
                    feedItem.responseCounter
                )
                buttonResponses.squareRoundedGrayButtonStyle(context, responsesText)
            } else {
                val responsesText =
                    context.resources.getString(com.vylo.common.R.string.no_responses)
                buttonResponses.squareRoundedGrayButtonGrayTextStyle(context, responsesText)
            }

            onKebabClick.let {
                imageMmenu.setOnClickListener {
                    onKebabClick(feedItem)
                }
            }

            if (feedItem.link.isNullOrEmpty()) {
                labelOfTime.text = Converter.getDurationString(feedItem.contentDuration.orZero())
                labelOfTimeAudio.text = Converter.getDurationString(feedItem.contentDuration.orZero())
            } else {
                labelOfTime.toGone()
                labelOfTimeAudio.toGone()
            }

            if (feedItem.status == 2 || feedItem.status == 4) {
                if (feedItem.contentType == "mp3") layoutOfPrivateAudio?.toVisible()
                else layoutOfPrivate?.toVisible()
            }

            feedItem.globalId?.let { id ->
                if (!feedItem.globalId.isNullOrEmpty() && !feedItem.link.isNullOrEmpty()) {
                    labelOfNews.setOnClickListener {
                        onNewsClick?.let {
                            it(
                                WebData(
                                    id,
                                    feedItem.link,
                                    feedItem.title,
                                    feedItem.externalLink,
                                    feedItem.categoryId,
                                    feedItem.categoryName
                                ),
                                feedItem
                            )
                        }
                    }
                    imageContainer.setOnClickListener {
                        onNewsClick?.let {
                            it(
                                WebData(
                                    id,
                                    feedItem.link,
                                    feedItem.title,
                                    feedItem.externalLink,
                                    feedItem.categoryId,
                                    feedItem.categoryName
                                ),
                                feedItem
                            )
                        }
                    }
                }

                if (!feedItem.globalId.isNullOrEmpty() && !feedItem.contentUrl.isNullOrEmpty()) {
                    labelOfNews.setOnClickListener {
                        onVideoClick?.let {
                            it(VideoData(id, videoType = VideoType.NEWS), feedItem)
                        }
                    }
                    imageContainer.setOnClickListener {
                        onVideoClick?.let {
                            it(VideoData(id, videoType = VideoType.NEWS), feedItem)
                        }
                    }
                }
            }
        }
    }

    internal inner class ResponseViewHolder(itemView: View, private val onKebabClick: (ProfileNewsItem) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val imageOfReporter = itemView.findViewById<ImageView>(R.id.image_of_reporter)
        private val labelNameOfReporter = itemView.findViewById<TextView>(R.id.label_name_of_reporter)
        private val labelNicknameOfReporter = itemView.findViewById<TextView>(R.id.label_nickname_of_reporter)
        private val labelOfTiming = itemView.findViewById<TextView>(R.id.label_of_timing)
        private val imageOfVideoReport = itemView.findViewById<ImageView>(R.id.image_of_video_report)
        private val imageOfAudioReport = itemView.findViewById<ImageView>(R.id.image_of_audio_report)
        private val imageContainer = itemView.findViewById<ConstraintLayout>(R.id.image_container)
        private val imageVverified = itemView.findViewById<ImageView>(R.id.image_verified)
        private val imageMmenu = itemView.findViewById<ImageView>(R.id.image_menu)
        private val labelOfTime = itemView.findViewById<TextView>(R.id.label_of_time)
        private val titleOfNews = itemView.findViewById<TextView>(R.id.title_of_news)
        private val imageOfNewsCompany = itemView.findViewById<ImageView>(R.id.image_of_news_company)
        private val labelNewsName = itemView.findViewById<TextView>(R.id.label_news_name)
        private val imageReportedNews = itemView.findViewById<ImageView>(R.id.image_reported_news)
        private val containerNews = itemView.findViewById<LinearLayout>(R.id.container_news)
        private val layoutOfPrivate = itemView.findViewById<ConstraintLayout>(R.id.layout_private)
        private val layoutOfPrivateAudio = itemView.findViewById<ConstraintLayout>(R.id.layout_private_audio)
        private val labelOfTimeAudio = itemView.findViewById<TextView>(R.id.label_of_time_audio)
        private val containerOfAudio = itemView.findViewById<ConstraintLayout>(R.id.container_of_audio)
        private val layoutSound = itemView.findViewById<ConstraintLayout>(R.id.layout_sound_x)
        private var feedItem: ProfileNewsItem? = null

        fun getFeedItem() = feedItem

        fun bind(feedItem: ProfileNewsItem) {

            this.feedItem = feedItem

            feedItem.publisherImage?.let {
                Picasso.get().load(feedItem.publisherImage).into(imageOfReporter)
            }

            labelNameOfReporter.text = feedItem.publisherName
            labelNicknameOfReporter.text = feedItem.getUserName()
            feedItem.published?.let {
                labelOfTiming.text = Converter.timeAgo(feedItem.published!!)
            }
            if (feedItem.contentType == "mp3") {
                layoutSound.toGone()
                feedItem.thumbnailUrl?.let {
                    Picasso.get().load(feedItem.thumbnailUrl).into(imageOfAudioReport)
                }
                containerOfAudio.toVisible()
                imageOfVideoReport.toGone()
                imageOfAudioReport.toVisible()
            } else {
                layoutSound.toVisibleOrGone(feedItem.isUserNews.orFalse())
                feedItem.thumbnailUrl?.let {
                    Picasso.get().load(feedItem.thumbnailUrl).into(imageOfVideoReport)
                }
                containerOfAudio.toGone()
                imageOfVideoReport.toVisible()
                imageOfAudioReport.toGone()
            }
            feedItem.responseTo?.let {
                feedItem.responseTo!!.thumbnailUrl?.let {
                    Picasso.get().load(feedItem.responseTo!!.thumbnailUrl).into(imageReportedNews)
                }
            }
            titleOfNews.text = feedItem.responseTo?.title.orEmpty()
            feedItem.responseTo?.let {
                feedItem.responseTo!!.publisherImage?.let {
                    Picasso.get().load(feedItem.responseTo!!.publisherImage)
                        .into(imageOfNewsCompany)
                }
            }
            labelNewsName.text = feedItem.responseTo?.let { feedItem.responseTo!!.publisherName }

            onKebabClick.let {
                imageMmenu.setOnClickListener {
                    onKebabClick(feedItem)
                }
            }

            if (feedItem.link.isNullOrEmpty()) {
                labelOfTime.text = Converter.getDurationString(feedItem.contentDuration.orZero())
                labelOfTimeAudio.text = Converter.getDurationString(feedItem.contentDuration.orZero())
            } else {
                labelOfTime.toGone()
                labelOfTimeAudio.toGone()
            }

            if (feedItem.status == 2 || feedItem.status == 4) {
                if (feedItem.contentType == "mp3") layoutOfPrivateAudio?.toVisible()
                else layoutOfPrivate?.toVisible()
            }

            feedItem.globalId?.let { id ->
                if (!feedItem.globalId.isNullOrEmpty() && !feedItem.link.isNullOrEmpty()) {
                    imageContainer.setOnClickListener {
                        onNewsClick?.let {
                            it(
                                WebData(
                                    id,
                                    feedItem.link,
                                    feedItem.title,
                                    feedItem.externalLink,
                                    feedItem.categoryId,
                                    feedItem.categoryName
                                ),
                                feedItem
                            )
                        }
                    }
                }

                if (!feedItem.globalId.isNullOrEmpty() && !feedItem.contentUrl.isNullOrEmpty()) {
                    imageContainer.setOnClickListener {
                        onVideoClick?.let {
                            it(VideoData(id, videoType = VideoType.RESPONSE), feedItem)
                        }
                    }
                }
            }
            feedItem.responseTo?.let { responseTo ->
                responseTo.globalId?.let { id ->
                    if (!feedItem.globalId.isNullOrEmpty() && !responseTo.link.isNullOrEmpty()) {
                        containerNews.setOnClickListener {
                            onNewsClick?.let {
                                it(
                                    WebData(
                                        id,
                                        feedItem.responseTo!!.link,
                                        feedItem.title,
                                        feedItem.externalLink,
                                        feedItem.categoryId,
                                        feedItem.categoryName
                                    ),
                                    feedItem
                                )
                            }
                        }
                    } else if (!feedItem.globalId.isNullOrEmpty() && !responseTo.contentUrl.isNullOrEmpty()) {
                        containerNews.setOnClickListener {
                            onVideoClick?.let {
                                it(VideoData(id, videoType = VideoType.NEWS), feedItem)
                            }
                        }
                    }
                }
            }
        }
    }

    fun clearData() {
        newsList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount() = newsList.size

    fun submitData(list: List<ProfileNewsItem>) {
        newsList.addAll(list)
        notifyDataSetChanged()
    }

    fun updateData(data: com.vylo.common.entity.UploadData) {
        val updItem = newsList.find { it.globalId == data.id }
        updItem?.let {
            val index = newsList.indexOf(it)
            val newItem = newsList[index].copy(
                title = data.title,
                categoryId = data.categoryId,
                categoryName = data.categoryName,
                status = data.status
            )

            newsList.removeAt(index)
            newsList.add(index, newItem)
            notifyItemChanged(index)
        }
    }

    fun deleteData(id: String) {
        val delItem = newsList.find { it.globalId == id }
        delItem?.let {
            val index = newsList.indexOf(it)
            newsList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun getData() = newsList
}