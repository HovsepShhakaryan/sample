package com.vylo.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.vylo.common.R
import com.vylo.common.ext.setLinearMarginTop
import com.vylo.common.ext.toVisible
import com.vylo.common.util.enums.ButtonStyle

class EmptyStateView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayoutCompat(context!!, attrs, defStyleAttr) {

    private val container: LinearLayoutCompat
    private val topLine: View
    private val image: ImageView
    private val bigTitle: TextView
    private val title: TextView
    private val subTitle: TextView
    private val firstButton: MainButton
    private val secondButton: MainButton

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    private fun setFirstTitleStyle(styleId: Int) {
        bigTitle.setTextAppearance(styleId)
    }

    private fun setSubTitleStyle(styleId: Int) {
        subTitle.setTextAppearance(styleId)
    }

    fun createVyloEmptyState(
        onFindUsersToFollowClick: () -> Unit
    ) {
        title.apply {
            text = resources.getString(R.string.label_empty_state_following)
            setFirstTitleStyle(R.style.MainText_H3_1)
            setLinearMarginTop(R.dimen.container_185)
            toVisible()
        }

        subTitle.apply {
            text = resources.getString(R.string.sublabel_empty_state_follow_users)
            setSubTitleStyle(R.style.MainText_H8_8)
            setLinearMarginTop(R.dimen.margin_padding_size_large_pre_mid)
            toVisible()
        }

        firstButton.apply {
            setLinearMarginTop(R.dimen.margin_padding_size_biggest_big)
            toVisible()
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.button_empty_state_find_users),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            clickOnButton {
                onFindUsersToFollowClick()
            }
        }
    }

    fun createFollowingVyloEmptyState(
        onFindUsersToFollowClick: () -> Unit
    ) {
        title.apply {
            text = resources.getString(R.string.label_empty_state_following)
            setFirstTitleStyle(R.style.MainText_H3_1)
            setLinearMarginTop(R.dimen.container_185)
            toVisible()
        }

        subTitle.apply {
            text = resources.getString(R.string.sublabel_empty_state_follow_users)
            setSubTitleStyle(R.style.MainText_H8_8)
            setLinearMarginTop(R.dimen.margin_padding_size_large_pre_mid)
            toVisible()
        }

        firstButton.apply {
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.button_empty_state_find_users),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            setLinearMarginTop(R.dimen.margin_padding_size_biggest_big)
            toVisible()
            clickOnButton {
                onFindUsersToFollowClick()
            }
        }
    }

    fun createFollowingNewsstandEmptyState(
        onFindPublishersToFollowClick: () -> Unit,
        onFindCategoriesToFollowClick: () -> Unit
    ) {
        title.apply {
            text =
                resources.getString(R.string.label_empty_state_not_following_publishers_categories)
            setFirstTitleStyle(R.style.MainText_H3_1)
            setLinearMarginTop(R.dimen.container_133)
            toVisible()
        }

        subTitle.apply {
            text = resources.getString(R.string.sublabel_empty_state_follow)
            setSubTitleStyle(R.style.MainText_H8_8)
            setLinearMarginTop(R.dimen.margin_padding_size_large_pre_mid)
            toVisible()
        }

        firstButton.apply {
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.button_empty_state_find_publishers),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            setLinearMarginTop(R.dimen.margin_padding_size_biggest_big)
            toVisible()
            clickOnButton {
                onFindPublishersToFollowClick()
            }
        }

        secondButton.apply {
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.button_empty_state_find_categories),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            toVisible()
            clickOnButton {
                onFindCategoriesToFollowClick()
            }
        }
    }

    fun createNewsstandEmptyState(
        onFindPublishersToFollowClick: () -> Unit,
        onFindCategoriesToFollowClick: () -> Unit
    ) {
        title.apply {
            text = resources.getString(R.string.label_empty_state_not_newsstand)
            setFirstTitleStyle(R.style.MainText_H3_1)
            setLinearMarginTop(R.dimen.container_133)
            toVisible()
        }

        subTitle.apply {
            text = resources.getString(R.string.sublabel_empty_state_follow)
            setSubTitleStyle(R.style.MainText_H8_8)
            setLinearMarginTop(R.dimen.margin_padding_size_large_pre_mid)
            toVisible()
        }

        firstButton.apply {
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.button_empty_state_find_publishers),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            setLinearMarginTop(R.dimen.margin_padding_size_biggest_big)
            toVisible()
            clickOnButton {
                onFindPublishersToFollowClick()
            }
        }

        secondButton.apply {
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.button_empty_state_find_categories),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            toVisible()
            clickOnButton {
                onFindCategoriesToFollowClick()
            }
        }
    }

    fun createFollowingNewsstandCategoryEmptyState(
        onFindCategoriesToFollowClick: () -> Unit
    ) {
        val params = LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        container.layoutParams = params

        title.apply {
            text = resources.getString(R.string.label_empty_state_not_following_category)
            setFirstTitleStyle(R.style.MainText_H3_1)
            setLinearMarginTop(R.dimen.margin_padding_size_biggest_mid)
            toVisible()
        }

        firstButton.apply {
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.button_empty_state_find_categories),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            setLinearMarginTop(R.dimen.margin_padding_size_large_pre_small_mid)
            toVisible()
            clickOnButton {
                onFindCategoriesToFollowClick()
            }
        }
    }

    fun createFollowingNewsstandPublishersEmptyState(
        onFindPublishersToFollowClick: () -> Unit
    ) {
        val params = LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        container.layoutParams = params

        title.apply {
            text = resources.getString(R.string.label_empty_state_not_following)
            setFirstTitleStyle(R.style.MainText_H3_1)
            setLinearMarginTop(R.dimen.margin_padding_size_biggest_mid)
            toVisible()
        }

        firstButton.apply {
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.button_empty_state_find_publishers),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            setLinearMarginTop(R.dimen.margin_padding_size_large_pre_small_mid)
            toVisible()
            clickOnButton {
                onFindPublishersToFollowClick()
            }
        }
    }

    fun createFollowingNewsstandCategoriesEmptyState(
        onFindPublishersToFollowClick: () -> Unit
    ) {
        val params = LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        container.layoutParams = params

        title.apply {
            text = resources.getString(R.string.label_empty_state_not_following)
            setFirstTitleStyle(R.style.MainText_H3_1)
            setLinearMarginTop(R.dimen.margin_padding_size_large_pre_small_mid)
            toVisible()
        }

        firstButton.apply {
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.button_empty_state_find_publishers),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            setLinearMarginTop(R.dimen.margin_padding_size_xxlarge)
            toVisible()
            clickOnButton {
                onFindPublishersToFollowClick()
            }
        }
    }

    fun createProfileEmptyState(
        onGoToNewsstandClick: () -> Unit
    ) {
        topLine.toVisible()
        title.apply {
            text = resources.getString(R.string.label_empty_state_no_posts)
            setFirstTitleStyle(R.style.MainText_H3_1)
            setLinearMarginTop(R.dimen.container_134)
            toVisible()
        }

        subTitle.apply {
            text = resources.getString(R.string.sublabel_empty_state_no_posts)
            setSubTitleStyle(R.style.MainText_H8_8)
            setLinearMarginTop(R.dimen.margin_padding_size_medium)
            toVisible()
        }

        firstButton.apply {
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.button_empty_state_go_to_newsstand),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            setLinearMarginTop(R.dimen.margin_padding_size_biggest_big)
            toVisible()
            clickOnButton {
                onGoToNewsstandClick()
            }
        }
    }

    fun createProfileFollowersEmptyState(
        onShareMyProfileClick: () -> Unit
    ) {
        title.apply {
            text = resources.getString(R.string.label_empty_state_no_followers)
            setFirstTitleStyle(R.style.MainText_H3_1)
            setLinearMarginTop(R.dimen.container_195)
            toVisible()
        }

//        subTitle.apply {
//            text = resources.getString(R.string.sublabel_empty_share_profile)
//            setSubTitleStyle(R.style.MainText_H8_8)
//            setLinearMarginTop(R.dimen.margin_padding_size_medium)
//            toVisible()
//        }

//        firstButton.apply {
//            setTitle(resources.getString(R.string.button_empty_share_profile))
//            setStyle(R.style.MainText_H7_1_Black)
//            setShape(R.drawable.shape_blue_without_border)
//            setLinearMarginTop(R.dimen.margin_padding_size_biggest_big)
//            toVisible()
//            clickOnButton {
//                onShareMyProfileClick()
//            }
//        }
    }

    fun createProfileFollowingEmptyState(
        onFindUsersToFollowClick: () -> Unit,
        onFindPublishersToFollowClick: () -> Unit
    ) {
        title.apply {
            text = resources.getString(R.string.label_empty_state_not_following)
            setFirstTitleStyle(R.style.MainText_H3_1)
            setLinearMarginTop(R.dimen.container_149)
            toVisible()
        }

        subTitle.apply {
            text = resources.getString(R.string.sublabel_empty_state_follow)
            setSubTitleStyle(R.style.MainText_H8_8)
            setLinearMarginTop(R.dimen.margin_padding_size_xlarge_small)
            toVisible()
        }

        firstButton.apply {
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.button_empty_state_find_users),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            setLinearMarginTop(R.dimen.margin_padding_size_biggest_mid_mid)
            toVisible()
            clickOnButton {
                onFindUsersToFollowClick()
            }
        }

        secondButton.apply {
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.button_empty_state_find_publishers),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            toVisible()
            clickOnButton {
                onFindPublishersToFollowClick()
            }
        }
    }

    fun createProfileEmptyStateNoButton() {
        topLine.toVisible()
        title.apply {
            text = resources.getString(R.string.label_empty_state_no_posts)
            setFirstTitleStyle(R.style.MainText_H3_1)
            setLinearMarginTop(R.dimen.container_134)
            toVisible()
        }

        subTitle.apply {
            text = resources.getString(R.string.sublabel_empty_state_no_posts)
            setSubTitleStyle(R.style.MainText_H8_8)
            setLinearMarginTop(R.dimen.margin_padding_size_medium)
            toVisible()
        }
    }

    init {
        inflate(context, R.layout.empty_state_view, this)

        container = findViewById(R.id.llContainer)
        topLine = findViewById(R.id.top_line)
        image = findViewById(R.id.image)
        bigTitle = findViewById(R.id.big_title)
        title = findViewById(R.id.title)
        subTitle = findViewById(R.id.sub_title)
        firstButton = findViewById(R.id.first_button)
        secondButton = findViewById(R.id.second_button)
    }
}