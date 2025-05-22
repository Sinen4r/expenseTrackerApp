package com.example.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Float,
    val type: String, // "income" or "expense" Ya houssemm!!
    val category: String,
    val date: Long ,
    val description: String=""
)