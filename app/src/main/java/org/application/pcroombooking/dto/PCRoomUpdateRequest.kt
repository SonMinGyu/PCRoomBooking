package org.application.pcroombooking.dto

data class PCRoomUpdateRequest(
    var pcroomName: String,
    var seatsStr: String,
    var seatName: String,
    var userEmail: String,
    var viewTag: String,
    var booked: Boolean,
    var inuse: Boolean
)
