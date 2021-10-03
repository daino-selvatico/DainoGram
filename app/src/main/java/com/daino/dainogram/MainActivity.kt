package com.daino.dainogram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.daino.dainogram.databinding.ActivityMainBinding
import com.daino.libsgram.TelegramConfiguration
import it.tdlight.client.SimpleTelegramClient


class MainActivity : FragmentActivity(), View.OnClickListener {

    //static data
    /** Put your api data here **/
    companion object {
        const val API_ID = 0
        const val API_HASH = "PUT_YOUR_API_HASH_HERE"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var client: SimpleTelegramClient

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val frag: Fragment = Fragment(R.layout.fragment_welcome)
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.mainLayout, frag)
        fm.commit()

    }

    override fun onClick(v: View?) {
        Log.d("MainActivity", "Clicked")
        when (v?.id) {
            R.id.welcomeBtn -> {
                val frag: Fragment = Fragment(R.layout.fragment_phone)
                val fm = supportFragmentManager.beginTransaction()
                fm.replace(R.id.mainLayout, frag)
                fm.commit()
            }
            R.id.startBtn -> {
                try {
                    val phoneEditText = findViewById<EditText>(R.id.editTextPhone)
                    client = ClientFactory.buildClient(
                        applicationContext,
                        API_ID,
                        API_HASH,
                        phoneEditText.text.toString()
                    )
                    Thread.sleep(500)
//                    Toast.makeText(applicationContext, "Telegram Started", Toast.LENGTH_SHORT)
//                        .show()

                    TelegramConfiguration.getInstance().client = client
                    TelegramConfiguration.getInstance().authSemaphore.acquire()
                    TelegramConfiguration.getInstance().loggedStatusSemaphore.acquire()
                    if(TelegramConfiguration.getInstance().isNeedLogin) {
                        val frag: Fragment = Fragment(R.layout.fragment_code)
                        val fm = supportFragmentManager.beginTransaction()
                        fm.replace(R.id.mainLayout, frag)
                        fm.commit()
                    } else {
                        Toast.makeText(applicationContext, "No Need To Login", Toast.LENGTH_SHORT)
                            .show()
                        TelegramConfiguration.getInstance().loggedStatusSemaphore.release()
                        val intent: Intent = Intent(this, ChatListActivity::class.java)
                        startActivity(intent)
                    }

                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Cannot Start Telegram", Toast.LENGTH_SHORT)
                        .show()
                    e.message?.let { it1 -> Log.e("MainActivity", it1) }
                }
            }
            R.id.sencCodeBtn -> {
                val codeEditText = findViewById<EditText>(R.id.editTextCode)
                TelegramConfiguration.getInstance().authCode = codeEditText.text.toString()
                Toast.makeText(applicationContext, "Telegram Started", Toast.LENGTH_SHORT)
                    .show()
                TelegramConfiguration.getInstance().authSemaphore.release()

                TelegramConfiguration.getInstance().loggedStatusSemaphore.release()
                val intent: Intent = Intent(this, ChatListActivity::class.java)
                startActivity(intent)

            }
        }
    }
}