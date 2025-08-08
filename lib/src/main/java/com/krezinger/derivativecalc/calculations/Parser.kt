package com.krezinger.derivativecalc.calculations

import kotlin.collections.plusAssign
import kotlin.text.isDigit
import kotlin.text.isLetter

sealed class Token {
    data class Number(val value: Double) : Token()
    data class Variable(val name: Char) : Token()
    data class Operand(val symbol: Char) : Token()
    data class Bracket(val type: BracketType) : Token()

    enum class BracketType { OPEN, CLOSE }
}
class Parser {
    // needs optimization for: (sin, cos, tan, exp, log)!
    fun tokenizer(input : String) : List<String>{
        val tokens = mutableListOf<String>()
        var index = 0

        while(index < input.length){
            val current = input[index]

            //1. case, positive number
            if(current.isDigit()){
                var number = ""

                while (index < input.length && input[index].isDigit()){
                    number += input[index]
                    index++
                }
                tokens += number

                //for e.g. 2x input
                if(index < input.length && input[index].isLetter()){
                    tokens += listOf("*", input[index].toString())
                    index++
                }
            }

            //2.case, negative number
            if(current == '-' && isNegative(tokens = tokens, index = index )){
                index++
                var number = "-"

                while (index < input.length && input[index].isDigit()){
                    number += input[index]
                    index++
                }
                tokens += number

                //for e.g. 2x input
                if(index < input.length && input[index].isLetter()){
                    tokens += listOf("*", input[index].toString())
                    index++
                }
            }

            else{
                tokens += current.toString()
                index++

            }

        }
        return tokens
    }

    fun isOperator(token: String): Boolean{
        return token in listOf("+","-","*","^", "/")
    }
    fun isNegative(tokens: List<String>, index: Int): Boolean{
        return index == 0 ||
                (tokens.isNotEmpty() && (tokens.last() == "(" || isOperator(tokens.last())))

    }
}
