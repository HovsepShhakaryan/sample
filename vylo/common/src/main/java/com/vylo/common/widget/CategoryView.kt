package com.vylo.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.R
import com.vylo.common.adapter.CategoriesAdapter
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.adapter.entity.CategoryInfo
import com.vylo.common.adapter.entity.SubcategoryInfo
import com.vylo.common.adapter.entity.toCommonCategoryItem
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.common.ext.toGone
import com.vylo.common.ext.toVisible

class CategoryView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context!!, attrs, defStyleAttr) {

    private val llCategory: LinearLayoutCompat

    private val labelNameOfReporter: TextView
//    private val labelNicknameOfReporter: TextView
    private var buttonFollowing: MainButton
    private var imgExpand: ImageView
    private var expandList: RecyclerView
    private var buttonStar: ImageView

    private var isExpandList: Boolean = false
    private var isFollow: Boolean

    private val subcategoryAdapter by lazy {
        CategoriesAdapter()
    }

    private fun setExpandListener(
        list: List<SubcategoryInfo>?
    ) {
        if (!list.isNullOrEmpty()) {
            imgExpand.toVisible()
            imgExpand.setOnClickListener {
                if (isExpandList) {
                    expandList.toGone()
                    setImgExpand(R.drawable.ic_kebab_expand)
                } else {
                    expandList.toVisible()
                    expandList.layoutManager = LinearLayoutManager(context)
                    subcategoryAdapter.submitList(list.map { it.copy(isFollow = isFollow) })
                    expandList.adapter = subcategoryAdapter
                    setImgExpand(R.drawable.ic_kebab_collapse)
                }
                isExpandList = !isExpandList
            }
        } else {
            imgExpand.toGone()
        }
    }

    private fun setImgExpand(id: Int) {
        imgExpand.setImageResource(id)
    }

    private fun setButtonFollowingStyle(resId: Int) {
        buttonFollowing.setShape(resId)
    }

    private fun setFollowButtonVisibility(isVisible: Boolean) {
        if (isVisible) {
            buttonFollowing.toVisible()
        } else {
            buttonFollowing.toGone()
        }
    }

    private fun setFollowTextStyle(resId: Int) {
        buttonFollowing.setStyle(resId)
    }

    private fun setFollowButtonText(isFollower: Boolean) {
        if (isFollower) {
            setButtonFollowingStyle(R.drawable.shape_white_border)
            setFollowTextStyle(R.style.MainText_H8_1)
            buttonFollowing.setTitle(resources.getString(R.string.button_following))
            buttonStar.setBackgroundResource(R.drawable.ic_star_active)
        } else {
            setButtonFollowingStyle(R.drawable.shape_white_without_border)
            setFollowTextStyle(R.style.MainText_H8_1_Dark)
            buttonFollowing.setTitle(resources.getString(R.string.button_follow))
            buttonStar.setBackgroundResource(R.drawable.ic_star_not_active)
        }
    }

    fun initialize(
        info: CategoryInfo,
        clickButtonFollowing: ((Long, Boolean) -> Unit)? = null,
        categoryClick: ((CommonCategoryItem) -> Unit)? = null,
        isShow: Boolean
    ) {
        labelNameOfReporter.text = info.name
//        labelNicknameOfReporter.text = "41.2M Subscribers"

        setFollowButtonVisibility(info.isButtonFollowing)
        isFollow = info.isFollow
        setFollowButtonText(isFollow)
        if (clickButtonFollowing != null) {
            subcategoryAdapter.setOnClickListener(clickButtonFollowing)
            when(isShow) {
                true -> {
                    buttonFollowing.visibility = View.GONE
                    buttonStar.visibility = View.VISIBLE
                }
                false -> {
                    buttonFollowing.visibility = View.VISIBLE
                    buttonStar.visibility = View.GONE
                }
            }

            buttonFollowing.clickOnButton {
                isFollow = !isFollow
                setFollowButtonText(isFollow)
                info.categoriesList?.let {
                    subcategoryAdapter.submitList(it.map { sub -> sub.copy(isFollow = isFollow) })
                }
                clickButtonFollowing(info.id, isFollow)
            }
            buttonStar.setOnClickListener {
                isFollow = !isFollow
                setFollowButtonText(isFollow)
                info.categoriesList?.let {
                    subcategoryAdapter.submitList(it.map { sub -> sub.copy(isFollow = isFollow) })
                }
                clickButtonFollowing(info.id, isFollow)
            }
        }

        categoryClick?.let {
            llCategory.setOnClickListener { it(info.toCommonCategoryItem()) }
        }

        setExpandListener(info.categoriesList)
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        inflate(context, R.layout.category_view, this)

        llCategory = findViewById(R.id.ll_category)
        labelNameOfReporter = findViewById(R.id.label_name)
//        labelNicknameOfReporter = findViewById(R.id.label_subname)
        buttonFollowing = findViewById(R.id.button_following)
        imgExpand = findViewById(R.id.img_expand)
        expandList = findViewById(R.id.expand_list)
        buttonStar = findViewById(R.id.button_star)
        expandList.addItemDecoration(
            MarginItemDecoration(
                bottom = resources.getDimensionPixelSize(
                    R.dimen.margin_padding_size_medium_small_small_mid
                ),
                isBottomMargin = false
            )
        )

        isFollow = false
    }
}