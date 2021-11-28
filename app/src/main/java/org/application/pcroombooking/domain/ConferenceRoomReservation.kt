package org.application.pcroombooking.domain

data class ConferenceRoomReservation(
    var conferenceRoomName: String,
    var buildingNumber: Int,
    var locationName: String,
    var layer: Int,
    var limit: Int,
    var startTime: Int,
    var endTime: Int,
    var reservationEmail: String
)
