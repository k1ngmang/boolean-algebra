package com.kingmang.algebra.functions

import com.kingmang.algebra.MathFunction
import java.util.*

class Xnor : MathFunction {
    constructor(vararg array: MathFunction) {
        list = asList(array)
    }

    constructor(list: List<MathFunction?>) {
        this.list.addAll(list)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (i in list.indices) {
            val term = list[i]
            if (term.isCons || term.isVar) {
                sb.append(term)
            } else {
                sb.append(term.top())
            }
            if (i < list.size - 1) {
                sb.append(" xnor ")
            }
        }
        return sb.toString()
    }

    override fun not(): MathFunction {
        return Xor(list)
    }

    override fun get(v: Array<Variable>, c: Array<Cons>): Cons {
        return alternate()[v, c]
    }


    override fun alternate(): MathFunction {
        val list: MutableList<MathFunction> = ArrayList()
        for (term in this.list) {
            list.add(term.alternate())
        }
        return Xor(list).alternate().not().alternate()
    }
}
