package com.vylo.signup.readyanimation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.lifecycle.lifecycleScope
import com.vylo.common.BaseFragment
import com.vylo.common.util.enums.ScreenType
import com.vylo.common.util.enums.UploadFileType
import com.vylo.signup.databinding.FragmentReadyAnimationBinding
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule


class ReadyAnimationFragment : BaseFragment<FragmentReadyAnimationBinding>() {
    private companion object Params {
        const val INITIAL_SCALE = 1f
        const val STIFFNESS = SpringForce.STIFFNESS_LOW
        const val DAMPING_RATIO = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
    }

    lateinit var scaleXAnimation: SpringAnimation
    lateinit var scaleYAnimation: SpringAnimation
    lateinit var alphaAnimation: SpringAnimation

    override fun getViewBinding() = FragmentReadyAnimationBinding.inflate(layoutInflater)

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
        startAnim()

        lifecycleScope.launch {
            Timer().schedule(4000) {
                requireActivity().runOnUiThread(Runnable {
                   navigateToMain()
                })
            }
        }
    }

    private fun startAnim() {
        scaleXAnimation = createSpringAnimation(
            viewBinder.logo, SpringAnimation.SCALE_X,
            INITIAL_SCALE, STIFFNESS, DAMPING_RATIO)

        scaleYAnimation = createSpringAnimation(
            viewBinder.logo, SpringAnimation.SCALE_Y,
            INITIAL_SCALE, STIFFNESS, DAMPING_RATIO)

        alphaAnimation = createSpringAnimation(
            viewBinder.logo, SpringAnimation.ALPHA,
            INITIAL_SCALE, STIFFNESS, DAMPING_RATIO)

        scaleYAnimation.apply {
            addEndListener(object : DynamicAnimation.OnAnimationEndListener {
                override fun onAnimationEnd(
                    animation1: DynamicAnimation<*>?,
                    canceled: Boolean,
                    value: Float,
                    velocity: Float
                ) {
                    viewBinder.logo.animate()
                        .scaleX(0.9f).scaleY(0.9f)
                        .alpha(0.7f)
                        .setDuration(200)
                        .setStartDelay(500)
                        .withEndAction {
                            scaleXAnimation.start()
                            scaleYAnimation.start()
                            alphaAnimation.start()
                        }
                }
            })
        }

        scaleXAnimation.start()
        scaleYAnimation.start()
        alphaAnimation.start()
    }

    private fun createSpringAnimation(view: View,
                                      property: DynamicAnimation.ViewProperty,
                                      finalPosition: Float,
                                      stiffness: Float,
                                      dampingRatio: Float): SpringAnimation {
        val animation = SpringAnimation(view, property)
        val spring = SpringForce(finalPosition)
        spring.stiffness = stiffness
        spring.dampingRatio = dampingRatio
        animation.spring = spring
        return animation
    }

    private fun navigateToMain() {
        setScreenType(ScreenType.MAIN)
        throwStartScreen()
        activity?.finish()
    }
}