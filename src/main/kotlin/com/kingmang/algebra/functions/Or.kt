package com.kingmang.algebra.functions

import com.kingmang.algebra.utils.Config
import com.kingmang.algebra.MathFunction
import java.util.*

class Or : MathFunction {
    constructor(vararg f: MathFunction) {
        this.list = asList(f)
    }

    constructor(f: List<MathFunction>) {
        list.addAll(f)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (i in list.indices) {
            val term = list[i]
            if (term.isCons || term.isVar || term.isOr || term.isAnd) {
                sb.append(term)
            } else {
                sb.append(term.top())
            }
            if (i < list.size - 1) {
                sb.append(Config.orMode.str())
            }
        }
        return sb.toString()
    }

    override fun simplify(): MathFunction {
        val l: MutableList<MathFunction> = ArrayList()
        // a+(b+c)=a+b+c
        // a+1=1,a+0=a
        for (v in list) {
            if (v.isOr) {
                l.addAll(v.list)
            } else if (v.isCons) {
                if (v.isHigh) {
                    return Cons.HIGH
                }
            } else {
                l.add(v)
            }
        }
        list.clear()
        list.addAll(l)
        if (list.isEmpty()) {
            return Cons.LOW
        }
        if (list.size == 1) {
            return list[0]
        }
        return this
    }


    override fun not(): MathFunction {
        // return new nor(f);
        val a = list[0]
        val b = if (list.size == 2) list[1] else Or(wout(0))
        return a.not().and(b.not())
    }

    override fun get(v: Array<Variable>, c: Array<Cons>): Cons {
        for (term in list) {
            if (term[v, c].value) {
                return Cons.HIGH
            }
        }
        return Cons.LOW
    }

    override fun alternate(): MathFunction {
        val list: MutableList<MathFunction> = ArrayList()
        for (term in this.list) {
            list.add(term.alternate())
        }
        return Or(list)
    }

    companion object {
        @JvmStatic
        fun make(list: List<MathFunction>): MathFunction {
            if (list.size == 1) return list[0]
            return Or(list)
        }
    }
}
