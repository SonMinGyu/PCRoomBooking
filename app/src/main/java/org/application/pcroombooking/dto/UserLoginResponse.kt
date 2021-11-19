package org.application.pcroombooking.dto

import com.google.gson.annotations.SerializedName
import org.application.pcroombooking.dto.parents.ResponseParent

data class UserLoginResponse(
    val jwtToken: String,
    @SerializedName("httpStatus")
    override val responseHttpStatus: Int,
    override val responseCode: Int,
    override val result: String,
    override val responseMessage: String
//    val email: String,
//    val authorities: Set<Authority>
) : ResponseParent(responseHttpStatus, responseCode, result, responseMessage)
