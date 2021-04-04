package com.mayank.ignitesol_task.ui.booklist

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.mayank.ignitesol_task.R
import com.mayank.ignitesol_task.databinding.ActivityBooksBinding
import com.mayank.ignitesol_task.util.OnBookItemClick
import com.nehak.gutenberg_task.models.Book
import com.nehak.gutenberg_task.models.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksActivity : AppCompatActivity(), OnBookItemClick {

    private lateinit var binding: ActivityBooksBinding
    private val viewModel by viewModels<BooksViewModel>()
    private lateinit var layoutManager: GridLayoutManager

    private var topic: String = ""
    private var page = 1
    private var isLoading: Boolean = false
    private val list = ArrayList<Book>()
    lateinit var adapter: BooksAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_books)

        init()

        subscribeUi()
    }

    private fun init(){
        topic = intent.getStringExtra("topic")!!

        // Toolbar :: Transparent
        binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setTitle(topic.capitalize())
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        layoutManager = GridLayoutManager(this, 3)
        binding.cvBooksList.layoutManager = layoutManager
        binding.cvBooksList.setHasFixedSize(true)
        adapter = BooksAdapter(list, this)
        binding.cvBooksList.adapter = adapter

        binding.cvBooksList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                if (isLoading == false) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == list.size - 1) {
                        isLoading = true
                        viewModel.fetchBooks(topic, page++)
                    }
                }

            }
        })

        binding.booksSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })

        viewModel.fetchBooks(topic,page)
    }

    private fun subscribeUi() {
        viewModel.booksList.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.results?.let { list ->
                        Log.i("dshcbjsd", "subscribeUi: "+list)

                        isLoading = false
                        binding.progressCircular.visibility = View.GONE
                        binding.loadMore.visibility = View.GONE
                        if (page == 1) {
                            this@BooksActivity.list.clear()
                            this@BooksActivity.list.addAll(list)
                            adapter.notifyItemInserted(list.size - 1)
                        } else {
                            this@BooksActivity.list.addAll(list)
                            adapter.notifyItemInserted(list.size - 1)
                        }
                    }

                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                }

                Result.Status.LOADING -> {


                    if (page == 1) {
                        binding.progressCircular.visibility = View.VISIBLE
                    } else {
                        binding.loadMore.visibility = View.VISIBLE
                    }
                }
            }

        })
    }


    private fun showError(msg: String) {
        Log.i("dshcbjsd", "showError: "+msg)
        binding.progressCircular.visibility = View.GONE
        binding.loadMore.visibility = View.GONE
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {}.show()
    }

    fun checkValidURL(url: String): Boolean {
        return url.contains(".zip", ignoreCase = true)
    }

    override fun onItemClick(position : Int) {
        val urlFormats = list.get(position).formats


        if (urlFormats?.application_pdf != null && !checkValidURL(urlFormats?.application_pdf)) {
            openBookInBrowser(urlFormats?.application_pdf)

        } else if (urlFormats?.text_plain_charset_utf_8 != null && !checkValidURL(urlFormats?.text_plain_charset_utf_8)) {
            openBookInBrowser(urlFormats?.text_plain_charset_utf_8)

        } else if (urlFormats?.text_html_charset_utf_8 != null && !checkValidURL(urlFormats?.text_html_charset_utf_8)) {
            openBookInBrowser(urlFormats?.text_html_charset_utf_8)
        } else {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("No viewable version available")
            alertDialogBuilder.setCancelable(true)

            alertDialogBuilder.setPositiveButton(
                getString(android.R.string.ok)
            ) { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    fun openBookInBrowser(url: String) {
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "text/html")
        startActivity(intent)
    }
}