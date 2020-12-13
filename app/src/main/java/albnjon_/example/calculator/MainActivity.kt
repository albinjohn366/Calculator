package albnjon_.example.calculator

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class Global: Application(){
    companion object{
        var previousDigit: String? = null
        val operators = arrayListOf<String>("+", "-", "*", "/", ".")
        var previousSolution: String? = null
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun ButtonClick(view: View){
        val digit: String = (view as Button).text as String
        if (digit in Global.operators && Global.previousSolution == (expression.text).toString()){
            if (digit == "."){
                return
            }
            expression.text = solution.text
        }else if (digit !in Global.operators && Global.previousSolution == (expression.text).toString()){
            expression.text = null
            solution.text = null
            Global.previousDigit = null
            Global.previousSolution = null
        }

        if (!(digit in Global.operators && (Global.previousDigit in Global.operators || Global.previousDigit == null))){
            expression.append(digit)
        }
        Global.previousDigit = digit
    }

    fun clearSpace(view: View){
        expression.text = null
        solution.text = null
        Global.previousDigit = null
        Global.previousSolution = null
    }

    fun solve(view: View){
        if (Global.previousDigit !in Global.operators && Global.previousDigit != null){
            val value = evaluate((expression.text).toString())
            solution.text = value
            Global.previousSolution = (expression.text).toString()
        }
    }

    fun evaluate(exp: String): String {
        var a = exp[0] + (exp.substring(1, exp.length)).replace("-", "n")
        while (true) {
            if ("/" in a) {
                var (num1, num2, ind1, ind2) = findingDigits(a, "/")
                var value = (num1 / num2).toString()
                value = format(value)
                if (value.length > 11)
                    value = value.toDouble().toString()
                a = a.substring(0, (ind1.toInt() + 1)) + value +
                        a.substring(ind2.toInt(), a.length)
            }else if ("*" in a){
                val (num1, num2, ind1, ind2) = findingDigits(a, "*")
                var value = (num1 * num2).toString()
                value = format(value)
                if (value.length > 11)
                    value = value.toDouble().toString()
                a = a.substring(0, (ind1.toInt() + 1)) + value +
                        a.substring(ind2.toInt(), a.length)
            }else if ("+" in a){
                val (num1, num2, ind1, ind2) = findingDigits(a, "+")
                var value = (num1 + num2).toString()
                value = format(value)
                if (value.length > 11)
                    value = value.toDouble().toString()
                a = a.substring(0, (ind1.toInt() + 1)) + value +
                        a.substring(ind2.toInt(), a.length)
            } else if ("n" in a.subSequence(1, a.length)){
                val (num1, num2, ind1, ind2) = findingDigits(a, "n")
                var value = (num1 - num2).toString()
                value = format(value)
                if (value.length > 11)
                    value = value.toDouble().toString()
                a = a.substring(0, (ind1.toInt() + 1)) + value +
                        a.substring(ind2.toInt(), a.length)
            }else{
                break
            }
        }
        return a
    }

    fun findingDigits(s: String, operator: String): ArrayList<Double> {
        val index = s.indexOf(operator)
        var num1: String = ""
        var num2: String = ""
        val result = arrayListOf<Double>()
        var ind1: Int? = null
        var ind2: Int? = null

        for (i in index + 1 until s.length) {
            if (s[i].toString() !in arrayListOf<String>("/", "*", "+", "n")) {
                if (i == s.length - 1) {
                    ind2 = i + 1
                }
                num2 += s[i].toString()
            } else {
                ind2 = i
                break
            }
        }
        for (i in index - 1 downTo 0) {
            if (s[i].toString() !in arrayListOf<String>("/", "*", "+", "n")) {
                if (i == 0) {
                    ind1 = i - 1
                }
                num1 = s[i].toString() + num1
            } else {
                ind1 = i
                break
            }
        }
        result.add(num1.toDouble())
        result.add(num2.toDouble())
        result.add(ind1!!.toDouble())
        result.add(ind2!!.toDouble())
        return result
    }

    fun format(value: String): String {
        if ("E" !in value){
            return value
        }else{
            val index = value.indexOf("E")
            var num = ""
            for (i in index + 1 until value.length){
                num += value[i]
            }
            var newValue: String = ""
            var index1: Int? = null
            if ("." in value){
                index1 = value.indexOf(".")
                newValue += value.substring(0, index1)
                newValue += value.substring(index1 + 1, index)
            }else{
                newValue += value.substring(0, index)
            }
            var iter = 0
            iter = if (index1 != null){
                num.toInt() - index1
            }else{
                num.toInt()
            }
            if (iter >= 0) {
                for (i in 0 until iter) {
                    newValue += "0"
                }
            }else{
                for (i in 0 until Math.abs(iter + newValue.length)){
                    newValue = "0$newValue"
                }
                newValue =  "0.$newValue"
            }
            return newValue

        }
    }

    fun erase(view: View) {
        val string = (expression.text).toString()
        expression.text = string.substring(0, string.length - 1)
    }

}