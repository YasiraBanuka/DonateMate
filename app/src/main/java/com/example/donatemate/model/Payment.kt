package com.example.donatemate.model

data class Payment(
    val userId:String,
    val type:String,
    val cardnumber: CharSequence,
    val name: String,
    val exDate: String,
    var cvcNo: CharSequence
)