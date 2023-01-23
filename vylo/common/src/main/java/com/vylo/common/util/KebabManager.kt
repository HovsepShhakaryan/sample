package com.vylo.common.util

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.vylo.common.R
import com.vylo.common.adapter.entity.KebabItem
import com.vylo.common.adapter.entity.KebabOption
import com.vylo.common.entity.NewsItem
import com.vylo.common.ext.orFalse
import com.vylo.common.util.enums.KebabType
import com.vylo.common.widget.KebabBottomSheet

object KebabManager {

    fun createCommonKebab(
        activity: FragmentActivity,
        globalId: String,
        externalLink: String?,
        isUserNews: Boolean?,
        isActive: Boolean,
        onInsightfulClick: (id: String, isActive: Boolean) -> Unit,
        onShareClick: (id: String, link: String?) -> Unit,
        onCopyLinkClick: (id: String, link: String?) -> Unit,
        onReportClick: (id: String) -> Unit
    ) {
        val bottomSheet = KebabBottomSheet(KebabType.COMMON)
        bottomSheet.show(activity.supportFragmentManager, null)

        val options = mutableListOf<KebabOption>()

        if (isUserNews.orFalse()) {
            options.add(
                KebabOption(
                    activity.getString(R.string.label_insightful),
                    imagesRes = listOf(
                        R.drawable.ic_vector_insingfull,
                        R.drawable.ic_vector_insingfull_active
                    ),
                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                    clickWithParam = {
                        it?.let {
                            onInsightfulClick(globalId, it as Boolean)
                        }
                    },
                    isActive = isActive
                )
            )
        }

        options.addAll(listOf(
            KebabOption(
                activity.getString(R.string.label_share),
                imagesRes = listOf(R.drawable.ic_kebab_share),
                imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                click = { onShareClick(globalId, externalLink) }
            ),
            KebabOption(
                activity.getString(R.string.label_copy_link),
                imagesRes = listOf(R.drawable.ic_kebab_link),
                imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                click = {
                    onCopyLinkClick(globalId, externalLink)
                    bottomSheet.dismiss()
                }
            ),
            KebabOption(
                activity.getString(R.string.label_report),
                imagesRes = listOf(R.drawable.ic_kebab_report),
                imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                click = { onReportClick(globalId) }
            )
        ))

        KebabItem(
            title = null,
            titleColorRes = null,
            options = options
        ).apply {
            bottomSheet.setKebabItem(this)
        }
    }

    fun createCommonWebKebab(
        activity: FragmentActivity,
        globalId: String?,
        externalLink: String?,
        onShareClick: (id: String, link: String?) -> Unit,
        onCopyLinkClick: (id: String, link: String?) -> Unit,
        onReportClick: (id: String) -> Unit
    ) {
        val bottomSheet = KebabBottomSheet(KebabType.COMMON)
        bottomSheet.show(activity.supportFragmentManager, null)

        val options = mutableListOf<KebabOption>()

        globalId?.let { id ->
            options.addAll(listOf(
                KebabOption(
                    activity.getString(R.string.label_share),
                    imagesRes = listOf(R.drawable.ic_kebab_share),
                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                    click = { onShareClick(id, externalLink) }
                ),
                KebabOption(
                    activity.getString(R.string.label_copy_link),
                    imagesRes = listOf(R.drawable.ic_kebab_link),
                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                    click = {
                        onCopyLinkClick(id, externalLink)
                        bottomSheet.dismiss()
                    }
                ),
                KebabOption(
                    activity.getString(R.string.label_report),
                    imagesRes = listOf(R.drawable.ic_kebab_report),
                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                    click = { onReportClick(id) }
                )
            ))

            KebabItem(
                title = null,
                titleColorRes = null,
                options = options
            ).apply {
                bottomSheet.setKebabItem(this)
            }
        }
    }

