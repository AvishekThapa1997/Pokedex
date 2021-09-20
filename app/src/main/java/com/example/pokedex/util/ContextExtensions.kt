package com.example.pokedex.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun Context.getColorCode(colorCode : Int) = ContextCompat.getColor(this,colorCode)

fun Context.loadImage(
    imageUrl: String,
    containerView: ImageView,
    requiredPalette: Boolean = false,
    checkPaletteExistence: () -> Boolean = { false },
    coroutineScope: CoroutineScope? = null,
    dominantColorCode: ((Int) -> Unit)? = null
) {
    Glide.with(this).load(imageUrl).listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ) = false

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            if (requiredPalette) {
                if (!checkPaletteExistence()) {
                    coroutineScope?.run {
                        launch {
                            val bitmap = resource?.toBitmap()
                            bitmap?.let { mBitmap ->
                                val paletteRGB = Palette.from(mBitmap)
                                    .generate().dominantSwatch?.rgb
                                withContext(Dispatchers.Main) {
                                    dominantColorCode?.invoke(paletteRGB ?: 0)
                                }
                            }
                        }
                    }
                }
            }
            return false
        }
    }).into(containerView)
}