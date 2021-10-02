package com.daino.dainogram

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.daino.dainogram.databinding.ActivityMainBinding
import it.tdlight.client.SimpleTelegramClient


class MainActivity : Activity() {

    //static data
    /** Put your api data here **/
    companion object{
        const val API_ID = 0
        const val API_HASH = "PUT_YOUR_API_HASH_HERE"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var btn = findViewById<Button>(R.id.startBtn)
        btn.setOnClickListener {
            try {
                var client : SimpleTelegramClient = ClientFactory.buildClient(API_ID, API_HASH)
                Toast.makeText(applicationContext, "Telegram Started", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Cannot Start Telegram", Toast.LENGTH_SHORT).show()
                e.message?.let { it1 -> Log.e("MainActivity", it1) }
            }
        }
    }
}