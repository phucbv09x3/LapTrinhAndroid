package com.monstar_lab_lifetime.laptrinhandroid.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.monstar_lab_lifetime.laptrinhandroid.fragment.*

class ContentsAdapter(private val content:Context,fm:FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when(position){
            0->{
                return FeedFragment()
            }

            1->{
                return MessageFragment()
            }

            2->{
                return AddFragment()
            }
            3->{
                return NotifycationFragment()
            }
            4->{
                return TrangChuFragment()
            }
         else->return FeedFragment()
        }
    }

    override fun getCount(): Int {
       return  totalTabs
    }
}