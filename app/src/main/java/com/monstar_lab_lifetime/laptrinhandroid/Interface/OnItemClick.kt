package com.monstar_lab_lifetime.laptrinhandroid.Interface

import com.monstar_lab_lifetime.laptrinhandroid.model.FeedData

interface OnItemClick {
    fun onClicks(feedData: FeedData, position: Int)

}