    fun createReportKebab(
        activity: FragmentActivity,
        id: String,
        sendReport: (Int, String) -> Unit
    ) {
        val bottomSheet = KebabBottomSheet(KebabType.REPORT)
        bottomSheet.show(activity.supportFragmentManager, null)

        val reportList = activity.resources.getStringArray(R.array.ReportItems)

        val kebabOption = mutableListOf<KebabOption>()
        reportList.forEachIndexed { index, s ->
            kebabOption.add(
                KebabOption(
                    option = s,
                    click = {
                        sendReport(index + 1, id)
                        bottomSheet.dismiss()
                    }
                )
            )
        }

        KebabItem(
            title = activity.getString(R.string.title_column_report),
            titleColorRes = ContextCompat.getColor(
                activity,
                R.color.white_light_gray
            ),
            options = kebabOption
        ).apply {
            bottomSheet.setKebabItem(this)
        }
    }

    fun createMyHamburger(
        activity: FragmentActivity,
        onInsightfulClick: () -> Unit,
        onMyContactClick: () -> Unit,
        onSettingsPrivacyClick: () -> Unit
    ) {
        val bottomSheet = KebabBottomSheet(KebabType.HAMBURGER)
        bottomSheet.show(activity.supportFragmentManager, null)

        KebabItem(
            options = listOf(
//                KebabOption(
//                    option = activity.getString(R.string.title_insightful),
//                    drawImage = ContextCompat.getDrawable(
//                        activity,
//                        com.vylo.common.R.drawable.ic_insight_icon
//                    ),
//                    click = {
//                        onInsightfulClick()
//                        bottomSheet.dismiss()
//                    }
//                ),
//                KebabOption(
//                    option = activity.getString(R.string.hamburger_my_contacts),
//                    drawImage = ContextCompat.getDrawable(
//                        activity,
//                        R.drawable.ic_contact_book
//                    ),
//                    click = {
//                        onMyContactClick()
//                        bottomSheet.dismiss()
//                    }
//                ),
                KebabOption(
                    option = activity.getString(R.string.hamburger_settings_and_privacy),
                    drawImage = ContextCompat.getDrawable(
                        activity,
                        R.drawable.ic_settings
                    ),
                    click = {
                        onSettingsPrivacyClick()
                        bottomSheet.dismiss()
                    }
                )
            )
        ).apply {
            bottomSheet.setKebabItem(this)
        }
    }

    fun createUserHamburger(
        activity: FragmentActivity,
        onCopyUrlClick: () -> Unit,
        onShareClick: () -> Unit,
        onBlockUserClick: () -> Unit,
        onReportUserClick: () -> Unit,
    ) {
        val bottomSheet = KebabBottomSheet(KebabType.HAMBURGER)
        bottomSheet.show(activity.supportFragmentManager, null)
        val redColor = ContextCompat.getColor(activity, R.color.kebab_red)

        KebabItem(
            options = listOf(
//                KebabOption(
//                    option = activity.getString(R.string.copy_profile_url),
//                    click = {
//                        onCopyUrlClick()
//                        bottomSheet.dismiss()
//                    }
//                ),
//                KebabOption(
//                    option = activity.getString(R.string.share_this_profile),
//                    click = {
//                        onShareClick()
//                        bottomSheet.dismiss()
//                    }
//                ),
                KebabOption(
                    option = activity.getString(R.string.block_user),
                    color = redColor,
                    click = {
                        onBlockUserClick()
                        bottomSheet.dismiss()
                    }
                ),
                KebabOption(
                    option = activity.getString(R.string.report_user),
                    color = redColor,
                    click = {
                        onReportUserClick()
                        bottomSheet.dismiss()
                    }
                )
            )
        ).apply {
            bottomSheet.setKebabItem(this)
        }
    }

    fun createProfileReport(
        activity: FragmentActivity,
        sendReport: (Int, String) -> Unit
    ) {
        val bottomSheet = KebabBottomSheet(KebabType.COMMON)
        bottomSheet.show(activity.supportFragmentManager, null)
        val whiteColor = ContextCompat.getColor(activity, R.color.primary)
        val redColor = ContextCompat.getColor(activity, R.color.kebab_red)

        val reportList = activity.resources.getStringArray(R.array.ReportItems)
        val kebabOption = mutableListOf<KebabOption>()
        val twoLastIndexes = (reportList.size - 2) until reportList.size

        reportList.forEachIndexed { index, s ->
            kebabOption.add(
                KebabOption(
                    option = s,
                    color = if (index in twoLastIndexes) redColor else whiteColor,
                    click = {
                        sendReport(index + 1, "id")
                        bottomSheet.dismiss()
                    }
                )
            )
        }

        KebabItem(
            title = activity.getString(R.string.title_user_report),
            titleColorRes = ContextCompat.getColor(
                activity,
                com.vylo.common.R.color.white_light_gray
            ),
            options = kebabOption
        ).apply {
            bottomSheet.setKebabItem(this)
        }
    }

