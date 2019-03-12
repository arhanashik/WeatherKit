package com.workfort.demo.util.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class JavaDatePickerDialog : DialogFragment() {
    private var onDateSetListener: DatePickerDialog.OnDateSetListener? = null

    lateinit var date: Date
    private var calender: Calendar = Calendar.getInstance()
    var onOk: ((date: Date) -> Unit)? = null
    var onCancel: (() -> Unit)? = null

    companion object {
        private const val TAG = "JavaDatePickerDialog"

        private const val EXTRA_DATE = "date"

        fun newInstance(date: Date? = null): JavaDatePickerDialog {
            val dialog = JavaDatePickerDialog()
            val args = Bundle().apply {
                putSerializable(EXTRA_DATE, date)
                /*
                date?.also {
                    putLong(EXTRA_DATE, it.atStartOfDay(ZoneId.systemDefault()).toEpochSecond())
                }
                 */
            }
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        date = arguments?.getSerializable(EXTRA_DATE) as Date? ?: Date()
        calender.time = date

        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener {
                _, year, monthOfYear, dayOfMonth ->
            calender.set(Calendar.YEAR, year)
            calender.set(Calendar.MONTH, monthOfYear)
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            this.date = calender.time
            onOk?.invoke(date)
        }

        val dialog = DatePickerDialog(activity!!, listener, year, month, day)
        dialog.setOnCancelListener {
            onCancel?.invoke()
        }

        return dialog
    }
}