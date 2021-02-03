package com.vjezba.androidjetpacknews.ui.fragments


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.vjezba.androidjetpacknews.R
import com.vjezba.androidjetpacknews.di.Injectable
import com.vjezba.androidjetpacknews.di.ViewModelFactory
import com.vjezba.androidjetpacknews.di.injectViewModel
import com.vjezba.androidjetpacknews.ui.adapters.NewsSlidePagerAdapter
import com.vjezba.androidjetpacknews.viewmodels.NewsDetailsViewModel
import com.vjezba.domain.model.Articles
import kotlinx.android.synthetic.main.fragment_news_view_pager.*
import javax.inject.Inject


class IntroViewPagerFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var newsDetailsViewModel: NewsDetailsViewModel

    private var firstInitNewsPosition = 0

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment.id == R.id.fragmentData) {
            val b = Bundle()
            b.putInt("listPosition", 0)
            fragment.arguments = b
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        newsDetailsViewModel = injectViewModel(viewModelFactory)

        val b = arguments
        if (b != null) {
            firstInitNewsPosition = b.getInt("listPosition")
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar.visibility = View.VISIBLE
        newsDetailsViewModel.getNewsFromLocalDatabaseRoom()
    }

    override fun onStart() {
        super.onStart()

        newsDetailsViewModel.newsDetailsList.observe(viewLifecycleOwner, Observer { news ->

            progressBar.visibility = View.GONE

            val pagerAdapter =
                NewsSlidePagerAdapter(
                    this,
                    getListOfNewsPagerContents(news),
                    news.size
                )
            news_pager.adapter = pagerAdapter

            Handler(Looper.getMainLooper()).postDelayed({
                news_pager.setCurrentItem(firstInitNewsPosition, false)
            }, 100)

            TabLayoutMediator(tab_layout, news_pager)
            { tab, position -> }.attach()
        })
    }

    private fun getListOfNewsPagerContents(newsDetails: List<Articles>): List<Array<String>> {
        val articlesDetailsList = newsDetails.map { arrayOf(it.title, it.urlToImage, it.description) }
        return articlesDetailsList
    }

}
