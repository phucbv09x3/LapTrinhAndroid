package com.monstar_lab_lifetime.laptrinhandroid.model

class MesData {
    var name: String? =""
    var image: String =""
    var uid: String=""


    constructor() {
    }

    constructor(name: String, image: String, uid: String) {
        this.name = name
        this.image = image
        this.uid = uid
    }

}