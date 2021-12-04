package org.application.pcroombooking.domain

data class ConferenceRoomReservation(
    var conferenceRoomName: String,
    var conferenceRoom: ConferenceRoom?,
    var buildingNumber: Int,
    var locationName: String,
    var date: String,
    var startTime: Int,
    var endTime: Int,
    var reservationEmail: String,
    var reserved: Boolean,
    var enabled: Boolean
) {
    constructor(conferenceRoomName: String, startTime: Int, endTime: Int): this(
        conferenceRoomName,
        null,
        0,
        "",
        "",
        startTime,
        endTime,
        "",
        false,
        true
    )
}
