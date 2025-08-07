package com.krezinger.derivativecalc.calculations

class Parser {
    // needs optimization for: (sin, cos, tan, exp, log)!
    fun Tokenizer(input : String) : List<String>{
        val tokens = mutableListOf<String>()
        var index = 0

        while(index < input.length){
            val current = input[index]

            //1. case, digits
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
            else{
                tokens += current.toString()
                index++

            }

        }
        return tokens
    }

}
