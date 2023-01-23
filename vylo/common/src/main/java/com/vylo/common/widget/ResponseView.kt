package com.vylo.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import com.vylo.common.R

class ResponseView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context!!, attrs, defStyleAttr) {

    private val titleOfNews: TextView
    private val imageOfNewsCompany: ImageView
    private val labelNewsName: TextView
    private val imageReportedNews: ImageView
    private val containerNews: LinearLayout

    fun initialize(
        titleNews: String,
        imageNewsCompany: String,
        titleNewsName: String,
        imageReport: String,
        onNewsClick: (() -> Unit)? = null,
        onPublisherClick: (() -> Unit)? = null
    ) {
        titleOfNews.text = titleNews
        labelNewsName.text = titleNewsName

        if (imageNewsCompany.isNotBlank()) {
            Picasso.get().load(imageNewsCompany).into(imageOfNewsCompany)
        }

        if (imageReport.isNotBlank()) {
            Picasso.get().load(imageReport).into(imageReportedNews)
        }

        onNewsClick?.let {
            containerNews.setOnClickListener { it() }
        }

        onPublisherClick?.let {
            imageOfNewsCompany.setOnClickListener { it() }
            labelNewsName.setOnClickListener { it() }
        }
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        inflate(context, R.layout.response_view, this)
        containerNews = findViewById(R.id.container_news)
        titleOfNews = findViewById(R.id.title_of_news)
        imageOfNewsCompany = findViewById(R.id.image_of_news_company)
        labelNewsName = findViewById(R.id.label_news_name)
        imageReportedNews = findViewById(R.id.image_reported_news)
    }
}