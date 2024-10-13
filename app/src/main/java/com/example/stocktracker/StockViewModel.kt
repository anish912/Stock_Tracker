package com.example.stocktracker

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StockViewModel : ViewModel() {
    private val _stockData = MutableLiveData<Result<StockInfo>>()
    val stockData: LiveData<Result<StockInfo>> get() = _stockData


    fun fetchStockInfo(symbol: String) {
        viewModelScope.launch {
            _stockData.value = Result.Loading
            try {

                val response = StockApi.retrofitService.getStockInfo(symbol=symbol)

                if (response.isSuccessful && response.body() != null) {


                    val stockInfo = parseStockData(response.body()!!)
                    if (stockInfo != null) {
                        _stockData.value = Result.Success(stockInfo)
                    } else {
                        _stockData.value = Result.Error("Failed to parse stock data")
                    }
                } else {
                    _stockData.value = Result.Error("Invalid Stock Symbol or API Error")
                }
            } catch (e: Exception) {
                _stockData.value = Result.Error("Network Error: ${e.message}")
            }
        }
    }


    private fun parseStockData(response: StockApiResponse): StockInfo? {
        val timeSeries = response.monthlyTimeSeries ?: return null
        val latestEntry = timeSeries.entries.firstOrNull() ?: return null

        val stockData = latestEntry.value
        val price = stockData.close
        val companyName = response.metaData.symbol

        val previousEntry = timeSeries.entries.elementAtOrNull(1)
        val previousClose = previousEntry?.value?.close?.toDoubleOrNull()
        val currentClose = price.toDoubleOrNull()

        val percentageChange = if (previousClose != null && currentClose != null) {
            String.format("%.3f", ((currentClose - previousClose) / previousClose) * 100)
        } else {
            "N/A"
        }

        return StockInfo(
            companyName = companyName,
            price = price,
            percentageChange = "$percentageChange%"
        )
    }

}

