package com.dmobley0608.cwac.data.model

data class Show(
    var key:String? = "",
    var title:String?="",
    val dateAdded:Long?=System.currentTimeMillis(),
    val arrivalDate:String?="",
    val arrivalTime:String?="",
    var startDate:String?="",
    val endDate:String?="",
    val startTime:String?="",
    val endTime:String?="",
    val equipmentRequested:HashMap<String, String>?= HashMap<String, String>(),
    val locationRequested:String?="A Arena",
    val isChargedEvent:Boolean?=false,
    var showManager:String?=""
)
