package com.iskandar.numbertools

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

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
        btnReverse.setOnClickListener {  }
        btnDivisors.setOnClickListener {  }
        btnSum.setOnClickListener {  }


        // convert to text //
        btnDigByDigReading.setOnClickListener {  }
        btnNaturalReading.setOnClickListener {  }
    }

    private fun showResultDialog(input: Long, oprName: String, result:String)
    {

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

}
