package org.application.pcroombooking.dto

import org.application.pcroombooking.domain.Seat

data class PCRoomAddRequest(
    var name: String,
    var buildingNumber: Int,
    var layer: Int,
    var pcRoomLine: Int,
    var pcRoomRow: Int,
    var seatsStr: String,
    var allSeatNumber: Int,
    var pcSeatNumber: Int,
    var notebookSeatNumber: Int,
    var enabled: Boolean,
)
