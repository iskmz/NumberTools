package com.iskandar.numbertools

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setListeners()
    }

    private fun setListeners() {

        btnInfo.setOnClickListener { showInfoDialog() }
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
