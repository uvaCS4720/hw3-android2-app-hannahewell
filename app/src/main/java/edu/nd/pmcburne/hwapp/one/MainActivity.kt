package edu.nd.pmcburne.hwapp.one

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.nd.pmcburne.hwapp.one.ui.theme.HWStarterRepoTheme
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HWStarterRepoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val date = uiState.date
    val data = uiState.data
    Column(modifier = modifier) {
        Text(
            "View Basketball Game Data by Date!"
        )
        Spacer(modifier = modifier.height(8.dp))
        val newDate = dateTypePicker(
            date = date,
            onGameCall = { viewModel.loadGamesOnDate()}
        )
        if (newDate != date){
            viewModel.ChangeDate(newDate)
        }
        Text(data ?: "no data")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dateTypePicker(
    date: LocalDate,
    onGameCall: () -> Unit
):LocalDate{
    var mutableDate by remember {mutableStateOf("$date")}
    var gameType by remember {mutableStateOf("Game Type")}
    var expanded by remember { mutableStateOf(false) }
    var datePickerVisible by remember{ mutableStateOf(false)}
    var returnDate by remember {mutableStateOf(LocalDate.now())}
    Row(modifier = Modifier.padding(16.dp)){
        Column() {
            Button(onClick = { datePickerVisible = true }) {
                Text("Change date")
            }
            Text(mutableDate)
        }
        if (datePickerVisible){
            val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

            DatePickerDialog(
                onDismissRequest = {datePickerVisible = false},
                confirmButton = {
                    TextButton(onClick = {
                        val millis = datePickerState.selectedDateMillis ?: 0
                        val instant = Instant
                            .ofEpochMilli(millis)
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()
                        mutableDate = "$instant"
                        returnDate = instant
                        datePickerVisible = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { datePickerVisible = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        Box() {
            Button(onClick = { expanded = !expanded }
            ) {
                Text(gameType)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Men's Games") },
                    onClick = { gameType = "Men's"; expanded = false }
                )
                DropdownMenuItem(
                    text = { Text("Women's Games") },
                    onClick = { gameType = "Women's"; expanded = false }
                )
            }
        }
        if (gameType != "Game Type"){
            Button(onClick = {onGameCall()}){
                Text("See games!")
            }
        }
    }
    return returnDate
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    HWStarterRepoTheme {
        MainScreen(viewModel = MainViewModel())
    }
}