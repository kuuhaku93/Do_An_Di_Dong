package com.example.libraryoffreelancer.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val company_name: String,
    val phone_number: String,
    val website: String,
    val address: String,
    val employer_description: String,
    val email: String
)
@Serializable
data class EmployerProfile(
    val company_name: String,
    val company_logo: String = "",
    val email: String,
    val phone_number: String,
    val website: String = "",
    val address: String = "",
    val employer_description: String = "",
    val rating: Double
)

@Serializable
data class EmployerProfileResponse(
    val success: Boolean,
    val message: String="",
    val profile: EmployerProfile = EmployerProfile("","","","","","","",0.0)
)
@Serializable
data class EmployerReview(
    val review_id: Int,
    val comment: String,
    val score: Double,
    val created_at: String,
    val freelancer_id: Int,
    val freelancer_name: String,
    val freelancer_avatar: String? = null
)

@Serializable
data class EmployerReviewResponse(
    var success: Boolean,
    var reviews: List<EmployerReview> = emptyList(),
    var message: String? = null
)