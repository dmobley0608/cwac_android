package com.dmobley0608.cwac.data.model

data class User(
    val firstname: String = "",
    val lastName: String = "",
    val role: String = "",
    val email: String?="",
    val key: String?=""

)