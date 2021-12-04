package org.application.pcroombooking.dto

import com.google.gson.annotations.SerializedName
import org.application.pcroombooking.domain.ConferenceRoom

data class ConferenceRoomResponse(
    val conferenceRooms: MutableList<ConferenceRoom>,
    @SerializedName("httpStatus")
    val responseHttpStatus: Int,
    val responseCode: Int,
    val result: String,
    val responseMessage: String
    )
