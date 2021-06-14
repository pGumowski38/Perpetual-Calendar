package com.ubi.perpetual_calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import com.ubi.perpetual_calendar.databinding.ActivityDniRoboczeBinding
import java.util.*
import kotlin.math.floor

class DniRobocze : AppCompatActivity() {

    private lateinit var bindingDR: ActivityDniRoboczeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDR = ActivityDniRoboczeBinding.inflate(layoutInflater)
        val viewDR = bindingDR.root
        setContentView(viewDR)

        val date1: DatePicker = bindingDR.datePicker1
        val date2: DatePicker = bindingDR.datePicker2
        val cal: Calendar = Calendar.getInstance()
        cal.set(1900, 1, 1)
        date1.minDate = cal.timeInMillis
        date2.minDate = cal.timeInMillis
        cal.set(2200, 12, 31)
        date1.maxDate = cal.timeInMillis
        date2.maxDate = cal.timeInMillis

        date1.setOnDateChangedListener { picker, year, month, day ->
            countDays(year, month, day, date2.year, date2.month, date2.dayOfMonth)
        }

        date2.setOnDateChangedListener { picker, year, month, day ->
            countDays(date1.year, date1.month, date1.dayOfMonth, year, month, day)
        }

        val okee = bindingDR.buttonDROk
        okee.setOnClickListener {
            finish()
        }

    }

    private fun countDays(year1: Int, month1: Int, day1: Int, year2: Int, month2: Int, day2: Int){
        val calendar1: Calendar = Calendar.getInstance()
        calendar1.set(year1, month1, day1)

        val calendar2: Calendar = Calendar.getInstance()
        calendar2.set(year2, month2, day2)

        if (calendar1.after(calendar2)){
            return
        }

        var daysBetween: Int = 0
        var daysWorking: Int = 0
        calendar1.add(Calendar.DAY_OF_YEAR, 1)

        while (calendar1.before(calendar2)){

            System.out.println(calendar1.get(Calendar.DAY_OF_WEEK))
            if (calendar1.get(Calendar.DAY_OF_WEEK) != 1 && calendar1.get(Calendar.DAY_OF_WEEK) != 7){
                daysWorking++

                if (calendar1.get(Calendar.DAY_OF_MONTH) == 1 && calendar1.get(Calendar.MONTH) == 0){ daysWorking-- }
                else if (calendar1.get(Calendar.DAY_OF_MONTH) == 6 && calendar1.get(Calendar.MONTH) == 0){ daysWorking-- }
                else if (calendar1.get(Calendar.DAY_OF_MONTH) == 1 && calendar1.get(Calendar.MONTH) == 4){ daysWorking-- }
                else if (calendar1.get(Calendar.DAY_OF_MONTH) == 3 && calendar1.get(Calendar.MONTH) == 4){ daysWorking-- }
                else if (calendar1.get(Calendar.DAY_OF_MONTH) == 15 && calendar1.get(Calendar.MONTH) == 7){ daysWorking-- }
                else if (calendar1.get(Calendar.DAY_OF_MONTH) == 1 && calendar1.get(Calendar.MONTH) == 10){ daysWorking-- }
                else if (calendar1.get(Calendar.DAY_OF_MONTH) == 11 && calendar1.get(Calendar.MONTH) == 10){ daysWorking-- }
                else if (calendar1.get(Calendar.DAY_OF_MONTH) == 25 && calendar1.get(Calendar.MONTH) == 11){ daysWorking-- }
                else if (calendar1.get(Calendar.DAY_OF_MONTH) == 26 && calendar1.get(Calendar.MONTH) == 11){ daysWorking-- }
            }
            daysBetween++
            calendar1.add(Calendar.DAY_OF_YEAR, 1)
        }

        calendar1.clear()
        calendar1.set(year1, month1, day1)

        for (i in 0..(year2-year1)){
            val calWielka: Calendar = findHolidays(year1+i)

            if (calWielka.after(calendar1) && calWielka.before(calendar2)){
                daysWorking--
            }

            calWielka.add(Calendar.DAY_OF_YEAR, 59)
            if (calWielka.after(calendar1) && calWielka.before(calendar2)){
                daysWorking--
            }

            calWielka.clear()
        }

        val kalendarzoweText = "Dni Kalendarzowe: $daysBetween"
        bindingDR.textKalendarzowe.text = kalendarzoweText
        val roboczeText = "Dni Robocze: $daysWorking"
        bindingDR.textRobocze.text = roboczeText
    }

    private fun findHolidays(year: Int) :Calendar {
        val a = year % 19
        val b = floor(year.toDouble() / 100)
        val c = year % 100
        val d = floor(b.toDouble() / 4)
        val e = b % 4
        val f = floor((b + 8).toDouble() / 25)
        val g = floor((b - f + 1).toDouble() / 3)
        val h = (19*a + b - d - g + 15) % 30
        val i = floor(c.toDouble() / 4)
        val k = c % 4
        val l = (32 + 2*e + 2*i - h - k) % 7
        val m = floor((a + 11*h + 22*l).toDouble() / 451)
        val p = (h + l - 7*m + 114) % 31
        val day = (p + 1)
        val month = floor((h + l - 7*m + 114)) / 31

        val calendar = Calendar.getInstance()
        calendar.set(year, month.toInt()-1, day.toInt()+1)

        return calendar
    }
}