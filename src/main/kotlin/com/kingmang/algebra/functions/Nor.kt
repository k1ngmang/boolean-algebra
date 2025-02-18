package com.kingmang.algebra.functions

import com.kingmang.algebra.MathFunction
import java.util.*

class Nor : MathFunction {
    constructor(vararg arg: MathFunction) {
        this.list = asList(arg)
    }

    constructor(list: List<MathFunction>) {
        this.list = ArrayList(list)
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
                sb.append(" nor ")
            }
        }
        return sb.toString()
    }

    override fun not(): MathFunction {
        return Or(list)
    }

    override fun get(v: Array<Variable>, c: Array<Cons>): Cons {
        return alternate()[v, c]
    }


    override fun alternate(): MathFunction {
        val list: MutableList<MathFunction> = ArrayList()
        for (p in this.list) {
            list.add(p.not().alternate())
        }
        return And(list)
    }
}
