package com.example.libraryoffreelancer.model

import kotlinx.serialization.Serializable

@Serializable
data class Skill (
    val id:Int,
    val skill_name: String
)

@Serializable
data class TypeSkill(
    val type:String,
    val skills:List<Skill>
)

@Serializable
data class ListSkillsRespone(
    val success:Boolean,
    val skills:List<TypeSkill>,
    val message:String=""
)
