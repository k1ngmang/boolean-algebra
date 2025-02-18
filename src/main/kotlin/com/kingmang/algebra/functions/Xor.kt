package com.kingmang.algebra.functions

import com.kingmang.algebra.utils.Config
import com.kingmang.algebra.MathFunction

class Xor : MathFunction {
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
                sb.append(Config.xorMode.str())
            }
        }
        return sb.toString()
    }

    override fun alternate(): MathFunction {
        // a' And b or a And b'
        var a = list[0]
        var b = if (list.size == 2) list[1] else Xor(wout(0))

        a = a.alternate()
        b = b.alternate()

        val left = a.and(b.not())
        val right = a.not().and(b)
        return left.or(right)
    }

    override fun not(): MathFunction {
        return Xnor(list)
    }

    override fun get(v: Array<Variable>, c: Array<Cons>): Cons {
        return alternate()[v, c]
    }
}
