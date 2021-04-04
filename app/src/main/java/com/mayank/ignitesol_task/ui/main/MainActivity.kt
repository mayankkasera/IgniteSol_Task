package com.mayank.ignitesol_task.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.mayank.ignitesol_task.R
import com.mayank.ignitesol_task.databinding.ActivityMainBinding
import com.mayank.ignitesol_task.ui.booklist.BooksActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        binding.cvFiction.setOnClickListener {
            goToBookList("fiction")
        }
        binding.cvDrama.setOnClickListener {
            goToBookList("drama")
        }
        binding.cvHumor.setOnClickListener {
            goToBookList("humor")
        }
        binding.cvPolitics.setOnClickListener {
            goToBookList("politics")
        }
        binding.cvPhilosophy.setOnClickListener {
            goToBookList("philosophy")
        }
        binding.cvHistory.setOnClickListener {
            goToBookList("history")
        }
        binding.cvAdventure.setOnClickListener {
            goToBookList("adventure")
        }
    }

    fun goToBookList(topic: String) {
        val intent = Intent(this, BooksActivity::class.java).apply {
            putExtra("topic", topic)
        }
        startActivity(intent)
    }
}