package com.example.calc


import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*

class MainActivity : AppCompatActivity() {
    private val buttonList = ArrayList<Button>()
    private val buttonCharacters = "<>CL" + "789+" + "456-" + "123*" + ".0=/"
    private var offset = 0
    private val displayDigits = 14
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonsRoot = findViewById<LinearLayout>(R.id.buttonlayout)
        for(i in 0..4) {
            val line = buttonCharacters.subSequence(i * 4, (i+1)*4)
            buttonsRoot.addView(generateLine(line.toString()))
            buttonsRoot.addView(generateSpace(30, false))
        }

    }
    fun generateButton(character: Char): Button {
        val button = Button(applicationContext)
        button.text = "" + character
        button.setBackgroundColor(Color.rgb(0, 0, 0))
        button.setTextColor(Color.rgb(0, 225, 225))
        button.setOnClickListener {
            val formulaData = addCharToFormula(character)
            Log.i("Debug", formulaData.toString())
            if(!formulaData.isFormulaCorrect){
                Toast.makeText(applicationContext, "Příklad je zadán nesprávně", Toast.LENGTH_LONG).show()
            }else{
                var resultDisplay = findViewById<TextView>(R.id.resultDisplay)
                resultDisplay.text = formulaData.formulaResult
            }
            var display = findViewById<TextView>(R.id.display)
            display.text = handleShift(getMathFormula(), character)
        }
        return button

    }
    fun generateLine(line: String):LinearLayout {
        var lineLayout = LinearLayout(applicationContext)
        lineLayout.gravity = Gravity.CENTER
        lineLayout.orientation = LinearLayout.HORIZONTAL
        var counter = 1
        for (character in line) {
            val generatedButton = generateButton(character)
            buttonList.add(generatedButton)
            lineLayout.addView(generatedButton)
            if(counter <= 3 ){
                lineLayout.addView(generateSpace(30))
            }
            counter++
        }
        return  lineLayout
    }
    fun generateSpace(size:Int, vertical:Boolean = true):Space{
        val space = Space(applicationContext)
        if (vertical){
            space.minimumWidth = size
        }else{
            space.minimumHeight = size
        }
        return  space
    }

    fun handleShift(formula:String, clickedChar : Char) : String{
        if(formula.length <= displayDigits) {
            return formula
        }
        if(clickedChar == '<' && offset + displayDigits < formula.length){
            offset++
        }else if(clickedChar == '>' && offset > 0){
            offset--
        }
        var newString = ""
        for (i in 0 until displayDigits){
            newString += formula[i + offset]
        }
        return newString
    }
}
