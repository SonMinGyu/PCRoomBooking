package org.application.pcroombooking.domain

import java.lang.reflect.Constructor

data class Seat(
    var pcRoom: PCRoom?,
    var pcroomName: String,
    var seatName: String,
    var user: User?,
    var userEmail: String,
    var seatType: String,
    var booked: Boolean,
    var isUsing: Boolean,
    var enabled: Boolean
) {
    constructor(
        pcroomName: String,
        seatName: String,
        userEmail: String,
        seatType: String,
        booked: Boolean,
        isUsing: Boolean,
        enabled: Boolean
    ): this(
        null,
        pcroomName,
        seatName,
        null,
        userEmail,
        seatType,
        booked,
        isUsing,
        enabled
    )
}
