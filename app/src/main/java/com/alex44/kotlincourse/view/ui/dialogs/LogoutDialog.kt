package com.alex44.kotlincourse.view.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class LogoutDialog : DialogFragment() {

    companion object {
        val TAG = LogoutDialog::class.java.name + "_TAG"
        fun createDialog() = LogoutDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context)
                .setTitle("Выход из учетной записи")
                .setMessage("Вы уверены, что хотите разлогиниться")
                .setPositiveButton("Да") { _, _ -> (activity as LogoutListener).onLogout() }
                .setNegativeButton("Нет") { _, _ -> dismiss() }
                .create()

    interface LogoutListener {
        fun onLogout()
    }
}