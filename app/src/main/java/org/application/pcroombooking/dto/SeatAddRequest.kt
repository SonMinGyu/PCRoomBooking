package org.application.pcroombooking.dto

import org.application.pcroombooking.domain.Seat

data class SeatAddRequest(
    var pcroomName: String,
    var seats: MutableList<Seat>
)
