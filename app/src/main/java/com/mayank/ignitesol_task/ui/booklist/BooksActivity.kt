package com.mayank.ignitesol_task.ui.booklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mayank.ignitesol_task.R
import com.nehak.gutenberg_task.models.Book
import com.nehak.gutenberg_task.models.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksActivity : AppCompatActivity() {

    private val list = ArrayList<Book>()
    private val viewModel by viewModels<BooksViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.booksList.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.results?.let { list ->
                        Log.i("dshcbjsd", "subscribeUi: "+list)
                    }

                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                }

                Result.Status.LOADING -> {
                    Log.i("dshcbjsd", "LOADING: ")
                }
            }

        })
    }


    private fun showError(msg: String) {
        Log.i("dshcbjsd", "showError: "+msg)
//        Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
//        }.show()
    }
}