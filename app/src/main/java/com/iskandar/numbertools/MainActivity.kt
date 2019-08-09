package com.iskandar.numbertools

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow
import kotlin.math.sqrt
import android.R.attr.label
import android.content.ClipData
import android.content.ClipData.newPlainText
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE



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
        btnBinary.setOnClickListener { doIt("Conversion To Binary") {getBinary()} }
        btnOctal.setOnClickListener { doIt("Conversion To Octal") {getOctal()} }
        btnHexa.setOnClickListener { doIt("Conversion To Hexadecimal"){getHexa()} }

        // manipulations / calculations //
        btnReverse.setOnClickListener { doIt("Reverse Digits Order"){getReversedDigits()} }
        btnDivisors.setOnClickListener { doIt("Find Proper Divisors"){getDivisors()} }
        btnSum.setOnClickListener { doIt("Calculate Sum of Digits"){calcDigitSum()} }

        // convert to text //
        btnDigByDigReading.setOnClickListener {  doIt("Digit By Digit Reading"){digByDigReading()} }
        btnNaturalReading.setOnClickListener {  doIt("Natural Reading"){naturalReading()} }
    }

/////////////////////////////////   general utility functions ////////////////////////////////////////////////////

    private fun doIt(oprName: String, result:()->String) { if (inputOK()) showResultDialog(input,oprName,result) }

    private fun showResultDialog(input: String, oprName: String, result:()->String) {

        val rrr = result.invoke() // running lambda just now, after input was given in inputOK() !! //
        val msg = "Input: $input \n\nOperation: $oprName \n\nResult: $rrr"

        val res = AlertDialog.Builder(this@MainActivity)
            .setTitle("Result Window!")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setNeutralButton("COPY ALL"){ _,_ -> copyToClipboard(msg) }
            .setNegativeButton("COPY RESULT") { _,_ -> copyToClipboard(rrr)}
            .setMessage(msg)
            .create()
        res.setCanceledOnTouchOutside(false)
        res.show()
    }

    private fun copyToClipboard(msg: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = newPlainText("",msg)
        clipboard.primaryClip = clip
        Toast.makeText(this@MainActivity,"Copied to clipboard!",Toast.LENGTH_LONG).show()
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
            .setNeutralButton("Exit App.") { _,_ -> finish() }
            .setIcon(R.drawable.ic_info_orange)
            .create()
        info.setCanceledOnTouchOutside(false)
        info.show()
    }

/////////////////////////////////   base conversion  functions  /////////////////////////////////////////////////

    private fun getBinary() = input.toLong().toString(2)
    private fun getOctal() = input.toLong().toString(8)
    private fun getHexa() = input.toLong().toString(16)

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
        val checkLimit = sqrt(tmp.toDouble())

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