    fun createMyNewsKebab(
        activity: FragmentActivity,
        item: NewsItem,
        isActive: Boolean,
        onInsightfulClick: (String, Boolean) -> Unit,
        onEditClick: (NewsItem) -> Unit,
        onShareClick: (String, String?) -> Unit,
        onCopyLinkClick: (String, String?) -> Unit,
        onDeleteClick: (String) -> Unit,
    ) {
        val bottomSheet = KebabBottomSheet(KebabType.COMMON)
        bottomSheet.show(activity.supportFragmentManager, null)

        val title = if (item.status != 2) {
            null
        } else {
            activity.getString(R.string.label_kebab_private)
        }

        val optionColor = if (item.status != 2) {
            null
        } else {
            ContextCompat.getColor(activity, R.color.secondary_grey)
        }

        val insightfulIcon = if (item.status != 2) {
            listOf(
                R.drawable.ic_vector_insingfull,
                R.drawable.ic_vector_insingfull_active
            )
        } else {
            listOf(
                R.drawable.ic_kebab_insight
            )
        }

        val shareIcon = if (item.status != 2) {
            R.drawable.ic_kebab_share
        } else {
            R.drawable.ic_kebab_share_nonactive
        }

        val copyIcon = if (item.status != 2) {
            R.drawable.ic_kebab_link
        } else {
            R.drawable.ic_kebab_copy_link_nonactive
        }

        val editIcon = if (item.status != 2) {
            R.drawable.ic_kebab_edit
        } else {
            R.drawable.ic_kebab_edit_nonactive
        }

        val editInvoke: (() -> Unit)? = if (item.status != 2) {
            {
                onEditClick(item)
                bottomSheet.dismiss()
            }
        } else {
            null
        }

        val shareInvoke: (() -> Unit)? = if (item.status != 2) {
            { onShareClick(item.globalId.orEmpty(), item.externalLink) }
        } else {
            null
        }

        val copyLinkInvoke: (() -> Unit)? = if (item.status != 2) {
            {
                onCopyLinkClick(item.globalId.orEmpty(), item.externalLink)
                bottomSheet.dismiss()
            }
        } else {
            null
        }

        val deleteInvoke = {
            onDeleteClick(item.globalId.orEmpty())
            bottomSheet.dismiss()
        }

        val options = mutableListOf<KebabOption>()

        if (item.isUserNews.orFalse()) {
            options.add(
                KebabOption(
                    activity.getString(R.string.label_insightful),
                    imagesRes = insightfulIcon,
                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                    color = optionColor,
                    clickWithParam = {
                        it?.let { isActive ->
                            item.globalId?.let { id ->
                                onInsightfulClick(id, isActive as Boolean)
                            }
                        }
                    },
                    isActive = isActive
                )
            )
        }

        options.addAll(
            listOf(
                KebabOption(
                    activity.getString(R.string.label_share),
                    imagesRes = listOf(shareIcon),
                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                    color = optionColor,
                    click = shareInvoke
                ),
                KebabOption(
                    activity.getString(R.string.label_kebab_coly_link_video),
                    imagesRes = listOf(copyIcon),
                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                    color = optionColor,
                    click = copyLinkInvoke
                ),
//                KebabOption(
//                    activity.getString(R.string.label_kebab_edit_video),
//                    imagesRes = listOf(editIcon),
//                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
//                    color = optionColor,
//                    click = editInvoke
//                ),
                KebabOption(
                    activity.getString(R.string.label_kebab_delete_video),
                    imagesRes = listOf(R.drawable.ic_kebab_delete),
                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                    click = deleteInvoke
                )
            )
        )

        KebabItem(
            title = title,
            titleColorRes = null,
            options = options
        ).apply {
            bottomSheet.setKebabItem(this)
        }
    }

