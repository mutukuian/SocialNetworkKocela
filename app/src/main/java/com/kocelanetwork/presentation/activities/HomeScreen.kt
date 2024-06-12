package com.kocelanetwork.presentation.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kocelanetwork.R
import com.kocelanetwork.domain.models.ImageItem
import com.kocelanetwork.presentation.adapters.ImageAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class HomeScreen : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_screen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        // Calculate time of day
        val calendar = Calendar.getInstance()
        val timeOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val greetingMessage = when (timeOfDay) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            else -> "Good Night"
        }

        val heyIanTextView: TextView = findViewById(R.id.hey_ian)
        heyIanTextView.text = greetingMessage


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val recyclerView2 = findViewById<RecyclerView>(R.id.recycler_view2)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        val imageList = listOf(
            ImageItem(R.drawable.image1),
            ImageItem(R.drawable.image2),
            ImageItem(R.drawable.image3),
            ImageItem(R.drawable.image1),
        )
        val snippersImages = listOf(
            ImageItem(R.drawable.image4),
            ImageItem(R.drawable.image5),
            ImageItem(R.drawable.image6),
            ImageItem(R.drawable.image1),
        )

        val adapter = ImageAdapter(imageList)
        recyclerView.adapter = adapter

        val adapter2 = ImageAdapter(snippersImages)
        recyclerView2.adapter =adapter2
    }
}