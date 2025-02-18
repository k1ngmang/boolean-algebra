package com.kingmang.algebra.handTest

import com.kingmang.algebra.KarnaughMap.TruthTable
import com.kingmang.algebra.MathFunction

fun main(args: Array<String>) {
    val sum = MathFunction.parse("((a xor b) xor c) xor t")
    println(sum)
    println(TruthTable(sum))
}

