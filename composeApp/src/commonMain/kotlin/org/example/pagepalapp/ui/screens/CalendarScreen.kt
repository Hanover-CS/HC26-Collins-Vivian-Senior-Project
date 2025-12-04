@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.datetime.*
import org.example.pagepalapp.data.HomeViewModel
import org.example.pagepalapp.ui.components.StreakBanner

@Composable
fun CalendarScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {

    var currentMonth by remember {
        mutableStateOf(
            Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
                .let { LocalDate(it.year, it.month, 1) }
        )
    }

    // Collect calendar-related flows
    val readingDays by viewModel.readingLogs.collectAsState()
    val moodLogs by viewModel.moodLogs.collectAsState()
    val streak by viewModel.streak.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reading Calendar") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←", fontSize = 32.sp)
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

            // Streak banner
            if (streak >= 3) {
                StreakBanner(streak = streak)
                Spacer(Modifier.height(16.dp))
            }

            // Month header
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

            // Day labels
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("M", "T", "W", "T", "F", "S", "S").forEach {
                    Text(
                        it,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            CalendarMonthGrid(
                monthStart = currentMonth,
                readingDays = readingDays,
                moodLogs = moodLogs,
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
    moodLogs: Map<LocalDate, String>,
    onDayClick: (LocalDate) -> Unit
) {
    val year = monthStart.year
    val month = monthStart.month

    val firstDay = LocalDate(year, month, 1)
    val dayOfWeek = firstDay.dayOfWeek.ordinal // Monday = 0

    val isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
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
                    val date =
                        if (cellIndex >= dayOfWeek && dayNumber <= daysInMonth)
                            LocalDate(year, month, dayNumber)
                        else null

                    val isReadingDay = date != null && date in readingDays
                    val moodEmoji = date?.let { moodLogs[it] }

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
                                    moodEmoji != null ->
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)

                                    isReadingDay ->
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)

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

                                Text(
                                    date.dayOfMonth.toString(),
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Spacer(Modifier.height(2.dp))

                                // Mood emoji OR ⭐
                                when {
                                    moodEmoji != null -> {
                                        Text(
                                            moodEmoji,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }

                                    isReadingDay -> {
                                        Text(
                                            "⭐",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
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
