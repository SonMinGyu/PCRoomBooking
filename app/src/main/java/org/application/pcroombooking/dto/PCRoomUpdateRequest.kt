package org.application.pcroombooking.dto

import org.application.pcroombooking.domain.Seat

data class PCRoomUpdateRequest(
    var pcroomName: String,
    var seatsStr: String,
    var seats: MutableList<Seat>
)
