package com.iskandar.numbertools

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private var input = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setListeners()
    }


    private fun setListeners() {

        btnInfo.setOnClickListener { showInfoDialog() }

        // base conversions //
        btnBinary.setOnClickListener {
            if(!inputOK()) return@setOnClickListener
            showResultDialog(input.toLong(),"Conversion To Binary", input.toLong().toString(2))
        }
        btnOctal.setOnClickListener {
            if(!inputOK()) return@setOnClickListener
            showResultDialog(input.toLong(),"Conversion To Octal",input.toLong().toString(8))
        }
        btnHexa.setOnClickListener {
            if(!inputOK()) return@setOnClickListener
            showResultDialog(input.toLong(),"Conversion To Hexadecimal",input.toLong().toString(16))
        }

        // manipulations / calculations //
        btnReverse.setOnClickListener {
            if(!inputOK()) return@setOnClickListener
            showResultDialog(input.toLong(),"Reverse Digits Order", getReversedDigits())
        }
        btnDivisors.setOnClickListener {
            if(!inputOK()) return@setOnClickListener
            showResultDialog(input.toLong(),"Find Proper Divisors", getDivisors())
        }
        btnSum.setOnClickListener {
            if(!inputOK()) return@setOnClickListener
            showResultDialog(input.toLong(),"Calculate Sum of Digits", calcDigitSum())
        }

        // convert to text //
        btnDigByDigReading.setOnClickListener {  }
        btnNaturalReading.setOnClickListener {  }
    }


/////////////////////////////////   general utility functions ////////////////////////////////////////////////////

    private fun showResultDialog(input: Long, oprName: String, result:String) {
        val res = AlertDialog.Builder(this@MainActivity)
            .setTitle("Result Window!")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setMessage("Input: $input \n\nOperation: $oprName \n\nResult: $result")
            .create()
        res.setCanceledOnTouchOutside(false)
        res.show()
    }

    private fun inputOK(): Boolean {
        input = txtInputNum.text.toString()
        if(input.isEmpty()) {
            Toast.makeText(this@MainActivity,"Input must NOT be empty!",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun showInfoDialog() {
        val info = AlertDialog.Builder(this@MainActivity)
            .setTitle("Number Tools")
            .setMessage("by Iskandar Mazzawi \u00a9")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss()  }
            .setNegativeButton("Exit App.") { _,_ -> finish() }
            .setIcon(R.drawable.ic_info_orange)
            .create()
        info.setCanceledOnTouchOutside(false)
        info.show()
    }


/////////////////////////////////   manipulations / calculations  functions  /////////////////////////////////////


    private fun getReversedDigits(): String {
        var tmp = input.toLong()
        var revTmp = 0L
        while(tmp>0){ revTmp= revTmp*10+tmp%10; tmp/=10 }
        return  revTmp.toString()
    }


    private fun getDivisors(): String {

        val tmp = input.toLong()

        if (tmp==0.toLong()) return "No Divisors"
        if(tmp==1.toLong()) return "1"
        if(tmp > (10.0.pow(9.0)).toLong()) return "\n<<OUT-OF-RANGE-LIMIT>>\nPlease enter a number less than One-Billion!"

        var divListStart="1"
        var divListEnd=""
        var checkLimit = sqrt(tmp.toDouble())

        for(i in 2..checkLimit.toLong())
        {
            if(tmp%i==0.toLong())
            {
                if(i==tmp/i) { divListStart += " , $i" }
                else { divListStart += " , $i"; divListEnd = " , ${tmp/i}$divListEnd" }
            }
        }
        return (divListStart+divListEnd)
    }


    private fun calcDigitSum(): String {

        var tmp = input.toLong()
        var sum = 0
        while (tmp>0) { sum += (tmp%10).toInt(); tmp/=10 }
        return sum.toString()
    }
}
