package com.vylo.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.vylo.common.R
import com.vylo.common.adapter.entity.UserInfo
import com.vylo.common.ext.toGone
import com.vylo.common.ext.toVisible

class UserView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayoutCompat(context!!, attrs, defStyleAttr) {

    private val containerOfReporterAvatar: CardView
    private val imageOfReporter: ImageView
    private val llUserContainer: LinearLayoutCompat
    private val labelNameOfReporter: TextView
    private val labelNicknameOfReporter: TextView
    private val labelFollowing: TextView
    private var buttonFollowing: MainButton

    private var isFollow: Boolean
    private var clickButtonFollowing: ((Long, String, Boolean) -> Unit)? = null


    private fun setReportImage(reportImage: String?) {
        if (reportImage != null && reportImage.isNotEmpty()) {
            reportImage.let {
                Picasso.get().load(it).placeholder(R.drawable.portrait_placeholder)
                    .into(imageOfReporter)
            }
        }

    }

    private fun setReporterNickname(text: String?) {
        if (text.isNullOrEmpty()) {
            labelNicknameOfReporter.toGone()
        } else {
            labelNicknameOfReporter.text = text
        }
    }

//    private fun setVerificationLabel(isVerified: Boolean) {
//        if (isVerified) {
//            labelNameOfReporter.drawableEnd(
//                R.drawable.ic_verified,
//                R.dimen.margin_padding_size_small_mid
//            )
//        }
//    }

    private fun setLabelFollowingVisibility(
        isFollower: Boolean
    ) {
        if (isFollower) {
            labelFollowing.text = resources.getString(R.string.label_following)
            labelFollowing.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white_gray_ultra_light
                )
            )
        } else {
            labelFollowing.text = resources.getString(R.string.label_follow)
            labelFollowing.setTextColor(ContextCompat.getColor(context, R.color.secondary))
        }
        labelFollowing.toVisible()
    }

    private fun setFollowButtonVisibility(isVisible: Boolean) {
        if (isVisible) {
            buttonFollowing.toVisible()
        } else {
            buttonFollowing.toGone()
        }
    }

    private fun setFollowButtonText(isFollower: Boolean) {
        if (isFollower) {
            buttonFollowing.squareRoundedGrayButtonStyle(
                context,
                context.getString(R.string.button_following)
            )
        } else {
            buttonFollowing.squareRoundedWhiteButtonStyle(
                context,
                context.getString(R.string.button_follow)
            )
        }
    }

    fun initialize(
        reportImage: String?,
        reporterName: String,
        reporterNickname: String?,
        isVerified: Boolean,
        isLabelFollowing: Boolean,
        isButtonFollowing: Boolean = false,
        isFollower: Boolean = false,
        clickButtonFollowing: (() -> Unit)? = null
    ) {
        setReportImage(reportImage)

        labelNameOfReporter.text = reporterName
        setReporterNickname(reporterNickname)

//        setVerificationLabel(isVerified)

        setLabelFollowingVisibility(isLabelFollowing)

        setFollowButtonVisibility(isButtonFollowing)
        setFollowButtonText(isFollower)
        if (clickButtonFollowing != null) {
            buttonFollowing.clickOnButton { clickButtonFollowing() }
            OnClickListener { clickButtonFollowing() }
        }
    }

    fun initialize(info: UserInfo, clickButtonFollowing: (() -> Unit)? = null) {
        setReportImage(info.reporterImage)

        labelNameOfReporter.text = info.name
        setReporterNickname(info.subName)

//        setVerificationLabel(info.isVerified)

        setLabelFollowingVisibility(info.isLabelFollowing)

        setFollowButtonVisibility(info.isButtonFollowing)
        setFollowButtonText(info.isFollower)
        if (clickButtonFollowing != null) {
            buttonFollowing.clickOnButton { clickButtonFollowing() }
        }
    }

    fun initialize(
        info: UserInfo,
        clickButtonFollowing: ((Long, Boolean) -> Unit)? = null,
        clickUser: ((String?) -> Unit)? = null
    ) {
        setReportImage(info.reporterImage)

        labelNameOfReporter.text = info.name
        setReporterNickname(info.subName)

//        setVerificationLabel(info.isVerified)

        isFollow = info.isFollower
        if (info.isLabelFollowing) {
            setLabelFollowingVisibility(isFollow)
        }

        setFollowButtonVisibility(info.isButtonFollowing)

        setFollowButtonText(isFollow)
        clickButtonFollowing?.let {
            buttonFollowing.clickOnButton {
                isFollow = !isFollow
                setFollowButtonText(isFollow)
                it(info.id, isFollow)
            }
            labelFollowing.setOnClickListener {
                isFollow = !isFollow
                setLabelFollowingVisibility(isFollow)
                it(info.id, isFollow)
            }
        }
        clickUser?.let {
            containerOfReporterAvatar.setOnClickListener { it(info.globalId) }
            llUserContainer.setOnClickListener { it(info.globalId) }
        }
    }

    fun initialize(
        info: UserInfo,
        clickButtonFollowing: ((Long, String, Boolean) -> Unit)? = null,
        clickUser: ((String?) -> Unit)? = null
    ) {
        setReportImage(info.reporterImage)

        labelNameOfReporter.text = info.name
        labelNameOfReporter.visibility = GONE
        val followLabel = info.subName +  if (info.isLabelFollowing)  resources.getString(R.string.label_following_dot) else ""
        setReporterNickname(followLabel)

//        setVerificationLabel(info.isVerified)

        isFollow = info.isFollower
        if (info.isLabelFollowing) {
            setLabelFollowingVisibility(isFollow)
        }

        setFollowButtonVisibility(info.isButtonFollowing)

        setFollowButtonText(isFollow)
        this.clickButtonFollowing = clickButtonFollowing
        if (info.id != 0L) {
            updateFollowingListener(info.id, info.globalId.orEmpty())
        }

        clickUser?.let {
            containerOfReporterAvatar.setOnClickListener { it(info.globalId) }
            llUserContainer.setOnClickListener { it(info.globalId) }
        }
    }

    fun updateFollowingStatus(isLabelFollowing: Boolean, isFollower: Boolean) {
        isFollow = isFollower
        if (isLabelFollowing) {
            setLabelFollowingVisibility(isFollow)
        }
        setFollowButtonText(isFollow)
    }

    fun updateFollowingListener(id: Long, globalId: String) {
        clickButtonFollowing?.let {
            buttonFollowing.clickOnButton {
                isFollow = !isFollow
                setFollowButtonText(isFollow)
                it(id, globalId, isFollow)
            }
            labelFollowing.setOnClickListener {
                isFollow = !isFollow
                setLabelFollowingVisibility(isFollow)
                it(id, globalId, isFollow)
            }
        }
    }

    fun updateNickNameStyle(fontColor: Int) {
        labelNicknameOfReporter.setTextColor(fontColor)
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        inflate(context, R.layout.user_view, this)

        containerOfReporterAvatar = findViewById(R.id.container_of_reporter_avatar)
        imageOfReporter = findViewById(R.id.image_of_reporter)
        llUserContainer = findViewById(R.id.ll_user_container)
        labelNameOfReporter = findViewById(R.id.label_name)
        labelNicknameOfReporter = findViewById(R.id.label_subname)
        labelFollowing = findViewById(R.id.label_following)
        buttonFollowing = findViewById(R.id.button_following)

        isFollow = false
    }
}