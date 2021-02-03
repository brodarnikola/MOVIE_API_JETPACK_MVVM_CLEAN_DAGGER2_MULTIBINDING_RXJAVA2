package com.vjezba.androidjetpacknews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vjezba.androidjetpacknews.INTRO_STRING_OBJECT
import com.vjezba.androidjetpacknews.R
import kotlinx.android.synthetic.main.activity_news_details.tvTitle
import kotlinx.android.synthetic.main.news_view_pager_content.*

class ViewPagerContentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.news_view_pager_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.takeIf { it.containsKey(INTRO_STRING_OBJECT) }?.apply {

            activity?.tvTitle?.text = getStringArray(INTRO_STRING_OBJECT)!![0]

            newsTitle.text = getStringArray(INTRO_STRING_OBJECT)!![0]
            Glide.with(this@ViewPagerContentFragment)
                .load(getStringArray(INTRO_STRING_OBJECT)!![1])
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .fallback(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivNewsPicture)
            newsDescription.text = getStringArray(INTRO_STRING_OBJECT)!![2]
        }

    }

}