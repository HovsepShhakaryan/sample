package com.vylo.main.navigation.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.tabs.TabLayoutMediator
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.FragmentAdapter
import com.vylo.common.entity.RespondData
import com.vylo.common.entity.UploadData
import com.vylo.common.util.DESTINATION_OF_NAVIGATION_FRAGMENT
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.common.util.UPLOAD_INFO
import com.vylo.common.util.enums.UploadFileType
import com.vylo.main.R
import com.vylo.main.categorymain.category.presentation.fragment.CategoryFragment
import com.vylo.main.component.sharedviewmodel.NavigationSharedViewModel
import com.vylo.main.databinding.FragmentNavigationBinding
import com.vylo.main.navigation.presentation.viewmodel.NavigationFragmentsViewModel
import com.vylo.main.responsemain.createaudio.presentation.fragment.CreateAudioFragment
import com.vylo.main.responsemain.createvideo.presentation.fragments.CaptureFragment
import com.vylo.main.responsemain.edit.presentation.fragment.EditFragment
import com.vylo.main.responsemain.upload.presentation.fragment.ThumbnailFragment
import com.vylo.main.responsemain.upload.presentation.fragment.UploadFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.concurrent.schedule


class NavigationFragment : BaseFragment<FragmentNavigationBinding>() {

    private val sharedViewModel by sharedViewModel<NavigationSharedViewModel>()
    private val viewModel by viewModel<NavigationFragmentsViewModel>()
    private var isDestinationProfile = false
    private var responseType: Int = 0
    override fun getViewBinding() = FragmentNavigationBinding.inflate(layoutInflater)

    companion object {

        @JvmStatic
        fun newInstance(): NavigationFragment {
            return NavigationFragment()
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                sharedViewModel.setUri(result.data!!.data, UploadFileType.VIDEO)
                Timer().schedule(1000) {
                    requireActivity().runOnUiThread(Runnable {
                        createFragments()
                    })
                }
            } else {
                backToPrevious()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onResume() {
        super.onResume()
        hideNavBar()
        getParametersData()
    }

    private fun openGalleryForPickVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_PICK
        resultLauncher.launch(intent)

    }

    private fun beginning() {
        val responseType = arguments?.getInt(RESPONSE_TYPE)
        this.responseType = responseType!!
        sharedViewModel.responseType = responseType!!

        if (responseType == 2) {
            openGalleryForPickVideo()
        } else {
            createFragments()
        }
    }

    private fun createFragments() {
        val uploadData =
            arguments?.getParcelable<UploadData>(
                UPLOAD_INFO
            )

        arguments?.getParcelable<RespondData>(RESPOND_INFO)?.let {
            sharedViewModel.setRespondData(it)
        }

        isDestinationProfile = arguments?.getBoolean(DESTINATION_OF_NAVIGATION_FRAGMENT)!!

        val fragmentAdapter = FragmentAdapter(this)

        when (responseType) {
            0 -> {
                fragmentAdapter.addFragment(
                    CaptureFragment(viewBinder.pager),
                    resources.getString(R.string.label_record_video)
                )
            }
            1 -> {
                fragmentAdapter.addFragment(
                    CreateAudioFragment(viewBinder.pager),
                    resources.getString(R.string.label_record_audio)
                )
            }
        }

        fragmentAdapter.addFragment(
            EditFragment(viewBinder.pager),
            resources.getString(R.string.label_edit_video_audio)
        )

        fragmentAdapter.addFragment(
            UploadFragment(viewBinder.pager, isDestinationProfile, uploadData),
            resources.getString(R.string.label_upload_video_audio)
        )

        if (responseType != 1) {
            fragmentAdapter.addFragment(
                ThumbnailFragment(viewBinder.pager),
                resources.getString(R.string.label_edit_thumbnail)
            )
        }

        fragmentAdapter.addFragment(
            CategoryFragment(viewBinder.pager),
            resources.getString(R.string.label_select_category)
        )

        viewBinder.pager.adapter = fragmentAdapter
        TabLayoutMediator(viewBinder.tabLayout, viewBinder.pager) { tab, position ->
            tab.text = fragmentAdapter.getPageTitle(position)
        }.attach()
        viewBinder.pager.isUserInputEnabled = false
    }

    private fun getParametersData() {
        viewModel.getVideoPresignData()
        viewModel.responseVideoParametersSuccess.observe(viewLifecycleOwner) { params ->
            if (params != null) {
                arguments?.getParcelable<RespondData>(RESPOND_INFO)?.let {
                    sharedViewModel.setParametersVideo(
                        params.copy(
                            responseToGlobalId = it.responseToGlobalId,
                            responseToTitle = it.responseToTitle,
                            responseCategoryId = it.responseCategoryId,
                            responseCategoryName = it.responseCategoryName
                        )
                    )
                } ?: run {
                    sharedViewModel.setParametersVideo(params)
                }
            }
        }
        viewModel.getAudioPresignData()
        viewModel.responseAudioParametersSuccess.observe(viewLifecycleOwner) { params ->
            if (params != null) {
                arguments?.getParcelable<RespondData>(RESPOND_INFO)?.let {
                    sharedViewModel.setParametersAudio(
                        params.copy(
                            responseToGlobalId = it.responseToGlobalId,
                            responseToTitle = it.responseToTitle,
                            responseCategoryId = it.responseCategoryId,
                            responseCategoryName = it.responseCategoryName
                        )
                    )
                } ?: run {
                    sharedViewModel.setParametersAudio(params)
                }
            }
        }

    }

    override fun onDestroy() {
        sharedViewModel.clearParameters()
        super.onDestroy()
    }

}