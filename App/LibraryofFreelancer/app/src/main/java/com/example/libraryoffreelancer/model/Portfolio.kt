package com.example.libraryoffreelancer.model

import kotlinx.serialization.Serializable


@Serializable
data class Portfolio(
    val freelancer_name: String,
    val avatar: String,
    val email: String,
    val phone_number: String,
    val description: String,
    val complete: Double,
    val rating: Double,
    val items: List<Item>,
    val skills: List<Map<String, List<String>>>
)
@Serializable
data class Item(
    val type: String,
    val default: List<DefaultItem>,
    val custom: List<DefaultItem>
)
@Serializable
data class DefaultItem(
    val title: String,
    val description: String?,
    val icon: String?,
    val start_year: Int? = null,
    val end_year: Int? = null
)

@Serializable
data class LoadPortfolioApiResponse(
    val success: Boolean,
    val portfolio: Portfolio=Portfolio("","","","","",0.0,0.0,emptyList(), emptyList()),
    val message: String=""
)

@Serializable
data class Rating(
    val rating_id: Int,
    val comment:String,
    val rating: Double,
    val complete: Boolean,
    val employer_avatar:String = "https://tse4.mm.bing.net/th/id/OIP.xsKDhQCvjJdx9f2U-SIT5wHaHa?rs=1&pid=ImgDetMain&o=7&rm=3",
    val employer_name: String=" ",
    val employer_id: Int,
    val created_at: String
)

@Serializable
data class ListRatingResponse(
    val success: Boolean,
    val ratings: List<Rating> =emptyList(),
    val message: String=""
)