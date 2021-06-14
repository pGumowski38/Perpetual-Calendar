package com.ubi.perpetual_calendar

import android.content.Intent
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ubi.perpetual_calendar.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.floor


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val yearPicker: NumberPicker = binding.yearPicker
        yearPicker.minValue = 1900
        yearPicker.maxValue = 2200
        yearPicker.value = Calendar.getInstance().get(Calendar.YEAR);
        yearPicker.wrapSelectorWheel = false

        val startYear = yearPicker.value
        val c1: Calendar = findHolidays(startYear)
        val wD = c1.get(Calendar.DAY_OF_MONTH)
        val wM =  c1.get(Calendar.MONTH)

        var dateWielkanoc = returnDate(wD, wM + 1) + ".$startYear"
        binding.dateWielkanoc.text = dateWielkanoc
        var datePopielec = findPopielec(wD, wM, startYear) + ".$startYear"
        binding.datePopielec.text = datePopielec
        var dateBozeCialo = findBozeCialo(wD, wM, startYear) + ".$startYear"
        binding.dateBCialo.text = dateBozeCialo
        var dateAdwent = findAdwent(startYear) + ".$startYear"
        binding.dateAdwent.text = dateAdwent


        yearPicker.setOnValueChangedListener { picker, oldValue, newVal ->
            val selected = newVal
            val cal: Calendar = findHolidays(selected)

            val wielkanocDay = cal.get(Calendar.DAY_OF_MONTH)
            val wielkanocMonth =  cal.get(Calendar.MONTH)

            dateWielkanoc = returnDate(wielkanocDay, wielkanocMonth + 1) + ".$selected"
            binding.dateWielkanoc.text = dateWielkanoc
            datePopielec = findPopielec(wielkanocDay, wielkanocMonth, selected) + ".$selected"
            binding.datePopielec.text = datePopielec
            dateBozeCialo = findBozeCialo(wielkanocDay, wielkanocMonth, selected) + ".$selected"
            binding.dateBCialo.text = dateBozeCialo
            dateAdwent = findAdwent(selected) + ".$selected"
            binding.dateAdwent.text = dateAdwent
        }

        val sundays = binding.buttonNiedziele
        sundays.setOnClickListener {

            if (yearPicker.value < 2020) {
                Toast.makeText(this, "Wtedy wszystkie były handlowe. Kiedyś to było.", Toast.LENGTH_SHORT).show()
            }
            else {

                val intent = Intent(this, Niedziele::class.java)
                intent.putExtra("chosenYear", yearPicker.value)

                val d: Int = binding.dateWielkanoc.text.subSequence(0, 2).toString().toInt()
                val m: Int = binding.dateWielkanoc.text.subSequence(3, 5).toString().toInt()

                intent.putExtra("wielkanocDay", d)
                intent.putExtra("wielkanocMonth", m)
                startActivity(intent)
            }
        }

        val workingDays = binding.buttonRobocze
        workingDays.setOnClickListener {
            val intent = Intent(this, DniRobocze::class.java)
            startActivity(intent)
        }

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
        val m = floor((a + 11 * h + 22 * l).toDouble() / 451)
        val p = (h + l - 7*m + 114) % 31
        val day = (p + 1)
        val month = floor((h + l - 7 * m + 114)) / 31

        val calendar = Calendar.getInstance()
        calendar.set(year, month.toInt() - 1, day.toInt())

        return calendar
    }

    private fun findPopielec(day: Int, month: Int, year: Int) :String {
        val calendar: Calendar = Calendar.getInstance()

        calendar.set(year, month, day)
        calendar.add(Calendar.DAY_OF_YEAR, -46)
        return returnDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1)
    }

    private fun findBozeCialo(day: Int, month: Int, year: Int) :String {
        val calendar: Calendar = Calendar.getInstance()

        calendar.set(year, month, day)
        calendar.add(Calendar.DAY_OF_YEAR, 60)
        return returnDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1)
    }

    private fun findAdwent(year: Int): String{
        val calendar = Calendar.getInstance()
        calendar.set(year, 11, 25)
        calendar.add(Calendar.DAY_OF_YEAR, -28)

        if (calendar.get(Calendar.DAY_OF_WEEK) != 1){
            calendar.add(Calendar.DAY_OF_YEAR, 8 - calendar.get(Calendar.DAY_OF_WEEK))
        }

        return returnDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1)
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