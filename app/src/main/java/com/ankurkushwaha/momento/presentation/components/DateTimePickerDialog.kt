package com.ankurkushwaha.momento.presentation.components

/**
 * @author Ankur Kushwaha
 * Created on 20/05/2025 at 22:38
 */

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ankurkushwaha.momento.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * A composable dialog that allows the user to select both date and time using input fields,
 * returning a timestamp in milliseconds since epoch.
 *
 * @param isVisible whether the dialog is currently visible
 * @param onDismissRequest callback to invoke when the dialog is dismissed
 * @param onDateTimeSelected callback to invoke when a date and time are selected,
 *        providing the timestamp in milliseconds
 * @param initialDateTime optional initial date and time to display
 */

/**
 * Preview for DateTimePickerDialog
 */
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DateTimePickerDialogPreview() {
    MaterialTheme {
        // We need to wrap the dialog in a Box for preview
        Box(modifier = Modifier.fillMaxSize()) {
            DateTimePickerDialog(
                onDismissRequest = { /* Preview only */ },
                onDateTimeSelected = { /* Preview only */ },
                initialDateTime = LocalDateTime.of(2025, 5, 19, 14, 30)  // 2:30 PM on May 19, 2025
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerDialog(
    onDismissRequest: () -> Unit,
    onDateTimeSelected: (Long) -> Unit,
    initialDateTime: LocalDateTime = LocalDateTime.now()
) {
    // State to track the selected date and time
    var selectedDate by remember { mutableStateOf(initialDateTime.toLocalDate()) }
    var selectedTime by remember { mutableStateOf(initialDateTime.toLocalTime()) }

    // State to track which picker is currently active (date or time)
    var isDatePickerVisible by remember { mutableStateOf(true) }

    // Date and time formatters
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM dd, yyyy") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a") }


    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .padding(10.dp)
                .wrapContentSize()
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dialog title
                Text(
                    text = "Select Time & Date",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 16.dp, start = 16.dp)
                )

                // Toggle tabs for Date and Time
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isDatePickerVisible) MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                            .clickable { isDatePickerVisible = true }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_calendar),
                                contentDescription = "Date",
                                tint = if (isDatePickerVisible) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Date",
                                color = if (isDatePickerVisible) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (!isDatePickerVisible) MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                            .clickable { isDatePickerVisible = false }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_clock),
                                contentDescription = "Time",
                                tint = if (!isDatePickerVisible) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Time",
                                color = if (!isDatePickerVisible) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Display current selection
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${selectedDate.format(dateFormatter)} at ${
                            selectedTime.format(
                                timeFormatter
                            )
                        }",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Date or Time picker
                if (isDatePickerVisible) {
                    // Date Picker
                    val today = LocalDate.now()
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = selectedDate
                            .atStartOfDay()
                            .toInstant(ZoneOffset.UTC)
                            .toEpochMilli(),
                        selectableDates = object : SelectableDates {
                            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                val date =
                                    Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneOffset.UTC)
                                        .toLocalDate()
                                return !date.isBefore(today)
                            }
                        }
                    )

                    // Observe date changes
                    LaunchedEffect(datePickerState.selectedDateMillis) {
                        datePickerState.selectedDateMillis?.let {
                            val newDate = LocalDateTime
                                .ofEpochSecond(it / 1000, 0, ZoneOffset.UTC)
                                .toLocalDate()
                            selectedDate = newDate
                        }
                    }

                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        title = null,
                        headline = null,
                        modifier = Modifier.height(350.dp)
                    )
                } else {
                    // Time Picker Input only (no dial)
                    val timeState = rememberTimePickerState(
                        initialHour = selectedTime.hour,
                        initialMinute = selectedTime.minute,
                        is24Hour = false
                    )

                    // Update selectedTime when timeState changes
                    LaunchedEffect(timeState.hour, timeState.minute) {
                        selectedTime = LocalTime.of(timeState.hour, timeState.minute)
                    }

                    // Time Input (no dial)
                    TimeInput(
                        state = timeState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = TimePickerDefaults.colors(
                            timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                            timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }

                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val dateTime = LocalDateTime.of(selectedDate, selectedTime)

                            // ✅ Convert to Unix timestamp (milliseconds) using system's default timezone
                            val timestamp = dateTime
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()

                            // Pass the timestamp to callback
                            onDateTimeSelected(timestamp)
                            onDismissRequest()
                        }
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}