package com.erabhidman.useractivitymonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.erabhidman.useractivitymonitor.databinding.ActivityTimelineBinding

class TimelineActivity : AppCompatActivity() {
    lateinit var _binding: ActivityTimelineBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline)

        shineAnimation()
    }

    private fun shineAnimation() {
        // attach the animation layout Using AnimationUtils.loadAnimation
        val anim = AnimationUtils.loadAnimation(this, R.anim.left_right)
        _binding.shine.startAnimation(anim)
        // override three function There will error
        // line below the object
        // click on it and override three functions
        anim.setAnimationListener(object : Animation.AnimationListener {
            // This function starts the
            // animation again after it ends
            override fun onAnimationEnd(p0: Animation?) {
                _binding.shine.startAnimation(anim)
            }

            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationRepeat(p0: Animation?) {}

        })
    }

}