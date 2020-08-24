package com.monstar_lab_lifetime.laptrinhandroid.model

class MesData {
    var name: String? = ""
    var image: String = ""
    var uid: String = ""
    var mail: String = ""

    constructor() {
    }

    constructor(name: String, image: String, uid: String, mail: String) {
        this.name = name
        this.image = image
        this.uid = uid
        this.mail = mail
    }

}