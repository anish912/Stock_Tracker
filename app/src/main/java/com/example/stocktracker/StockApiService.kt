package com.example.stocktracker

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface StockApiService {
    @GET("query?function=TIME_SERIES_MONTHLY&apikey=HNS40K35K4ASTM3K")
    suspend fun getStockInfo(


        @Query("symbol") symbol: String,


    ): Response<StockApiResponse>
}
