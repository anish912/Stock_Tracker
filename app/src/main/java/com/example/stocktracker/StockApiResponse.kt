package com.example.stocktracker

import com.google.gson.annotations.SerializedName

data class StockApiResponse(
                             @SerializedName("Meta Data")
                             val metaData: MetaData,
                             @SerializedName("Monthly Time Series")
                             val monthlyTimeSeries: Map<String, StockData>?
)
