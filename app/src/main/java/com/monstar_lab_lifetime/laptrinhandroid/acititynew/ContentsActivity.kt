package com.monstar_lab_lifetime.laptrinhandroid.acititynew

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.monstar_lab_lifetime.laptrinhandroid.Interface.OnItemClick
import com.monstar_lab_lifetime.laptrinhandroid.R
import com.monstar_lab_lifetime.laptrinhandroid.model.MesData
import com.monstar_lab_lifetime.laptrinhandroid.model.Status
import kotlinx.android.synthetic.main.item_feed.*

class ContentsActivity : AppCompatActivity(),OnItemClick {
    var tabLayOut:TabLayout?=null
    var viewPager:ViewPager?=null
    var mList = mutableListOf<MesData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents)
        check()

        tabLayOut=findViewById(R.id.tabLayout)
        viewPager=findViewById(R.id.viewPager)
        tabLayOut!!.addTab(tabLayOut!!.newTab().setIcon(R.drawable.ic_group_7))
        tabLayOut!!.addTab(tabLayOut!!.newTab().setIcon(R.drawable.ic_group_mes))
        tabLayOut!!.addTab(tabLayOut!!.newTab().setIcon(R.drawable.ic_baseline_add_circle_24))
        tabLayOut!!.addTab(tabLayOut!!.newTab().setIcon(R.drawable.ic_notifications_black_24dp))
        tabLayOut!!.addTab(tabLayOut!!.newTab().setIcon(R.drawable.ic_baseline_view_headline_24))
        tabLayOut!!.tabGravity=TabLayout.GRAVITY_FILL


        val  contentsAdapter=ContentsAdapter(this,supportFragmentManager,tabLayOut!!.tabCount)
        viewPager!!.adapter=contentsAdapter


        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayOut))
        tabLayOut!!.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager!!.currentItem=tab!!.position
            }

        })

    }

    fun check(){
        val pr= ProgressDialog(this)
        val connectManager=this!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi=connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobileData=connectManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (wifi.isConnectedOrConnecting){

        }else{
            if (mobileData.isConnectedOrConnecting){
                pr.dismiss()
            }else{
                pr.show()
                Toast.makeText(this,"Không có internet !!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClicks(list: Status, position: Int) {

            Toast.makeText(this,"pjiv",Toast.LENGTH_LONG).show()

            var dem =list.countHeart.toInt()
            tv_countHeart?.let {  if (iv_heart.isChecked){
                dem++

                Toast.makeText(this,position.toString(), Toast.LENGTH_LONG).show()
                // tv_countHeart.setText(dem.toString())
            }
            }

    }
}