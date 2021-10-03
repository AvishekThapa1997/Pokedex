package com.example.pokedex.util

import android.animation.ObjectAnimator
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.LinearProgressIndicator

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, imageUrl: String) {
    Glide.with(view).load(imageUrl)
        .into(view)
}

@BindingAdapter("animateProgress")
fun animateProgress(progress: LinearProgressIndicator, progressValue: Int) {
    ObjectAnimator.ofInt(progress, "progress", 0, progressValue)
        .setDuration(500L)
        .start()
}
