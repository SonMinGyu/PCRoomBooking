package org.application.pcroombooking.dto

data class SeatUpdateRequest(
    var pcroomName: String,
    var seatName: String,
    var userEmail: String,
    var viewTag: String,
    var booked: Boolean,
    var inuse: Boolean
)
