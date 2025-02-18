package com.kingmang.algebra.functions

import com.kingmang.algebra.utils.Config
import com.kingmang.algebra.MathFunction

class And : MathFunction {
    constructor(vararg list: MathFunction) {
        this.list = asList(list)
    }

    constructor(list: List<MathFunction>) {
        this.list.addAll(list)
    }

    companion object {
        @JvmStatic
        fun make(list: List<MathFunction>): MathFunction {
            if (list.size == 1) return list[0]
            return And(list)
        }
    }
    override fun toString(): String {
        val sb = StringBuilder()
        for (i in list.indices) {
            val term = list[i]
            if (term.isCons || term.isVar || term.isAnd) {
                sb.append(term)
            } else {
                sb.append(term.top())
            }

            if (i < list.size - 1) {
                sb.append(Config.andMode.str())
            }
        }
        return sb.toString()
    }

    override fun simplify(): MathFunction {
        val l: MutableList<MathFunction> = ArrayList()
        for (v in list) {
            if (v.isAnd) {
                l.addAll(v.list)
            } else if (v.isCons) {
                if (v.isLow) {
                    return Cons.LOW
                } else {
                    continue
                }
            } else {
                l.add(v)
            }
        }
        list.clear()
        list.addAll(l)
        if (list.isEmpty()) {
            return Cons.HIGH
        } else if (list.size == 1) {
            return list[0]
        }
        return this
    }

    override fun not(): MathFunction {
        return Nand(list)
    }

    override fun get(v: Array<Variable>, c: Array<Cons>): Cons {
        for (term in list) {
            if (!term[v, c].value) {
                return Cons.LOW
            }
        }
        return Cons.HIGH
    }

    override fun alternate(): MathFunction {
        val list: MutableList<MathFunction> = ArrayList()
        for (term in this.list) {
            list.add(term.alternate())
        }
        return And(list)
    }
}
