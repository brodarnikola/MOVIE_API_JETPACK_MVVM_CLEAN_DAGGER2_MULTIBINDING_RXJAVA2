package com.vjezba.androidjetpacknews.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vjezba.androidjetpacknews.INTRO_STRING_OBJECT
import com.vjezba.androidjetpacknews.ui.fragments.ViewPagerContentFragment

class NewsSlidePagerAdapter(
    fragment: Fragment,
    val listOfPagerContents: List<Array<String>>,
    val mPageNumbers :Int
) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int = mPageNumbers

    override fun createFragment(position: Int): Fragment {
        val fragment =
            ViewPagerContentFragment()

        fragment.arguments = Bundle().apply {
            putStringArray(INTRO_STRING_OBJECT, listOfPagerContents[position])
        }
        return fragment
    }
}