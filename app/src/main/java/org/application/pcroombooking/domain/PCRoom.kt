package org.application.pcroombooking.domain

data class PCRoom(
    var name: String,
    var buildingNumber: Int,
    var layer: Int,
    var allSeatNumber: Int,
    var pcSeatNumber: Int,
    var pcSeatBrokenNumber: Int,
    var pcSeatInUseNumber: Int,
    var pcSeatUseableNumber: Int,
    var notebookSeatNumber: Int,
    var notebookSeatBrokenNumber: Int,
    var notebookSeatInUseNumber: Int,
    var notebookSeatUseableNumber: Int,
    var seats: Set<Seat>,
    var enabled: Boolean
)
