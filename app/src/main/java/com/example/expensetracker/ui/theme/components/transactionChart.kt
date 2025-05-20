package com.example.expensetracker.ui.theme.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.expensetracker.data.Transaction
import java.util.Calendar
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.ui.Alignment


@Composable
fun IncomeExpenseChart(transactions: List<Transaction>) {
    val last7DaysStats = remember(transactions) {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())

        // Create a map to hold day labels and their (income, expense) totals
        val dayLabels = mutableListOf<String>()
        val incomeTotals = mutableListOf<Double>()
        val expenseTotals = mutableListOf<Double>()

        for (i in 6 downTo 0) {
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.DAY_OF_YEAR, -i)

            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val label = formatter.format(calendar.time)
            dayLabels.add(label)

            val income = transactions.filter {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.date
                val txDay = cal.get(Calendar.DAY_OF_MONTH)
                val txMonth = cal.get(Calendar.MONTH)
                val txYear = cal.get(Calendar.YEAR)
                txDay == day && txMonth == month && txYear == year && it.type == "income"
            }.sumOf { it.amount.toDouble() }

            val expense = transactions.filter {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.date
                val txDay = cal.get(Calendar.DAY_OF_MONTH)
                val txMonth = cal.get(Calendar.MONTH)
                val txYear = cal.get(Calendar.YEAR)
                txDay == day && txMonth == month && txYear == year && it.type ==  "expense"
            }.sumOf { it.amount.toDouble() }

            incomeTotals.add(income)
            expenseTotals.add(expense)
        }

        Triple(dayLabels, incomeTotals, expenseTotals)
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // Set a fixed height
            .padding(8.dp),
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = true
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(false)
            }
        },
        update = { chart ->
            val (labels, incomeData, expenseData) = last7DaysStats

            val incomeEntries = incomeData.mapIndexed { index, value -> Entry(index.toFloat(),
                value.toFloat()
            ) }
            val expenseEntries = expenseData.mapIndexed { index, value -> Entry(index.toFloat(),
                value.toFloat()
            ) }

            val incomeSet = LineDataSet(incomeEntries, "Income").apply {
                color = Color.BLUE
                valueTextColor = Color.BLACK
                circleRadius = 4f
                lineWidth = 2f
            }

            val expenseSet = LineDataSet(expenseEntries, "Expense").apply {
                color = ColorTemplate.COLORFUL_COLORS[1]
                valueTextColor = Color.BLACK
                circleRadius = 4f
                lineWidth = 2f
            }

            chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            chart.data = LineData(listOf(incomeSet, expenseSet))
            chart.invalidate()
        }
    )
}