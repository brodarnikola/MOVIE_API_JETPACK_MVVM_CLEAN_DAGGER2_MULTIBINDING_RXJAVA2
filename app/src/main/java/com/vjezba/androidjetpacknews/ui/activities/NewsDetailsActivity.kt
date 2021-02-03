package com.vjezba.androidjetpacknews.ui.activities

import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.vjezba.androidjetpacknews.R
import com.vjezba.androidjetpacknews.di.ViewModelFactory
import com.vjezba.androidjetpacknews.di.injectViewModel
import com.vjezba.androidjetpacknews.ui.adapters.NewsDetailsRecyclerViewAdapter
import com.vjezba.androidjetpacknews.ui.fragments.IntroViewPagerFragment
import com.vjezba.androidjetpacknews.ui.utilities.PagerNewsDetailsDecorator
import com.vjezba.androidjetpacknews.viewmodels.NewsDetailsViewModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_news_details.*
import javax.inject.Inject

class NewsDetailsActivity : AppCompatActivity(), HasActivityInjector, HasSupportFragmentInjector {

    var position = 0

    @Inject
    lateinit var dispatchingAndroidActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidActivityInjector

    @Inject
    lateinit var dispatchingAndroidFragmentInjector:  DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidFragmentInjector

    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var newsDetailsViewModel: NewsDetailsViewModel

    private var dataFetched = false


    private var newDetailsRecyclerViewAdapter: NewsDetailsRecyclerViewAdapter =
        NewsDetailsRecyclerViewAdapter(
            mutableListOf(),
        this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)

        newsDetailsViewModel = injectViewModel(viewModelFactory)
    }

    override fun onStart() {
        super.onStart()

        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        position = intent.getIntExtra("listPosition", 0)

        setupRecyclerViewProperties()
        setupFragmentProperties()

        radioGroupViewSelected.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radioButton: View = radioGroup.findViewById(checkedId)
            val index: Int = radioGroup.indexOfChild(radioButton)
            when (index) {
                0 -> {
                    fragmentData.visibility = View.VISIBLE
                    recylcerViewData.visibility = View.GONE
                }
                1 -> {
                    fragmentData.visibility = View.GONE
                    recylcerViewData.visibility = View.VISIBLE
                    getDataOnlyOnce()
                }
            }
        }

    }

    private fun setupRecyclerViewProperties() {
        recylcerViewData.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recylcerViewData.adapter = newDetailsRecyclerViewAdapter

        PagerSnapHelper().attachToRecyclerView(recylcerViewData)
        recylcerViewData.addItemDecoration(PagerNewsDetailsDecorator())
    }

    private fun setupFragmentProperties() {
        val fragment =
            IntroViewPagerFragment()
        val bundle = Bundle()
        bundle.putInt("listPosition",  position)
        fragment.setArguments(bundle)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentData, fragment).commit()

        fragmentData.visibility = View.GONE
    }

    private fun getDataOnlyOnce() {
        if( !dataFetched ) {

            progressBar.visibility = View.VISIBLE
            Log.d(ContentValues.TAG, "Da li ce uci sim BORUSIA MONCHEN GLADBACH: ")
            newsDetailsViewModel.newsDetailsList.observe(this, Observer { news ->

                progressBar.visibility = View.GONE
                dataFetched = true

                newDetailsRecyclerViewAdapter.updateDevices(news.toMutableList())
                recylcerViewData.scrollToPosition(position)
            })

            newsDetailsViewModel.getNewsFromLocalDatabaseRoom()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}