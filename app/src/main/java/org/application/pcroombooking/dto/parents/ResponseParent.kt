package org.application.pcroombooking.dto.parents

import com.google.gson.annotations.SerializedName

open class ResponseParent(
    @SerializedName("httpStatus")
    open val responseHttpStatus: Int,
    open val responseCode: Int,
    open val result: String,
    open val responseMessage: String
)