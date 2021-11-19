package org.application.pcroombooking.dto

import com.google.gson.annotations.SerializedName
import org.application.pcroombooking.dto.parents.ResponseParent

data class EmailSendResponse(
    @SerializedName("httpStatus")
    override val responseHttpStatus: Int,
    override val responseCode: Int,
    override val result: String,
    override val responseMessage: String
): ResponseParent(responseHttpStatus, responseCode, result, responseMessage)