package com.ziaad.notecollege.model

class firebasemodel {

    var title: String?=null
    var content: String?=null


    constructor(){

    }

    constructor(title:String,content:String){

        this.title = title
        this.content=content


    }
}