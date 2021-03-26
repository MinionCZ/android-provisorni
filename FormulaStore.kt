package com.example.calc

import kotlin.math.floor

data class FormulaData(var formulaResult: String, var isFormulaCorrect: Boolean) {

}


var formula = ""
fun getMathFormula(): String {
    return formula
}

fun addCharToFormula(char: Char): FormulaData {
    val actionChars = HashSet<Char>()
    actionChars.add('C')
    actionChars.add('L')
    actionChars.add('=')
    actionChars.add('<')
    actionChars.add('>')
    val formulaData = FormulaData("", false)
    if (actionChars.contains(char)) {
        when (char) {
            'C' -> clear()
            'L' -> clearLastChar()
            '=' -> {
                if (isFormulaCorrect()) {
                    formulaData.formulaResult = calculateFormula()
                } else {
                    return formulaData
                }
            }
            else -> {
                formulaData.isFormulaCorrect = true
                return formulaData
            }
        }
    } else {
        formula += char
    }
    formulaData.isFormulaCorrect = true
    return formulaData
}

fun clear() {
    formula = ""
}

fun clearLastChar() {
    var length = formula.length
    length -= 1
    var helpFormula = ""
    for (i in 0 until length) {
        helpFormula += formula[i]
    }
    formula = helpFormula
}

fun isFormulaCorrect(): Boolean {
    val operators = getAllOperators(formula)
    val numbers = getAllNumbersFromFormula(formula)
    return operators.size + 1 == numbers.size
}

fun getAllNumbersFromFormula(formula: String): ArrayList<Double> {
    val completeNumbers = ArrayList<Double>()
    var stack = ""
    for (char in formula) {
        if (isNumber(char)) {
            stack += char
        } else {
            val number = stack.toDouble()
            completeNumbers.add(number)
            stack = ""
        }
    }
    if (stack.isNotEmpty()) {
        val number = stack.toDouble()
        completeNumbers.add(number)
    }
    return completeNumbers
}

fun getAllOperators(formula: String): ArrayList<Char> {
    val operators = ArrayList<Char>()
    for (char in formula) {
        if (!isNumber(char)) {
            operators.add(char)
        }
    }
    return operators
}

fun isNumber(char: Char): Boolean {
    val operators = HashSet<Char>()
    operators.add('*')
    operators.add('/')
    operators.add('+')
    operators.add('-')
    return !operators.contains(char)
}

fun calculateFormula(): String {
    var operators = getAllOperators(formula)
    var numbers = getAllNumbersFromFormula(formula)
    numbers = handleMultiplication(numbers, operators)
    var result = handleSum(numbers, operators)
    var roundedResult = floor(result)
    if (result == roundedResult) {
        return result.toLong().toString()
    }

    return result.toString()
}


fun handleMultiplication(
    numbers: ArrayList<Double>,
    operators: ArrayList<Char>
): ArrayList<Double> {
    var offset = 0
    for (i in 0 until operators.size) {
        if (operators[i] == '*' || operators[i] == '/') {
            if (operators[i] == '*') {
                var result = numbers[offset] * numbers[offset + 1]
                numbers[offset] = result
                numbers.removeAt(offset + 1)
            } else {
                var result = numbers[offset] / numbers[offset + 1]
                numbers[offset] = result
                numbers.removeAt(offset + 1)
            }
        } else {
            offset++
        }
    }
    return numbers
}

fun handleSum(numbers: ArrayList<Double>, operators: ArrayList<Char>): Double {
    var sum: Double = numbers[0]
    var numberPointer = 1
    for (operatorPointer in 0 until operators.size) {
        if (operators[operatorPointer] == '+') {
            sum += numbers[numberPointer]
            numberPointer++
        } else if (operators[operatorPointer] == '-') {
            sum -= numbers[numberPointer]
            numberPointer++
        }
    }
    return sum
}

