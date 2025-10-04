package com.cinemapalace.domain.models

enum class BookingStatus(val value: String) {
    FREE("FREE"),
    RESERVED("RESERVED"),
    BOOKED("BOOKED"),
    CANCELLED("CANCELLED");

    companion object {
        fun from(value: String): BookingStatus =
            entries.find { it.value.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown BookingStatus: $value")
    }
}