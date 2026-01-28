package com.example.libraryoffreelancer.model

import kotlinx.serialization.Serializable


@Serializable
data class Portfolio(
    val freelancer_name: String,
    var avatar: String,
    val email: String,
    val phone_number: String,
    val description: String,
    val complete: Double,
    val rating: Double,
    val items: List<Item>,
    val skills: List<TypeSkill>
)
@Serializable
data class Item(
    val type: String,
    val type_id: Int,
    var default: List<DefaultItem>,
    val custom: List<CustomItem>
)
@Serializable
data class CustomItem(
    val type_id: Int,
    val title: String,
    val description: String,
    val icon: String?,
)

@Serializable
data class DefaultItem(
    val id: Int,
    val title: String,
    val description: String?,
    val icon: String?,
    var start_year: Int? = 2000,
    var end_year: Int? = 2000
)

@Serializable
data class ListItemResponse(
    val success: Boolean=false,
    val items: List<Item> = emptyList(),
    val message: String=""
)
@Serializable
data class ItemRequest(
    val id: Int,
    var start_year: Int,
    var end_year: Int
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

@Serializable
data class HistoryJob(
    val contact_id: Int,
    val job_title: String,
    val score: Double,
    val complete: Boolean,
    val company_name: String,
    val company_avatar: String,
    val employer_id: Int,
    val start_date: String,
    val end_date: String
)

@Serializable
data class ListHistory(
    val success: Boolean,
    val jobs: List<HistoryJob> = emptyList(),
    val message: String=""
)

@Serializable
data class CurrentJob(
    val contact_id: Int,
    val job_title: String,
    val company_id: Int,
    val is_done: Boolean,
    val is_rating: Boolean,
)

@Serializable
data class ListCurrent(
    val success: Boolean,
    val jobs: List<CurrentJob> = emptyList(),
    val message: String=""
)