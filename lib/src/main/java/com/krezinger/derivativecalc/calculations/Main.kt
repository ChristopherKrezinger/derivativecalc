package com.krezinger.derivativecalc.calculations

fun main(){
    val test = Parser()
    println(test.tokenizer("-(2x)+x+-3*y"))
}