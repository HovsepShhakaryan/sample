package com.vylo.main.component.adapter

import android.content.Context
import android.util.Log
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
import com.vylo.main.component.adapter.enum.FeedType
import com.vylo.main.component.events.domain.entity.request.TimingModel
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import java.util.*


class FeedAdapter(
    adapterCallBack: AdapterCallBack,
    context: Context,
    private val feedType: FeedType = FeedType.VYLO,
    private val onVideoClick: ((VideoData, FeedItem) -> Unit)? = null,
    private val onNewsClick: ((WebData, FeedItem) -> Unit)? = null,
    private val onKebabClick: ((FeedItem) -> Unit)? = null,
    private val onUserClick: (String?) -> Unit,
    private val onRespondClick: (String?, String?, String?, String?) -> Unit,
    private val onResponsesClick: ((VideoData) -> Unit)? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface AdapterCallBack {
        fun getFeed(position: Int)
    }

    private val viewTypeNews = 1
    private val viewTypeResponse = 2
    private var data = mutableListOf<FeedItem>()
    private val callBack = adapterCallBack
    private val ctx = context
    private val timingList = mutableListOf<TimingModel>()

    override fun getItemViewType(position: Int): Int {
        if (data[position].responseTo == null)
            return viewTypeNews
        return viewTypeResponse
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == viewTypeNews) {
            return NewsViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_feed_news, parent, false)
            )
        }
        return ResponseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_feed_report, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (holder) {
            is NewsViewHolder -> holder.bind(item, ctx)
            is ResponseViewHolder -> holder.bind(item)
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        val currentTime = (Calendar.getInstance().timeInMillis / 1000).toInt()
        timingList.add(
            TimingModel(
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
        val timeModelList = mutableListOf<TimingModel>()
        timeModelList.addAll(timingList)
        for (item in timeModelList) {
            if (item.position == holder.layoutPosition) {
                val newModel = TimingModel(
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

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        private val labelOfTimeAudio = itemView.findViewById<TextView>(R.id.label_of_time_audio)
        private val containerOfAudio = itemView.findViewById<ConstraintLayout>(R.id.container_of_audio)
        private val layoutSound = itemView.findViewById<ConstraintLayout>(R.id.layout_sound_x)
        private var feedItem: FeedItem? = null

        fun getFeedItem() = feedItem

        fun bind(feedItem: FeedItem, context: Context) {

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

            imageOfReporter.setOnClickListener { onUserClick(feedItem.publisherId) }
            labelNameOfReporter.setOnClickListener { onUserClick(feedItem.publisherId) }
            labelNicknameOfReporter.setOnClickListener { onUserClick(feedItem.publisherId) }

            labelNameOfReporter.text = feedItem.publisherName
            labelNicknameOfReporter.text = feedItem.getUserName()
            feedItem.published?.let {
                labelOfTiming.text = Converter.timeAgo(feedItem.published)
            }
            if (feedItem.contentType == "mp3") {
                layoutSound.toGone()
                feedItem.thumbnailUrl?.let {
                    Picasso.get().load(feedItem.thumbnailUrl).into(imageOfAudioNews)
                }
                containerOfAudio.toVisible()
                imageOfVideoNews.toGone()
            } else {
                layoutSound.toVisibleOrGone(feedItem.isUserNews.orFalse())
                feedItem.thumbnailUrl?.let {
                    Picasso.get().load(feedItem.thumbnailUrl).into(imageOfVideoNews)
                }
                containerOfAudio.toGone()
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
                buttonResponses.setTitle(responsesText)
                buttonResponses.squareRoundedGrayButtonGrayTextStyle(context, responsesText)
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

            onKebabClick?.let {
                imageMmenu.setOnClickListener { it(feedItem) }
            }

            if (feedItem.link.isNullOrEmpty()) {
                labelOfTime.text = Converter.getDurationString(feedItem.contentDuration.orZero())
                labelOfTimeAudio.text = Converter.getDurationString(feedItem.contentDuration.orZero())
            } else {
                labelOfTime.toGone()
                labelOfTimeAudio.toGone()
            }
        }
    }

    internal inner class ResponseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        private val labelOfTimeAudio = itemView.findViewById<TextView>(R.id.label_of_time_audio)
        private val containerOfAudio = itemView.findViewById<ConstraintLayout>(R.id.container_of_audio)
        private val layoutSound = itemView.findViewById<ConstraintLayout>(R.id.layout_sound_x)
        private var feedItem: FeedItem? = null

        fun getFeedItem() = feedItem

        fun bind(feedItem: FeedItem) {

            this.feedItem = feedItem

            feedItem.publisherImage?.let {
                Picasso.get().load(feedItem.publisherImage).into(imageOfReporter)
            }

            imageOfReporter.setOnClickListener { onUserClick(feedItem.publisherId) }
            labelNameOfReporter.setOnClickListener { onUserClick(feedItem.publisherId) }
            labelNicknameOfReporter.setOnClickListener { onUserClick(feedItem.publisherId) }
            imageOfNewsCompany.setOnClickListener { onUserClick(feedItem.responseTo?.publisherId) }
            labelNewsName.setOnClickListener { onUserClick(feedItem.responseTo?.publisherId) }

            labelNameOfReporter.text = feedItem.publisherName
            labelNicknameOfReporter.text = feedItem.getUserName()
            feedItem.published?.let {
                labelOfTiming.text = Converter.timeAgo(feedItem.published)
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
                feedItem.responseTo.thumbnailUrl?.let {
                    Picasso.get().load(feedItem.responseTo.thumbnailUrl).into(imageReportedNews)
                }
            }
            titleOfNews.text = feedItem.responseTo?.title.orEmpty()

            feedItem.responseTo?.let {
                feedItem.responseTo.publisherImage?.let {
                    Picasso.get().load(feedItem.responseTo.publisherImage).into(imageOfNewsCompany)
                }
            }
            labelNewsName.text = feedItem.responseTo?.let { feedItem.responseTo.publisherName }

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
                                        feedItem.responseTo.link,
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

            onKebabClick?.let {
                imageMmenu.setOnClickListener { it(feedItem) }
            }

            if (feedItem.link.isNullOrEmpty()) {
                labelOfTime.text = Converter.getDurationString(feedItem.contentDuration.orZero())
                labelOfTimeAudio.text = Converter.getDurationString(feedItem.contentDuration.orZero())
            } else {
                labelOfTime.toGone()
                labelOfTimeAudio.toGone()
            }
        }
    }

    fun setData(loads: List<FeedItem>) {
        data.addAll(loads)
        notifyDataSetChanged()
    }

    fun deleteData(id: String) {
        val delItem = data.find { it.globalId == id }
        delItem?.let {
            val index = data.indexOf(it)
            data.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun getData() = data

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

}