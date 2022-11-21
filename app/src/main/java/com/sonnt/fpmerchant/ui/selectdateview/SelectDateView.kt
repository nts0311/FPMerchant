package com.sonnt.fpmerchant.ui.selectdateview

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.utils.standardFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SelectDateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    var newDateSelected: (LocalDate, LocalDate) -> Unit = {_, _ -> }

    lateinit var textFromDate: TextView
    lateinit var textToDate: TextView

    private var fromDate: LocalDate
    private var toDate: LocalDate

    init {
        init(attrs)

        fromDate = LocalDate.now()
        textFromDate.text = fromDate.standardFormat()

        toDate = LocalDate.now()
        textToDate.text = toDate.standardFormat()
    }

    @JvmName("setFromDate1")
    fun setFromDate(ld: LocalDate) {
        fromDate = ld
        textFromDate.text = ld.standardFormat()
        onNewDateSelected()
    }

    @JvmName("setToDate1")
    fun setToDate(ld: LocalDate) {
        toDate = ld
        textToDate.text = ld.standardFormat()
        onNewDateSelected()
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.view_select_date, this)

        textFromDate = findViewById(R.id.txtFromDate)
        textToDate = findViewById(R.id.txtToDate)

        setupText(textFromDate)
        setupText(textToDate)
    }

    private fun setupText(textView: TextView) {
        val dateSetListener: (DatePicker, Int, Int, Int) -> Unit =
            { _, year, monthOfYear, dayOfMonth ->
                val ld = LocalDate.of(year, monthOfYear + 1, dayOfMonth)

                if (textView === textFromDate) {
                    setFromDate(ld)
                } else {
                    if (ld >= fromDate){
                        setToDate(ld)
                    }
                }
            }

        textView.setOnClickListener {
            val ld = LocalDate.now()
            DatePickerDialog(
                context, dateSetListener,
                ld.year, ld.monthValue - 1, ld.dayOfMonth
            ).show()
        }

    }

    private fun onNewDateSelected() {
        newDateSelected(fromDate, toDate)
    }

}