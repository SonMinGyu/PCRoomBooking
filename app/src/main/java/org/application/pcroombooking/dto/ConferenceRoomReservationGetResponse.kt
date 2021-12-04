package org.application.pcroombooking.dto

import com.google.gson.annotations.SerializedName
import org.application.pcroombooking.domain.ConferenceRoomReservation

data class ConferenceRoomReservationGetResponse(
    var conferenceRoomReservations: MutableList<ConferenceRoomReservation>,
    @SerializedName("httpStatus")
    val responseHttpStatus: Int,
    val responseCode: Int,
    val result: String,
    val responseMessage: String
)
