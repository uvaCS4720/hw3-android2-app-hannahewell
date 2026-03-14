package edu.nd.pmcburne.hwapp.one

import android.widget.DatePicker
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.util.Calendar
import kotlinx.serialization.*
import kotlinx.coroutines.launch
import edu.nd.pmcburne.hwapp.one.network.API
import java.io.IOException

data class MainUIState(
    val date: LocalDate,
    val url: String?,
    val data: String?
)

class MainViewModel(
    val date: LocalDate = LocalDate.now(),
    val today: Calendar = Calendar.getInstance()
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUIState(date = date,null,null))
    val uiState: StateFlow<MainUIState> = _uiState.asStateFlow()


    fun ChangeDate(newDate: LocalDate){
        _uiState.update{ currentState -> currentState.copy(date = newDate)}
    }

    fun loadGamesOnDate(){
        // load games from API
        val year = _uiState.value.date.year
        val month = _uiState.value.date.month
        val day = _uiState.value.date.dayOfMonth
        val url = "$year/$month/$day"
        _uiState.update { currentState-> currentState.copy(url = url) }
        getData()
    }

    private fun getData(){
        viewModelScope.launch {
            try{
                val listResult = API.retrofitService.getData(_uiState.value.url ?: "")
                _uiState.update{currentState -> currentState.copy(data = listResult)}
            }catch(e: IOException){
                if (_uiState.value.data == null){
                    _uiState.update{currentState -> currentState.copy(data = "Error fetching data. Please check your internet connection and refresh.")}
                }
            }
        }
    }
}