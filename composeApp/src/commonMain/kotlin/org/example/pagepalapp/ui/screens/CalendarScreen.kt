package org.example.pagepalapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*
import org.example.pagepalapp.data.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: HomeViewModel) {

    var currentMonth by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                .let { LocalDate(it.year, it.month, 1) }
        )
    }

    val readingDays by viewModel.readingDays.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reading Calendar") },
                navigationIcon = {
                    IconButton(onClick = { /* add back if needed */ }) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // month header with navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "←",
                    modifier = Modifier.clickable {
                        currentMonth = currentMonth.minus(DatePeriod(months = 1))
                    }
                )

                Text(
                    "${currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${currentMonth.year}",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    "→",
                    modifier = Modifier.clickable {
                        currentMonth = currentMonth.plus(DatePeriod(months = 1))
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            // days of the week
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("M", "T", "W", "T", "F", "S", "S").forEach {
                    Text(
                        it,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // calendar grid
            CalendarMonthGrid(
                monthStart = currentMonth,
                readingDays = readingDays,
                onDayClick = { date ->
                    viewModel.toggleReadingDay(date)
                }
            )
        }
    }
}

@Composable
fun CalendarMonthGrid(
    monthStart: LocalDate,
    readingDays: Set<LocalDate>,
    onDayClick: (LocalDate) -> Unit
) {
    val month = monthStart.month
    val year = monthStart.year

    // find the first day of the month
    val firstDay = LocalDate(year, month, 1)
    val dayOfWeek = firstDay.dayOfWeek.ordinal // Monday=0 … Sunday=6

    // determine leap year manually (Kotlinx DateTime does not expose Year.isLeap)
    val isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

    // # of days in each month
    val daysInMonth = when (month) {
        Month.JANUARY -> 31
        Month.FEBRUARY -> if (isLeap) 29 else 28
        Month.MARCH -> 31
        Month.APRIL -> 30
        Month.MAY -> 31
        Month.JUNE -> 30
        Month.JULY -> 31
        Month.AUGUST -> 31
        Month.SEPTEMBER -> 30
        Month.OCTOBER -> 31
        Month.NOVEMBER -> 30
        Month.DECEMBER -> 31
    }

    Column {

        var dayNumber = 1
        val totalCells = ((dayOfWeek + daysInMonth + 6) / 7) * 7

        for (row in 0 until totalCells step 7) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                for (col in 0 until 7) {
                    val cellIndex = row + col
                    val date = if (cellIndex >= dayOfWeek && dayNumber <= daysInMonth)
                        LocalDate(year, month, dayNumber)
                    else null

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .clickable(enabled = date != null) {
                                if (date != null) onDayClick(date)
                            }
                            .background(
                                when {
                                    date != null && date in readingDays ->
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)   // cute pastel circle
                                    else -> Color.Transparent
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                // Day number
                                Text(
                                    date.dayOfMonth.toString(),
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                if (date in readingDays) {
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        "⭐",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                    }

                    if (date != null) dayNumber++
                }
            }
        }
    }
}