/////////////////////////////////   convert to text functions  ///////////////////////////////////////////////////

    //////// [natural reading] functions section   ////////////////////////

    // NOTE: MAX input number limit
    // is anything less than 1 quintillion! [18 digits MAX] << because of Long.Max_Value !!!
    // ... and non-negative.. & not a fraction

    private fun naturalReading():String{
        val numArr:Array<Int> = getNumArr(input.toLong())
        val numTextArrBasic:Array<String> = genBasicNumTextArr(numArr)
        val totalNumText:String = genFullNumText(numTextArrBasic)
        return "\n${capitalizeFirstLetter(totalNumText)}"
    }

    private fun genFullNumText(basicArr:Array<String>):String {
        var resultTxt =""
        for(arrCounter in basicArr.size-1 downTo 0)
        {
            if(basicArr[arrCounter].isNotEmpty())
            {
                resultTxt+="${basicArr[arrCounter]} ${getNumPlacementTag(arrCounter)} "
            }
        }
        return resultTxt
    }

    private fun getNumPlacementTag(place:Int) = when(place) {
        0 -> ""; 1 -> "thousand "; 2 -> "million "; 3 -> "billion "
        4 -> "trillion "; 5 -> "quadrillion"; else -> ""
        /*
            // the following is not included since limit is 18 digits (~ close to LONG.MAX_VALUE) //
                case 6: tag="quintillion"; break;
                case 7: tag="sextillion"; break;
                case 8: tag="septillion"; break;
                case 9: tag="octillion"; break;
            */
    }

    private fun genBasicNumTextArr(numArr : Array<Int>):Array<String>{
        val numTxtArr:Array<String> = Array(numArr.size){""}

        if(numArr.size==1 && numArr[0]==0) // basic condition //
        {
            numTxtArr[0]="ZERO"; return numTxtArr  // no need for further processing
        }

        for(arrCounter in 0 until numArr.size)
        {
            var tmp = numArr[arrCounter]
            numTxtArr[arrCounter]=""
            if(tmp>99)
            {
                val tmpDigit = tmp/100 // "hundreds" digit conversion to text //
                numTxtArr[arrCounter]+=getDigitName(tmpDigit)+" "+"hundred "
                tmp-=tmpDigit*100 // omitting whatever value is in hundreds digit to deal with smaller number
            }
            if(tmp>19)
            {
                val tmpDigit=tmp/10     // "tens" digit conversion to text //
                numTxtArr[arrCounter]+=getTensDigitName(tmpDigit)+" "
                tmp-=tmpDigit*10 // omitting whatever value is in the TENS digit to deal with smaller number
            }
            if(tmp>9)
            {
                // "teen-s" conversion to text //  i.e. tensDigit == 1 , in this case  //
                val tmpDigit=tmp%10 // tens-digit==1 , teen-name is defined by right-most digit //
                numTxtArr[arrCounter]+=getTeenNumName(tmpDigit)
            }
            else if(tmp>0)
            {
                numTxtArr[arrCounter]+=getDigitName(tmp)
            }
        }
        return numTxtArr
    }

    private fun getTeenNumName(digit:Int) = when(digit) {
        0->"ten"; 1->"eleven"; 2->"twelve"; 3->"thirteen"
        4->"fourteen"; 5->"fifteen"; 6->"sixteen"; 7->"seventeen"
        8->"eighteen"; 9->"nineteen"; else -> "" }

    private fun getTensDigitName(digit:Int):String = when(digit){
        0->""// dealt with in another method
        1->"" // dealt with in another method
        2->"twenty"; 3->"thirty"; 4->"forty"; 5->"fifty"; 6->"sixty"
        7->"seventy"; 8->"eighty"; 9->"ninety"; else ->"" }

    private fun getNumArr(num:Long):Array<Int> {
        val numArray:Array<Int> = Array<Int>(getNumArrLength(num)){0}
        splitNumToArr(numArray)
        return numArray
    }

    private fun splitNumToArr(numArr:Array<Int>){
        var tmp = input.toLong()
        for(arrCounter in 0 until numArr.size)
        {
            var digCounter = 0
            var locator = 1
            while(tmp>0 && digCounter<3)
            {
                numArr[arrCounter]+=locator*((tmp%10).toInt())
                tmp/=10
                digCounter+=1
                locator*=10
            }
        }
    }

    private fun getNumArrLength(n:Long):Int{
        val len = n.toString().length
        return if(len==0) 0 else ((len-1)/3)+1
    }

    //////////////////////////////////////////////////////////////////////
    //////// [digit by digit reading] functions section //////////////////

    private fun digByDigReading():String{
        var tmpReversed:Long = reverseNum(input.toLong())
        var res = ""
        while(tmpReversed>0)
        {
            res+="${getDigitName((tmpReversed%10).toInt())} "
            tmpReversed/=10
        }
        // special case: right-most digit is zero //
        if(input.toLong()%10==0.toLong()) res+="zero"
        return capitalizeFirstLetter(res)
    }

    private fun capitalizeFirstLetter(str:String) = when {
        str.isEmpty()->"" // if no letters in str
        str.length==1->str.toUpperCase() // if one letter only
        else->(str[0]).toString().toUpperCase()+str.substring(1)
    }

    private fun getDigitName(digit:Int) = when(digit){ 0->"zero"; 1->"one"
        2->"two"; 3->"three"; 4->"four"; 5->"five"; 6->"six"; 7->"seven"
        8->"eight"; 9->"nine"; else->"" }

    private fun reverseNum(num:Long):Long{
        var rev:Long=0
        var n = num
        while(n>0) { rev=rev*10+n%10; n/=10 }
        return rev
    }
}