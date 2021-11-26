package org.application.pcroombooking.dto

import com.google.gson.annotations.SerializedName

data class UserRegisterResponse(
    val name: String,
    val email: String,
    @SerializedName("httpStatus")
    val responseHttpStatus: Int,
    val responseCode: Int,
    val result: String,
    val responseMessage: String
)
