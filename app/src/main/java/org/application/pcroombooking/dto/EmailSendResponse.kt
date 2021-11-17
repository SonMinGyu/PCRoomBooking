package org.application.pcroombooking.dto

data class EmailSendResponse(
    val resultCode: Int,
    val result: String,
    val message: String
)
