package com.krezinger.derivativecalc.calculations

import kotlin.collections.plusAssign
import kotlin.text.isDigit
import kotlin.text.isLetter

sealed class Token {
    data class Number(val value: Double) : Token()
    data class Variable(val name: Char) : Token()
    data class Operand(val symbol: Char) : Token()
    data class Bracket(val type: BracketType) : Token()

    enum class BracketType(symbol: Char){
        OPEN('('),
        CLOSE(')')
    }
}
class Parser {
    // needs optimization for: (sin, cos, tan, exp, log)!
    // lexer for return type List<Token> needed
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

    fun lexer(token: String): Token{
        // Integer(for now needs Double later
        val numberRegex = Regex("^-?\\d+$")
        // Variables
        val variableRegex = Regex("^[a-zA-Z]$")
        // Operators
        val operatorRegex = Regex("^[+\\-*/^]$")
        // Opening bracket
        val openParenRegex = Regex("^\\($")
        // Closing Bracket
        val closeParenRegex = Regex("^\\)$")

        return when {
            numberRegex.matches(token) -> Token.Number(token.toDouble())
            variableRegex.matches(token) -> Token.Variable(token[0])
            operatorRegex.matches(token) -> Token.Operand(token[0])
            openParenRegex.matches(token) -> Token.Bracket(Token.BracketType.OPEN)
            closeParenRegex.matches(token) -> Token.Bracket(Token.BracketType.CLOSE)
            else -> throw IllegalArgumentException("invalid token")
        }

    }

    fun isOperator(token: String): Boolean{
        return token in listOf("+","-","*","^", "/")
    }
    fun isNegative(tokens: List<String>, index: Int): Boolean{
        return index == 0 ||
                (tokens.isNotEmpty() && (tokens.last() == "(" || isOperator(tokens.last())))

    }
}
