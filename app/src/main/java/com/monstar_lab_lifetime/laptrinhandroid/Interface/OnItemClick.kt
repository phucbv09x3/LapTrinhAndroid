package com.monstar_lab_lifetime.laptrinhandroid.Interface

import com.monstar_lab_lifetime.laptrinhandroid.model.Status

interface OnItemClick {
    fun onClicks(list: Status, position: Int)

}