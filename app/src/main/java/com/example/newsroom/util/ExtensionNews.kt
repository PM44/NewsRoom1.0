package com.example.newsroom.util

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newsroom.R

@BindingAdapter("android:imageUrl")
fun loadImage2(view: ImageView, url: String?){
    url?.let { view.loadImageView(url, getProcessDrawable(view.context)) }
}



//Fountion to display image
fun getProcessDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}
//to create tag for the xml layout to show image in image view
fun ImageView.loadImageView(url: String, progressDrawable: CircularProgressDrawable) {

    Log.d("Parth", "$url")

    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.errorimage)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)

}