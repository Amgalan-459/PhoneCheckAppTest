package com.example.phonecheckapp1.data.remote.dto

data class PhoneInfo(
    val status: String,
    val phone: String,
    val phone_valid: Boolean,
    val phone_type: String,
    val phone_region: String,
    val country: String,
    val country_code: String,
    val country_prefix: String,
    val international_number: String,
    val local_number: String,
    val e164: String,
    val carrier: String
)
