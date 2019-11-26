package com.example.newsroom.view.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.newsroom.R
import com.example.newsroom.datalayer.model.Articles
import com.example.newsroom.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_news_list.*
//Class to show news list
class NewsList : Fragment() {
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.getData()

        refreshLayout.setOnRefreshListener {
            news_recycler_view.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            refreshLayout.isRefreshing = false
        }

        observeViewModel()


    }
    //Funtion to pass value to detail page
    fun detailsPage( view : View, article: Articles): Unit {
        var bundle = bundleOf("article_id" to article.uuid)
        Navigation.findNavController(view).navigate(R.id.action_newsList_to_newsDetail, bundle)
    }
//fountion to observe data value change
    private fun observeViewModel() {

        val controller = NewsController(::detailsPage)
        news_recycler_view.setController(controller);
        controller.setData(arrayListOf())

        viewModel.articles.observe(this, Observer { articles ->
            articles?.let {
                news_recycler_view.visibility = View.VISIBLE
                controller.setData(articles)
            }
        })


        viewModel.loading.observe(this, Observer { isError ->
            isError?.let {
                if (isError) {
                    listError.visibility = View.VISIBLE
                } else
                    listError.visibility = View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                if (isLoading) {
                    loadingView.visibility = View.VISIBLE
                    listError.visibility = View.GONE
                    news_recycler_view.visibility = View.VISIBLE
                } else
                    loadingView.visibility = View.GONE
            }
        })
    }
    //function to show settings menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_item,menu)
    }
        //function to know which setting option is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionSetting->{
                view?.let {
                    Navigation.findNavController(it).navigate(NewsListDirections.actionNewsListToSettingFragment())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
