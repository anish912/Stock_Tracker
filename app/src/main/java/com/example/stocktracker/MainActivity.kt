package com.example.stocktracker


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color
import android.util.Log
import androidx.cardview.widget.CardView


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: StockViewModel

    private lateinit var etStockSymbol: EditText
    private lateinit var btnSearch: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvCompanyName: TextView
    private lateinit var tvStockPrice: TextView
    private lateinit var tvPercentageChange: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(StockViewModel::class.java)

        etStockSymbol = findViewById(R.id.etStockSymbol)
        btnSearch = findViewById(R.id.btnSearch)
        progressBar = findViewById(R.id.progressBar)
        tvCompanyName = findViewById(R.id.tvCompanyName)
        tvStockPrice = findViewById(R.id.tvStockPrice)
        tvPercentageChange = findViewById(R.id.tvPercentageChange)

        btnSearch.setOnClickListener {
            val symbol = etStockSymbol.text.toString()
            if (symbol.isNotEmpty()) {
                fetchStockInfo(symbol)
            } else {
                Toast.makeText(this, "Please enter a stock symbol", Toast.LENGTH_SHORT).show()
            }
        }



        viewModel.stockData.observe(this, Observer { result ->


            when (result) {
                is Result.Loading -> {
                    progressBar.visibility = android.view.View.VISIBLE

                }
                is Result.Success -> {
                    Toast.makeText(this, "Stock data fetched successfully", Toast.LENGTH_LONG).show()
                    progressBar.visibility = android.view.View.GONE
                    tvCompanyName.text = result.data.companyName
                    tvStockPrice.text = result.data.price
                    tvPercentageChange.text = result.data.percentageChange



                }
                is Result.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()

                }
            }
        })

    }
    private fun fetchStockInfo(symbol: String) {
        viewModel.fetchStockInfo(symbol)
    }
}










