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
    val avatar:String = "https://tse4.mm.bing.net/th/id/OIP.xsKDhQCvjJdx9f2U-SIT5wHaHa?rs=1&pid=ImgDetMain&o=7&rm=3",
    val title: String=" ",
    val description: String = " ",
    val salary_max: Double,
    val salary_min: Double,
    val publish_date: String,
    val location:String = " ",
    val deadline:String,
    val max_employee:Int,
    val current_employee:Int,
    val requirements: List<String>,
    val is_applied: Boolean,
)

@Serializable
data class ListJobsRespone(
    val success: Boolean,
    val jobs: List<Job>,
    val message: String=" ",
)

@Serializable
data class Application(
    val id: Int,
    val freelancer_id: Int,
    val description: String,
    val wanted_salary: Double,
    val applied_date: String,
    val skills: List<String>,
    val is_applied: Boolean
)
@Serializable
data class ListApplicationsRespone(
    val success: Boolean,
    val applications: List<Application>,
    val message: String=" ",
)

@Serializable
data class EmployerJob(
    val id: Int,
    val employer_name: String,
    val avatar: String = "https://tse4.mm.bing.net/th/id/OIP.xsKDhQCvjJdx9f2U-SIT5wHaHa?rs=1&pid=ImgDetMain&o=7&rm=3",
    val title: String = " ",
    val description: String = " ",
    val salary_min: Double,
    val salary_max: Double,
    val location: String = " ",
    val deadline: String,
    val publish_date: String,
    val max_employee: Int,
    val current_employee: Int,
    val requirements: List<String>
): java.io.Serializable

@Serializable
data class EmployerJobResponse(
    val success: Boolean,
    val jobs: List<EmployerJob>,
    val message: String = " "
)

data class CreateJob(
    val title: String,
    val description: String,
    val salaryMin: Long,
    val salaryMax: Long,
    val location: String,
    val deadline: String,
    val endDate: String,
    val maxEmployee: Int,
    val requirements: List<Int>
)