package com.example.libraryoffreelancer.model

import kotlinx.serialization.Serializable

@Serializable
data class Skill(
    val id: Int,
    val name: String
)

@Serializable
data class Job(
    val id: Int,
    val employer_name:String,
    val avatar:String,
    val title: String,
    val description: String,
    val salalry_max: Double,
    val salalry_min: Double,
    val publish_date: String,
    val location:String,
    val dateline:String,
    val max_employee:Int,
    val current_employee:Int,
    val requirements: List<String>,
    val is_applied: Boolean,
)
