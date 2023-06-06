package com.bignerdranch.android.sunset

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import com.bignerdranch.android.sunset.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val ANIMATION_FORWARD = 1
    private val ANIMATION_REVERSE = 2

    private var currentAnimation = ANIMATION_FORWARD

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sunSpin()
        binding.scene.setOnClickListener {

            if (currentAnimation == ANIMATION_FORWARD) {
                startForwardAnimation()
                currentAnimation = ANIMATION_REVERSE

            } else {
                startReverseAnimation()
                currentAnimation = ANIMATION_FORWARD

            }

        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForwardAnimation() {

            val currentSunY = binding.sun.y
            val sunYEnd = binding.sky.height.toFloat()

            val currentSunR = binding.sunRef.y
            val refEnd = binding.sun.top.toFloat()-300

        val currentSkyColor = binding.sky.background.mutate().current as ColorDrawable
        val currentBackgroundColor = currentSkyColor.color

            val heightAnimator = ObjectAnimator
                .ofFloat(binding.sun, "y", currentSunY, sunYEnd)
                .setDuration(3000)
            heightAnimator.interpolator = AccelerateInterpolator()

        val reflectionSun = ObjectAnimator
            .ofFloat(binding.sunRef, "y", currentSunR, refEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()



        val sunsetSkyAnimator = ValueAnimator.ofObject(
            ArgbEvaluator(),
            currentBackgroundColor,
            sunsetSkyColor,
            nightSkyColor
        ).apply {
            duration = 3000
            addUpdateListener { animation ->
                val color = animation.animatedValue as Int
                binding.sky.setBackgroundColor(color)
            }
        }



            val animatorSet = AnimatorSet()
            animatorSet.play(heightAnimator)
                .with(sunsetSkyAnimator)
                .with(reflectionSun)

            animatorSet.start()

    }


        @RequiresApi(Build.VERSION_CODES.O)
        private fun startReverseAnimation() {

            val refEnd = binding.sunRef.top.toFloat()
            var currentSunR = binding.sunRef.y


            val currentSunY = binding.sun.y
            val sunYEnd = binding.sun.top.toFloat()
            val currentSkyColor = binding.sky.background.mutate().current as ColorDrawable
            val currentBackgroundColor = currentSkyColor.color

                val heightAnimator = ObjectAnimator
                    .ofFloat(binding.sun, "y", currentSunY, sunYEnd)
                    .setDuration(3000)
                heightAnimator.interpolator = LinearInterpolator()


            val reflectionSun : ObjectAnimator
            val sunsetSkyAnimator: ValueAnimator
            when { (currentBackgroundColor==nightSkyColor) -> {

                sunsetSkyAnimator = ValueAnimator.ofObject(
                    ArgbEvaluator(),
                    currentBackgroundColor,
                    sunsetSkyColor,
                    blueSkyColor
                ).apply {
                    duration = 3000
                    addUpdateListener { animation ->
                        val color = animation.animatedValue as Int
                        binding.sky.setBackgroundColor(color)
                    }
                }
                    currentSunR = binding.sunRef.y - 300
                     reflectionSun = ObjectAnimator
                    .ofFloat(binding.sunRef, "y", currentSunR, refEnd)
                    .setDuration(3000)
                heightAnimator.interpolator = LinearInterpolator()

            }
                else -> {sunsetSkyAnimator = ValueAnimator.ofObject(
                    ArgbEvaluator(),
                    currentBackgroundColor,
                    blueSkyColor
                ).apply {
                    duration = 3000
                    addUpdateListener { animation ->
                        val color = animation.animatedValue as Int
                        binding.sky.setBackgroundColor(color)
                    }
                }
                    reflectionSun = ObjectAnimator
                        .ofFloat(binding.sunRef, "y", currentSunR, refEnd)
                        .setDuration(3000)
                    heightAnimator.interpolator = LinearInterpolator()}
            }


                val animatorSet = AnimatorSet()
                animatorSet.play(heightAnimator)
                    .with(sunsetSkyAnimator)
                    .with(reflectionSun)
                     animatorSet.start()

        }

        private fun sunSpin() {
            val sunAnimator = ObjectAnimator
                .ofFloat(binding.sun, "rotation", 0f, 360f)

            sunAnimator.repeatCount = 10000
            sunAnimator.interpolator = LinearInterpolator()
            sunAnimator.start()
        }


}