package org.application.pcroombooking.dto

data class UserRegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val student_number: String,
    val major: String,
    val cryptogram: String
)
