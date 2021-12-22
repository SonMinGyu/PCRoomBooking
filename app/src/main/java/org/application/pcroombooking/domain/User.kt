package org.application.pcroombooking.domain

import java.io.Serializable

data class User(
    var name: String,
    var email: String,
    var password: String,
    var studentNumber: String,
    var major: String,
    var seat: Seat,
    var seatBook: Boolean,
    var seatUse: Boolean,
    var enabled: Boolean
)