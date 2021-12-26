package org.application.pcroombooking.dto

data class SeatBookOrUseRequest(
    var pcroomName: String,
    var seatName: String,
    var userEmail: String,
    var viewTag: String,
    var booked: Boolean,
    var inuse: Boolean,
    var seatsStr: String,
)
