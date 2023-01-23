package com.vylo.main.activity.presentation.activity

import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Secure
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vylo.common.BaseActivity
import com.vylo.common.BaseFragment
import com.vylo.common.BaseViewModel
import com.vylo.common.ShowHideNavBar
import com.vylo.common.ext.toGone
import com.vylo.common.ext.toVisible
import com.vylo.common.util.DEEP_LINK_ID
import com.vylo.common.util.GoogleAnalytics
import com.vylo.common.util.TITLE_OF_SHEET
import com.vylo.main.R
import com.vylo.main.activity.domain.entity.NotificationToken
import com.vylo.main.activity.presentation.viewmodel.MainFlowActivityViewModel
import com.vylo.main.component.bottomsheet.presentation.view.BottomSheetDialog
import com.vylo.main.component.sharedviewmodel.ActivityFragmentSharedViewModel
import com.vylo.main.databinding.ActivityMainFlawBinding
import com.vylo.main.followmain.followingfragment.presentation.fragment.FollowingSearchCategoryFragment
import com.vylo.main.followmain.followingfragment.presentation.fragment.FollowingSearchFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFlawActivity : BaseActivity<ActivityMainFlawBinding>(),
    ShowHideNavBar,
    BottomSheetDialog.BottomSheetCallBack {

    companion object {
        const val QUERY_NEWS_ID = "ngid"
        const val DEVICE_NAME = "Android device"
        const val MODEL_BOTTOM_SHEET = "ModalBottomSheet"
        const val PROFILE = "profile"
        const val NEWS_TAND = "newstand"
        const val VYLO = "vylo"
    }

    private val sharedViewModel: ActivityFragmentSharedViewModel by viewModels()
    private val viewModel by viewModel<MainFlowActivityViewModel>()
    private val baseViewModel: BaseViewModel by inject()
    override fun getViewBinding() = ActivityMainFlawBinding.inflate(layoutInflater)
    private lateinit var bottomSheet: BottomSheetDialog
    private val FragmentManager.currentNavigationFragment: Fragment?
        get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beginning()
    }

    private fun beginning() {
        val deviceId = Secure.getString(contentResolver, Secure.ANDROID_ID)

        viewModel.sendNotificationToken(
            NotificationToken(
                name = DEVICE_NAME,
                registration_id = getPushNotificationToken(),
                device_id = deviceId
            )
        )

        val navController = findNavController(R.id.fragment)
        viewBinder.bottomNavigationBar.setupWithNavController(navController)
        baseViewModel.setNavBarListener(this)
        viewBinder.maskButton.z = 100F
        viewBinder.maskButton.setOnClickListener {
            sharedViewModel.setRespondType(GoogleAnalytics.CREATE_POST)
            val titleSheet = resources.getString(R.string.label_create)
            openBottomSheetDialog(null, null, null, null, titleSheet)
        }
        FollowingSearchFragment.backPressHandler(this)
        FollowingSearchCategoryFragment.backPressHandler(this)

        redirectFromDeepLink()

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.parent!!.displayName) {
                getString(R.string.vylo_graph) -> baseViewModel.setMainGraph(false)
                getString(R.string.newsstand_graph) -> baseViewModel.setMainGraph(false)
                getString(R.string.trending_graph) -> baseViewModel.setMainGraph(false)
                getString(R.string.profile_graph) -> baseViewModel.setMainGraph(true)
            }
        }

    }

    private fun redirectFromDeepLink() {
        val bundle = intent.extras
        val deepLinkId = bundle?.getString(DEEP_LINK_ID)

        var pathSegments: List<String>? = null
        if (deepLinkId != null)
            pathSegments = Uri.parse(deepLinkId).pathSegments

        if (!pathSegments.isNullOrEmpty())
            when (pathSegments[0]) {
                PROFILE -> {
                    sharedViewModel.setDeepLinkId(pathSegments[1])
                    viewBinder.bottomNavigationBar.selectedItemId = R.id.profile_main_graph
                }
                NEWS_TAND -> {
                    sharedViewModel.setDeepLinkId(pathSegments[1])
                    viewBinder.bottomNavigationBar.selectedItemId = R.id.news_stand_main_graph
                }
                VYLO -> {
                    sharedViewModel.setDeepLinkId(pathSegments[1])
                    viewBinder.bottomNavigationBar.selectedItemId = R.id.vylo_main_graph
                }
                else -> {
                    val queryId = Uri.parse(deepLinkId).getQueryParameter(QUERY_NEWS_ID)
                    sharedViewModel.setDeepLinkId(queryId)
                    viewBinder.bottomNavigationBar.selectedItemId = R.id.vylo_main_graph
                }
            }
        else
            viewBinder.bottomNavigationBar.selectedItemId = R.id.category_main_graph
    }

    fun openBottomSheetDialog(
        id: String?,
        title: String?,
        categoryId: String?,
        categoryName: String?,
        titleOfSheet: String
    ) {
        val bundle = Bundle()
        bundle.putString(TITLE_OF_SHEET, titleOfSheet)
        bottomSheet = BottomSheetDialog(sharedViewModel)
        bottomSheet.arguments = bundle
        bottomSheet.show(
            supportFragmentManager,
            MODEL_BOTTOM_SHEET
        )
        bottomSheet.setNewsID(id)
        bottomSheet.setNewsTitle(title)
        bottomSheet.setCategoryId(categoryId)
        bottomSheet.setCategoryName(categoryName)
        bottomSheet.setBottomSheetListener(this)
    }

    override fun showProgress() {
        viewBinder.progressBar.show()
        freesScreen()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideProgress()
        showNavBar()
    }

    override fun hideProgress() {
        viewBinder.progressBar.hide()
        unFreesScreen()
    }

    override fun hideNavBar() {
        viewBinder.bottomNavigationBar.toGone()
    }

    override fun showNavBar() {
        viewBinder.bottomNavigationBar.toVisible()
    }

    fun navigateTo(id: Int) {
        viewBinder.bottomNavigationBar.selectedItemId = id
    }

    override fun openResponseScreen(responseType: Int) {
        bottomSheet.dismiss()

        val currentFragment = supportFragmentManager.currentNavigationFragment
        if (currentFragment is BaseFragment<*>)
            currentFragment.openCreateResponseScreen(
                bottomSheet.getNewsID(),
                responseType,
                bottomSheet.getNewsTitle(),
                bottomSheet.getCategoryId(),
                bottomSheet.getCategoryName()
            )
    }

    fun smoothContentToTop(block: () -> Unit) {
        viewBinder.bottomNavigationBar.setOnItemReselectedListener {
            block.invoke()
        }
    }
}