package org.application.pcroombooking.dto

import com.google.gson.annotations.SerializedName
import org.application.pcroombooking.dto.parents.ResponseParent

data class EmailSendResponse(
    @SerializedName("httpStatus")
    val responseHttpStatus: Int,
    val responseCode: Int,
    val result: String,
    val responseMessage: String
)
