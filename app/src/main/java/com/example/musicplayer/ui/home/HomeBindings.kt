package com.example.musicplayer.ui.home

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.data.models.SessionObject

/**
 * @author Tomislav Curis
 */

@BindingAdapter("items")
fun setItems(listView: RecyclerView, items: List<SessionObject>?) {
    if (items == null) return

    (listView.adapter as HomeAdapter).submitList(items)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("itemText")
fun setItemGenre(textView: TextView, sessionObject: SessionObject) {
    if (sessionObject.genres.size < 1) return

    if (sessionObject.genres.size > 1) textView.text = sessionObject.genres[0] + ", "
    else textView.text = sessionObject.genres[0]

}

@SuppressLint("SetTextI18n")
@BindingAdapter("itemText2")
fun setItemGenre2(textView: TextView, sessionObject: SessionObject) {
    if (sessionObject.genres.size < 2) return

    if (sessionObject.genres.size > 2) textView.text = sessionObject.genres[1] + ", "
    else textView.text = sessionObject.genres[1]
}

@SuppressLint("SetTextI18n")
@BindingAdapter("itemText3")
fun setItemGenre3(textView: TextView, sessionObject: SessionObject) {
    if (sessionObject.genres.size  < 3) return

    textView.text = sessionObject.genres[2]
}

@BindingAdapter("imageUrl")
fun setGif(imageView: ImageView, url: String) {
    try {
        Glide
                .with(imageView.context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(80)))
                .into(imageView)
        imageView.clearFocus()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("app:searchResult")
fun fetchSearchResults(editText: EditText, queryListener: OnSearchTermListener) {
    val DUMMY_SEARCH = ""

    var lastText = ""

    editText.addTextChangedListener (
        object: TextWatcher {

        override fun afterTextChanged(p0: Editable?) {
            if (p0.toString() != lastText) {
                if (p0.toString() == "")
                    queryListener.onQuery(DUMMY_SEARCH)
                else
                    queryListener.onQuery(p0.toString())
                lastText = p0.toString()
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0 != lastText) {
                if (p0 == "")
                    queryListener.onQuery(DUMMY_SEARCH)
                else
                    queryListener.onQuery(p0.toString())
                lastText = p0.toString()
            }
        }
    } )
}

interface OnSearchTermListener {
    fun onQuery(query: String)
}