package com.example.expensetracker.ui.theme.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.expensetracker.data.AppDatabase
import com.example.expensetracker.data.Transaction
import com.example.expensetracker.data.TransactionDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar

class StatsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "finance-db"
    ).build()

    private val dao = db.transactionDao()
    init {
        viewModelScope.launch {
            if (dao.getTransactionCount() == 0) {
                addSampleData()
            }
        }
    }
    fun getRecentTransactions(): Flow<List<Transaction>> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startDate = calendar.timeInMillis

        return dao.getTransactionsBetweenDates(startDate, endDate)
    }
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            dao.insert(transaction)
        }
    }


    fun getIncomeTransactions(): Flow<List<Transaction>> {
        return dao.getTransactionsByType("income")
    }

    fun getExpenseTransactions(): Flow<List<Transaction>> {
        return dao.getTransactionsByType("expense")
    }

    suspend fun addSampleData() {
        val now = System.currentTimeMillis()
        val oneDay = 86400000L

        val sampleTransactions = listOf(
            // Income
            Transaction(amount = 1500f, type = "income", category = "Salary", date = now - oneDay * 6, description = "Monthly salary"),
            Transaction(amount = 200f, type = "income", category = "Freelance", date = now - oneDay * 4, description = "Freelance design job"),
            Transaction(amount = 100f, type = "income", category = "Gift", date = now - oneDay * 2, description = "Gift from friend"),

            // Expense
            Transaction(amount = 50f, type = "expense", category = "Groceries", date = now - oneDay * 5, description = "Supermarket"),
            Transaction(amount = 300f, type = "expense", category = "Food", date = now - oneDay * 3, description = "Lunch with friends"),
            Transaction(amount = 120f, type = "expense", category = "Transport", date = now - oneDay * 1, description = "Gas refill"),
            Transaction(amount = 80f, type = "expense", category = "Entertainment", date = now, description = "Movie ticket")
        )

        sampleTransactions.forEach { dao.insert(it) }
    }}