    fun createMyNewsKebabPrivate(
        activity: FragmentActivity,
        item: NewsItem,
        isActive: Boolean,
        onInsightfulClick: (String, Boolean) -> Unit,
        onEditClick: (NewsItem) -> Unit,
        onShareClick: (String, String?) -> Unit,
        onCopyLinkClick: (String, String?) -> Unit,
        onDeleteClick: (String) -> Unit,
    ) {
        val bottomSheet = KebabBottomSheet(KebabType.COMMON)
        bottomSheet.show(activity.supportFragmentManager, null)

        val title = if (item.status == 3) {
            null
        } else {
            activity.getString(R.string.label_kebab_private)
        }

        val optionColor = if (item.status == 3) {
            null
        } else {
            ContextCompat.getColor(activity, R.color.secondary_grey)
        }

        val insightfulIcon = if (item.status == 3) {
            listOf(
                R.drawable.ic_vector_insingfull,
                R.drawable.ic_vector_insingfull_active
            )
        } else {
            listOf(
                R.drawable.ic_kebab_insight
            )
        }

        val shareIcon = if (item.status == 3) {
            R.drawable.ic_kebab_share
        } else {
            R.drawable.ic_kebab_share_nonactive
        }

        val copyIcon = if (item.status == 3) {
            R.drawable.ic_kebab_link
        } else {
            R.drawable.ic_kebab_copy_link_nonactive
        }

        val editIcon = if (item.status == 3) {
            R.drawable.ic_kebab_edit
        } else {
            R.drawable.ic_kebab_edit_nonactive
        }

        val editInvoke: (() -> Unit)? = if (item.status == 3) {
            {
                onEditClick(item)
                bottomSheet.dismiss()
            }
        } else {
            null
        }

        val shareInvoke: (() -> Unit)? = if (item.status == 3) {
            { onShareClick(item.globalId.orEmpty(), item.externalLink) }
        } else {
            null
        }

        val copyLinkInvoke: (() -> Unit)? = if (item.status == 3) {
            {
                onCopyLinkClick(item.globalId.orEmpty(), item.externalLink)
                bottomSheet.dismiss()
            }
        } else {
            null
        }

        val deleteInvoke = {
            onDeleteClick(item.globalId.orEmpty())
            bottomSheet.dismiss()
        }

        val options = mutableListOf<KebabOption>()

        if (item.isUserNews.orFalse()) {
            options.add(
                KebabOption(
                    activity.getString(R.string.label_insightful),
                    imagesRes = insightfulIcon,
                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                    color = optionColor,
                    clickWithParam = {
                        it?.let { isActive ->
                            item.globalId?.let { id ->
                                onInsightfulClick(id, isActive as Boolean)
                            }
                        }
                    },
                    isActive = isActive
                )
            )
        }

        options.addAll(
            listOf(
                KebabOption(
                    activity.getString(R.string.label_share),
                    imagesRes = listOf(shareIcon),
                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                    color = optionColor,
                    click = shareInvoke
                ),
                KebabOption(
                    activity.getString(R.string.label_kebab_coly_link_video),
                    imagesRes = listOf(copyIcon),
                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                    color = optionColor,
                    click = copyLinkInvoke
                ),
//                KebabOption(
//                    activity.getString(R.string.label_kebab_edit_video),
//                    imagesRes = listOf(editIcon),
//                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
//                    color = optionColor,
//                    click = editInvoke
//                ),
                KebabOption(
                    activity.getString(R.string.label_kebab_delete_video),
                    imagesRes = listOf(R.drawable.ic_kebab_delete),
                    imagePaddingRes = R.dimen.margin_padding_size_large_pre_small_mid,
                    click = deleteInvoke
                )
            )
        )

        KebabItem(
            title = title,
            titleColorRes = null,
            options = options
        ).apply {
            bottomSheet.setKebabItem(this)
        }
    }

}