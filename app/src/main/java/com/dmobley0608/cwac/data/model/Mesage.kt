package com.dmobley0608.cwac.data.model

//Message
data class Message(
    var key:String?="",
    var text:String?="",
    var date:Long?= System.currentTimeMillis(),
    var author:String?="",
    val senderId:String?=""
)