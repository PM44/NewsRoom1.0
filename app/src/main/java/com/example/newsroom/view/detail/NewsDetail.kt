package com.example.newsroom.view.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.newsroom.datalayer.model.Articles
import com.example.newsroom.datalayer.model.NewsPallete
import com.example.newsroom.viewmodel.DetailViewModel
import android.content.Intent
import android.net.Uri


class NewsDetail : Fragment() {

    private lateinit var databinding: com.example.newsroom.databinding.FragmentNewsDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var currentArticle: Articles? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //activity?.actionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databinding = DataBindingUtil.inflate(
            inflater,
            com.example.newsroom.R.layout.fragment_news_detail, container, false
        )
        return databinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val uuid: Int = it.getInt("article_id")
            viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
            viewModel.fetch(uuid)
            observeViewModel()

        }


    }

    private fun readNews() {
        val uri = Uri.parse(currentArticle?.url) // missing 'http://' will cause crashed
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.articleList.observe(this, Observer { articles: Articles ->
            currentArticle = articles
            articles?.let {
                databinding.news = articles
                databinding.setOnclickListner {
                    readNews();
                }

                it.urlToImage?.let {
                    backgroundColor(it)
                }
            }
        })
    }

    //to set background of the detail page using pallete library of jetpack
    private fun backgroundColor(url: String?) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                @SuppressLint("ResourceAsColor")
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val intColor = palette?.getLightMutedColor((com.example.newsroom.R.color.white))
                            val myPallete = intColor?.let { NewsPallete(it) }
                            databinding.newsPallete = myPallete
                        }
                }

            })
    }
}


