package com.vylo.signup.onboardingfragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vylo.signup.R
import com.vylo.signup.databinding.FragmentOnboardingBinding
import com.vylo.common.BaseFragment
import com.vylo.common.util.enums.ScreenType

class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>() {
    private val timelines = listOf(4000, 9000, 14000, 19000, 28000)
    override fun getViewBinding() = FragmentOnboardingBinding.inflate(layoutInflater)

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
    }

    override fun onResume() {
        super.onResume()
        beginning()
    }

    private fun beginning() {
        playVideo()

        viewBinder.videoView.setOnCompletionListener{
            if (!it.isPlaying) {
                next()
            }
        }

        viewBinder.touchViewForward.setOnClickListener {
            moveToNext()
        }

        viewBinder.touchViewBack.setOnClickListener {
            moveToPrevious()
        }
    }

    private fun playVideo() {
        val uri = Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.vylo_onboard_animation)
        viewBinder.videoView.setVideoURI(uri)
        viewBinder.videoView.start()
    }

    private fun moveToNext() {
        if (viewBinder.videoView.isPlaying) {
            val nextTimeline = timelines.find { it >  viewBinder.videoView.currentPosition }
            nextTimeline?.let {
                println(nextTimeline)
                viewBinder.videoView.seekTo(nextTimeline)
            }
        }
    }

    private fun moveToPrevious() {
        if (viewBinder.videoView.isPlaying) {
            val chapterStartTimeline = timelines.findLast { it <  viewBinder.videoView.currentPosition }
            chapterStartTimeline?.let {
                println(chapterStartTimeline)
                var prevTimeline = timelines.findLast { it <  chapterStartTimeline }
                if (prevTimeline == null) {
                    prevTimeline = chapterStartTimeline
                }
                viewBinder.videoView.seekTo(prevTimeline)
            }
        }
    }

    private fun next() {
        navigateTo(R.id.action_onboardingFragment_to_readyAnimationFragment)
    }

}