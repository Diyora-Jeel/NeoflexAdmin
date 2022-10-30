package com.nextininfotech.neoflexadmin.constants

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.nextininfotech.neoflexadmin.R

class CommonUtils {

    fun isNetworkAvailable(context: Context): Boolean
    {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    return true
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    return true
                }
            } else {
                return false
            }
            return false
    }

    fun createDialog( context: Context,icon: Int, title: String, body: String) {

        val customDialog = LayoutInflater.from(context).inflate(R.layout.dialog_item, null)
        val alert: AlertDialog.Builder = AlertDialog.Builder(context)
        alert.setView(customDialog)
        alert.setCancelable(false)

        val btnClose: TextView = customDialog.findViewById(R.id.btnClose) as TextView
        val imageView: ImageView = customDialog.findViewById(R.id.imageView) as ImageView
        val titleTv: TextView = customDialog.findViewById(R.id.title) as TextView
        val bodyTv: TextView = customDialog.findViewById(R.id.body) as TextView

        imageView.setImageResource(icon)
        titleTv.text = title
        bodyTv.text = body

        val dialog = alert.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun createCustomLoader(mContext: Context, isCancelable: Boolean): Dialog {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(isCancelable)
        dialog.setCanceledOnTouchOutside(isCancelable)
        dialog.setContentView(R.layout.custom_loader)

        //Grab the window of the dialog, and change the width
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
        return dialog
    }

    fun showCustomDialog(dialog: Dialog?, context: Context) {
        if (dialog != null) {
            if (!dialog.isShowing)
                if (!(context as Activity).isFinishing) {
                    dialog.show()
                }
        }
    }

    fun dismissCustomDialog(dialog: Dialog?) {
        if (dialog != null) {
            if (dialog.isShowing)
                dialog.dismiss()
        }
    }
}

