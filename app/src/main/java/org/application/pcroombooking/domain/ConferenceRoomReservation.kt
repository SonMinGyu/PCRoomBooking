package org.application.pcroombooking.domain

data class ConferenceRoomReservation(
    var conferenceRoomName: String,
    var conferenceRoom: ConferenceRoom?,
    var date: String,
    var startTime: Int,
    var endTime: Int,
    var reservationEmail: String,
    var reserved: Boolean,
    var enabled: Boolean
) {
    constructor(startTime: Int, endTime: Int): this(
        "",
        null,
        "",
        startTime,
        endTime,
        "",
        false,
        true
    )
}
