package com.example.musicplayer.utils.helpers

import com.example.musicplayer.R

/**
 * @author Tomislav Curis
 */
interface DialogManager {

    fun openOneButtonDialog(
        buttonTextId: Int = R.string.ok,
        textId: Int,
        cancelable: Boolean = false,
        onClickOk: (() -> Unit)? = null
    )

    fun openOneButtonDialog(
        buttonTextId: Int = R.string.ok,
        text: String,
        cancelable: Boolean = false,
        onClickOk: (() -> Unit)? = null
    )

    fun openOneButtonDialog(
        buttonTextId: Int = R.string.ok,
        title: String,
        message: String,
        cancelable: Boolean = false,
        onClickOk: (() -> Unit)? = null
    )

    fun dismissAll()

    fun isDialogShown() : Boolean
}