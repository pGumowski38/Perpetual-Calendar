package com.ubi.perpetual_calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.ubi.perpetual_calendar.databinding.ActivityNiedzieleBinding
import java.util.*

class Niedziele : AppCompatActivity() {

    private lateinit var bindingN: ActivityNiedzieleBinding
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingN = ActivityNiedzieleBinding.inflate(layoutInflater)
        val viewN = bindingN.root
        setContentView(viewN)

        val chosenYear = intent.getIntExtra("chosenYear", 2020)
        val wielkanocDay = intent.getIntExtra("wielkanocDay", 30)
        val wielkanocMonth = intent.getIntExtra("wielkanocMonth", 4)

        val yearText = "Niedziele handlowe dla roku $chosenYear"
        bindingN.textYear.text = yearText

        listView = bindingN.listNiedziele
        val listItems = arrayOfNulls<String>(7)

        var index = 0
        val calendar = Calendar.getInstance()
        for (i in "1468"){
            calendar.set(chosenYear-4, i.toInt()-1, 1)
            val maxi = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            calendar.set(Calendar.DAY_OF_MONTH, maxi)

            if (calendar.get(Calendar.DAY_OF_WEEK) != 1){
                calendar.add(Calendar.DAY_OF_YEAR, 1-calendar.get(Calendar.DAY_OF_WEEK))
            }
            listItems[index] = calendar.get(Calendar.DAY_OF_MONTH).toString() + "." + (calendar.get(Calendar.MONTH)+1).toString() + ".$chosenYear"
            if (i == '1') { index++ }
            index++
        }

        calendar.clear()
        calendar.set(chosenYear, wielkanocMonth-1, wielkanocDay)
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        listItems[1] = calendar.get(Calendar.DAY_OF_MONTH).toString() + "." + (calendar.get(Calendar.MONTH)+1).toString() + ".$chosenYear"

        calendar.clear()
        calendar.set(chosenYear, 11, 25)
        if (calendar.get(Calendar.DAY_OF_WEEK) != 1){
            calendar.add(Calendar.DAY_OF_YEAR, 8-calendar.get(Calendar.DAY_OF_WEEK))
        }
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        listItems[6] = calendar.get(Calendar.DAY_OF_MONTH).toString() + "." + (calendar.get(Calendar.MONTH)+1).toString() + ".$chosenYear"
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        listItems[5] = calendar.get(Calendar.DAY_OF_MONTH).toString() + "." + (calendar.get(Calendar.MONTH)+1).toString() + ".$chosenYear"

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter

        val okee = bindingN.buttonNOk
        okee.setOnClickListener {
            finish()
        }
    }

    private fun returnDate(day: Int, month: Int): String{
        var dayDate: String = ""
        if (day < 10) {
            dayDate += "0$day."
        }
        else {
            dayDate += "$day."
        }
        if (month < 10) {
            dayDate += "0$month"
        }
        else {
            dayDate += "$month"
        }
        return dayDate
    }
}