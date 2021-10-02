package com.daino.dainogram

import android.app.Activity
import android.os.Bundle
import com.daino.dainogram.databinding.ActivityMainBinding
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.GetTextEntities
import java.util.concurrent.locks.Condition
import android.R.string.no
import java.util.concurrent.locks.ReentrantLock


class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}