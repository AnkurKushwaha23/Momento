package com.ankurkushwaha.momento.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/17 at 18:44
 */

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactDateTimePicker(
    initialDateTime: LocalDateTime = LocalDateTime.now(),
    onDateTimeSelected: (LocalDateTime) -> Unit,
    onDismiss: () -> Unit
) {
    val today = LocalDate.now()
    val startDate = today.minusDays(2)

    // Calculate initial values
    val initialDateOffset =
        ChronoUnit.DAYS.between(startDate, initialDateTime.toLocalDate()).toInt()

    // State for selection
    var selectedDateOffset by remember { mutableStateOf(initialDateOffset.coerceIn(0, 4)) }
    var selectedHour by remember { mutableStateOf(initialDateTime.hour) }
    var selectedMinute by remember { mutableStateOf(initialDateTime.minute) }
    var selectedAmPm by remember { mutableStateOf(if (initialDateTime.hour >= 12) "PM" else "AM") }

    // Convert 24-hour format to 12-hour format for display
    val displayHour = remember(selectedHour) {
        when {
            selectedHour == 0 -> 12
            selectedHour > 12 -> selectedHour - 12
            else -> selectedHour
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E1E1E), // Dark background
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header with current date and time
                Text(
                    text = SimpleDateFormat("EEEE, MMMM d, yyyy, h:mm a", Locale.getDefault())
                        .format(Date()),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Picker content
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Date column header (empty for alignment)
                    Box(modifier = Modifier.weight(1.5f)) {}

                    // Hour column header
                    Text(
                        text = "Hour",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )

                    // Minute column header
                    Text(
                        text = "Minute",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )

                    // AM/PM column header
                    Text(
                        text = "AM/PM",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Date and time selector rows
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    // Create 5 rows for dates (2 days before, current day, 2 days after)
                    for (offset in 0..4) {
                        val date = startDate.plusDays(offset.toLong())
                        val isSelectedDate = selectedDateOffset == offset

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clickable { selectedDateOffset = offset },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Date column
                            Box(
                                modifier = Modifier.weight(1.5f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                val dateText = if (date == today) {
                                    "Today"
                                } else {
                                    "MMM d".let { pattern ->
                                        SimpleDateFormat(pattern, Locale.getDefault())
                                            .format(date.toEpochDay() * 86400000)
                                    }
                                }

                                Text(
                                    text = dateText,
                                    color = if (isSelectedDate) Color(0xFF4CAF50) else Color.White,
                                    fontWeight = if (isSelectedDate) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            }

                            // Hour column
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelectedDate) {
                                    // Hour number picker
                                    NumberPickerColumn(
                                        value = displayHour,
                                        onValueChange = { newHour ->
                                            selectedHour = if (selectedAmPm == "AM") {
                                                if (newHour == 12) 0 else newHour
                                            } else {
                                                if (newHour == 12) 12 else newHour + 12
                                            }
                                        },
                                        range = 1..12,
                                        isSelected = isSelectedDate,
                                        formatter = { it.toString() }
                                    )
                                } else {
                                    // Just show hour number
                                    Text(
                                        text = displayHour.toString(),
                                        color = Color.Gray,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            // Minute column
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelectedDate) {
                                    // Minute number picker
                                    NumberPickerColumn(
                                        value = selectedMinute,
                                        onValueChange = { selectedMinute = it },
                                        range = 0..59,
                                        isSelected = isSelectedDate,
                                        formatter = { it.toString().padStart(2, '0') }
                                    )
                                } else {
                                    // Just show minute number
                                    Text(
                                        text = selectedMinute.toString().padStart(2, '0'),
                                        color = Color.Gray,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            // AM/PM column
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelectedDate) {
                                    // AM/PM toggle
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "AM",
                                            color = if (selectedAmPm == "AM") Color(0xFF4CAF50) else Color.Gray,
                                            fontWeight = if (selectedAmPm == "AM") FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 16.sp,
                                            modifier = Modifier.clickable {
                                                selectedAmPm = "AM"
                                                if (selectedHour >= 12) {
                                                    selectedHour -= 12
                                                }
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "PM",
                                            color = if (selectedAmPm == "PM") Color(0xFF4CAF50) else Color.Gray,
                                            fontWeight = if (selectedAmPm == "PM") FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 16.sp,
                                            modifier = Modifier.clickable {
                                                selectedAmPm = "PM"
                                                if (selectedHour < 12) {
                                                    selectedHour += 12
                                                }
                                            }
                                        )
                                    }
                                } else {
                                    // Just show AM/PM
                                    Text(
                                        text = selectedAmPm,
                                        color = Color.Gray,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("CANCEL", color = Color.Gray)
                    }

                    TextButton(
                        onClick = {
                            val selectedDate = startDate.plusDays(selectedDateOffset.toLong())
                            val finalHour = selectedHour

                            val selectedDateTime = LocalDateTime.of(
                                selectedDate.year,
                                selectedDate.month,
                                selectedDate.dayOfMonth,
                                finalHour,
                                selectedMinute
                            )

                            onDateTimeSelected(selectedDateTime)
                            onDismiss()
                        }
                    ) {
                        Text("CONFIRM", color = Color(0xFF4CAF50))
                    }
                }
            }
        }
    }
}

@Composable
fun NumberPickerColumn(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    isSelected: Boolean,
    formatter: (Int) -> String = { it.toString() }
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val textColor = if (isSelected) Color(0xFF4CAF50) else Color.White

        IconButton(
            onClick = {
                val newValue = if (value <= range.first) range.last else value - 1
                onValueChange(newValue)
            },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Increase",
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = formatter(value),
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.width(28.dp),
            textAlign = TextAlign.Center
        )

        IconButton(
            onClick = {
                val newValue = if (value >= range.last) range.first else value + 1
                onValueChange(newValue)
            },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Decrease",
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TaskBottomSheetPreview() {

    MaterialTheme {
        CompactDateTimePicker(
            onDateTimeSelected = { dateTime ->

            },
            onDismiss = { }
        )
    }
}