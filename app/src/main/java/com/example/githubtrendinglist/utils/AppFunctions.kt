package com.example.githubtrendinglist.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.example.githubtrendinglist.R

object AppFunctions {


    private var alertDialog: AlertDialog? = null
    fun showAlertDialog(
        context: Context, message: String,
        positiveButtonName: String,
        negativeButtonName: String? = null,
        onPositiveClick: DialogInterface.OnClickListener? = null,
        onNegativeClick: DialogInterface.OnClickListener? = null,
        canCancel: Boolean = true
    ) {

        if (alertDialog != null) {
            alertDialog = null
        }

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.apply {
            setTitle(context.getString(R.string.message))
            setMessage(message)
            setPositiveButton(positiveButtonName, onPositiveClick)
            if (negativeButtonName != null)
                setNegativeButton(negativeButtonName, onNegativeClick)
            setCancelable(canCancel)
        }

        alertDialog = alertDialogBuilder.create()
        alertDialog!!.show()
    }
}