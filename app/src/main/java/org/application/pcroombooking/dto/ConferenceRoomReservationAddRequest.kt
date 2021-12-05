package org.application.pcroombooking.dto

data class ConferenceRoomReservationAddRequest(
    var conferenceRoomName: String,
    var date: String,
    var startTime: Int,
    var endTime: Int,
    var reservationEmail: String
)
