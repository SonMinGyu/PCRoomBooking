package org.application.pcroombooking.domain

data class ConferenceRoom(
    var name: String,
    var buildingNumber: Int,
    var locationName: String,
    var layer: Int,
    var limit: Int,
    var reservations: MutableList<ConferenceRoomReservation>,
    var allBooked: Boolean,
    var enable: Boolean
)
