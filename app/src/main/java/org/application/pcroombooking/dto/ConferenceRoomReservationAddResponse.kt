package org.application.pcroombooking.dto

import com.google.gson.annotations.SerializedName
import org.application.pcroombooking.domain.ConferenceRoomReservation

data class ConferenceRoomReservationAddResponse(
    var conferenceRoomReservations: MutableList<ConferenceRoomReservation>,
    @SerializedName("httpStatus")
    var responseHttpStatus: Int,
    var responseCode: Int,
    var result: String,
    var responseMessage: String
)