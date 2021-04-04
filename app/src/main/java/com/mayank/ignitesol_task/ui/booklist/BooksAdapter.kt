package com.mayank.ignitesol_task.ui.booklist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mayank.ignitesol_task.BuildConfig
import com.mayank.ignitesol_task.R
import com.mayank.ignitesol_task.util.OnBookItemClick
import com.nehak.gutenberg_task.models.Book
import java.util.*
import kotlin.collections.ArrayList

class BooksAdapter(booksList: ArrayList<Book>, listner: OnBookItemClick) :
    RecyclerView.Adapter<BooksAdapter.BooksViewHolder>(),
    Filterable {
    var mBookList: ArrayList<Book> = booksList
    var booksFilterList = mBookList
    var mListner = listner


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.book_card, parent, false)
        return BooksViewHolder(v)
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        holder.book_name?.setText(booksFilterList.get(position).title)
        if (!booksFilterList.get(position).authors.isEmpty()) {
            holder.auther_name?.setText(booksFilterList.get(position).authors.get(0).name)
        }

        if (booksFilterList.get(position).id != null) {
            holder.iv_default_thumbnail!!.visibility = View.GONE
            setImageUrlWithId(holder.iv_thumbnail!!,booksFilterList.get(position).id)
        } else {
            holder.iv_default_thumbnail!!.visibility = View.VISIBLE

        }

        holder.itemView.setOnClickListener {
            mListner.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return booksFilterList.size
    }

    class BooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var book_name: TextView? = null
        var auther_name: TextView? = null
        var iv_thumbnail: ImageView? = null
        var iv_default_thumbnail: ImageView? = null

        init {
            book_name = itemView.findViewById(R.id.bookName)
            auther_name = itemView.findViewById(R.id.autherName)
            iv_thumbnail = itemView.findViewById(R.id.iv_thumbnail)
            iv_default_thumbnail = itemView.findViewById(R.id.iv_default_thumbnail)
        }
    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    booksFilterList = mBookList
                } else {
                    val resultList = ArrayList<Book>()
                    for (row in mBookList) {

                        if (row.title.toLowerCase(Locale.ROOT).contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            )
                        ) {
                            resultList.add(row)
                        }
                    }
                    booksFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = booksFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                booksFilterList = results?.values as ArrayList<Book>
                notifyDataSetChanged()
            }

        }
    }


    val RADIUS = 10;

    fun setImageUrl(imgView: ImageView, imgUrl: String?) {

        imgUrl?.let {
            val imgUri = it.toUri().buildUpon().scheme("https").build()
            val requestOptions = RequestOptions().transform(MultiTransformation(CenterCrop(), RoundedCorners(RADIUS))).placeholder(R.color.colorGrey)
                .error(R.color.colorGrey)
            Glide.with(imgView.context)
                .load(imgUri)
                .apply(requestOptions)
                .into(imgView)
        }
    }


    fun setImageUrlWithId(imgView: ImageView, id: Int?) {
        // Example : https://www.gutenberg.org/cache/epub/829/pg829.cover.medium.jpg
        val formedUrl = BuildConfig.IMAGE_BASE_URL + id + "/pg" + id + ".cover.medium.jpg";
        Log.i("sdklcnd", "setImageUrlWithId: "+formedUrl)
        setImageUrl(imgView, formedUrl)
    }


}