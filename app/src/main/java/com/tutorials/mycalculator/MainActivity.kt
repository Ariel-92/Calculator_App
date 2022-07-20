package com.tutorials.mycalculator

import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.ArithmeticException

enum class Operator {
    DEFAULT, PLUS, MINUS, MULTIPLY, DIVISION
}

class MainActivity : AppCompatActivity() {

    private var tvInput: TextView? = null
    var lastNumeric : Boolean = false
    var lastDot : Boolean = false
    var currentOperator : Operator = Operator.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.tvInput)
        tvInput?.text = "0"
    }

    fun onDigit(view: View){
        val input : String = (view as Button).text.toString()
        if(lastNumeric || lastDot) {
            tvInput?.append(input)
            lastNumeric = true
        }
        else {
            var tvValues : List<String> = subtractBetweenOperator(tvInput?.text.toString())
            if(tvValues.size <= 1) {
                if (currentOperator != Operator.DEFAULT && tvInput?.text.toString() != "0") {
                    tvInput?.append(input)
                    lastNumeric = true
                } else if (input != "0") {
                    tvInput?.text = input
                    lastNumeric = true
                }
            }
            if(tvValues.size == 2) {
                if (tvValues[1] == "0"){
                    if (input != "0") {
                        var tempText : String = tvInput?.text.toString()
                        tvInput?.text = tempText.substring(0, tempText.length - 1) + input
                        lastNumeric = true
                    }
                } else{
                    tvInput?.append(input)
                    if (input != "0")
                        lastNumeric = true
                }
            }
        }
    }

    fun onClear(view: View){
        tvInput?.text = "0"
        lastNumeric = false
        lastDot = false
    }

    fun onDecimalPoint(view: View){
        if((lastNumeric || tvInput?.text.toString().last() == '0') && !lastDot){
            tvInput?.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View){
        tvInput?.text?.let{
            if(lastNumeric && !isOperatorAdded(it.toString())){
                tvInput?.append((view as Button).text)
                lastNumeric = false
                lastDot = false
                changeOperator((view as Button).text.toString())
            }
        }
    }

    fun onEqual(view: View){
        if(lastNumeric){
            var tvValue = tvInput?.text.toString()
            var prefix = ""

            try{
                if(tvValue.startsWith("-")){
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }
                if(tvValue.contains("-"))
                    tvInput?.text = subtract(value = tvValue, prefix = prefix)
                else if(tvValue.contains("+"))
                    tvInput?.text = plus(value = tvValue, prefix = prefix)
                else if(tvValue.contains("*"))
                    tvInput?.text = multiply(value = tvValue, prefix = prefix)
                else if(tvValue.contains("/"))
                    tvInput?.text = divide(value = tvValue, prefix = prefix)
            }catch (e: ArithmeticException){
                e.printStackTrace()
            }
        }
    }

    private fun removeZeroAfterDot(result: String) : String{
        var value = result
        if(result.contains(".0")){
            value = result.substring(0, result.length - 2)
        }

        return value
    }

    private fun isOperatorAdded(value: String) : Boolean{
        return if(value.startsWith("-")){
            false
        }else{
            value.contains("/")
                    || value.contains("*")
                    || value.contains("+")
                    || value.contains("-")
        }
    }

    private fun plus(value: String, prefix: String) : String{
        val splitValue = value.split("+")

        var one = prefix + splitValue[0]
        var two = splitValue[1]
        var result = one.toDouble() + two.toDouble()

        return removeZeroAfterDot(result.toString())
    }

    private fun subtract(value: String, prefix: String) : String{
        val splitValue = value.split("-")

        var one = prefix + splitValue[0]
        var two = splitValue[1]
        var result = one.toDouble() - two.toDouble()

        return removeZeroAfterDot(result.toString())
    }

    private fun multiply(value: String, prefix: String) : String{
        val splitValue = value.split("*")

        var one = prefix + splitValue[0]
        var two = splitValue[1]
        var result = one.toDouble() * two.toDouble()

        return removeZeroAfterDot(result.toString())
    }

    private fun divide(value: String, prefix: String) : String{
        val splitValue = value.split("/")

        var one = prefix + splitValue[0]
        var two = splitValue[1]
        var result = one.toDouble() / two.toDouble()

        return removeZeroAfterDot(result.toString())
    }

    private fun changeOperator(value: String){
        when(value) {
            "+" -> currentOperator = Operator.PLUS
            "-" -> currentOperator = Operator.MINUS
            "*" -> currentOperator = Operator.MULTIPLY
            "/" -> currentOperator = Operator.DIVISION
        }
    }

    private fun subtractBetweenOperator(value: String) : List<String>{
        var result : List<String> = when(currentOperator) {
            Operator.PLUS -> value.split("+")
            Operator.MINUS -> value.split("-")
            Operator.MULTIPLY -> value.split("*")
            Operator.DIVISION -> value.split("/")
            else -> value.split(" ")
        }

        return result
    }
}