package com.example.rentateamtest

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String) {
    Glide.with(imageView)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.loading_rotate)
                .error(R.drawable.ic_broken_image)
        )
        .into(imageView)
}