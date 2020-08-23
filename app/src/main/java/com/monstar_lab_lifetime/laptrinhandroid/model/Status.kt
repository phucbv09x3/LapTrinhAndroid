package com.monstar_lab_lifetime.laptrinhandroid.model

class Status {
    var imgMy:String=""
    var imgAdd:String=""
    var nameMy:String=""
    var textAdd:String=""
    var uid:String=""
    var time:String=""
    constructor()
    constructor(imgMy:String,imgAdd:String,nameMy:String,textAdd:String,uid:String,time:String){
        this.imgMy=imgMy
        this.imgAdd=imgAdd
        this.nameMy=nameMy
        this.textAdd=textAdd
        this.uid=uid
        this.time=time
    }
}