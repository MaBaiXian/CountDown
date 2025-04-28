package edu.gdpu.countdown.data

data class Exam(
    val id: Int = 0,
    val name: String,
    val targetDate: Long,
    val isPinned: Boolean = false
)