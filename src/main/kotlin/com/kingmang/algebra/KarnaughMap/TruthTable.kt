package com.kingmang.algebra.KarnaughMap

import com.kingmang.algebra.MathFunction
import com.kingmang.algebra.functions.Cons
import com.kingmang.algebra.functions.Variable
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

class TruthTable(vararg f: MathFunction?) {
    @JvmField
    var vars: MutableList<Variable> = ArrayList()
    var `in`: MutableList<List<Cons>> =
        ArrayList()
    var out: MutableList<List<Cons>> =
        ArrayList()

    var f: Array<MathFunction> = f as Array<MathFunction>
    var calculated: Boolean = false

    fun calc() {
        val set: Set<Variable> = HashSet()
        for (term in f) {
            term.vars(set)
        }
        vars.addAll(set)
        sortVars(vars)

        val rows = 2.0.pow(vars.size.toDouble()).toInt()
        for (i in 0..<rows) {
            val lc: MutableList<Cons> = ArrayList()
            for (c in fix(Integer.toBinaryString(i), vars.size).toCharArray()) {
                lc.add(Cons(c))
            }
            `in`.add(lc)
            val ls: MutableList<Cons> = ArrayList()
            for (func in f) {
                ls.add(func[vars.toTypedArray<Variable>(), lc.toTypedArray<Cons>()])
            }
            out.add(ls)
        }
        calculated = true
    }

    override fun toString(): String {
        if (!calculated) {
            calc()
        }
        val sb = StringBuilder("\n")
        for (i in f.indices) {
            sb.append("F").append(i + 1).append("=").append(f[i]).append("\n")
        }
        sb.append(join(vars)).append(" | ")
        for (i in f.indices) {
            sb.append("F").append(i + 1)
            if (i < f.size - 1) {
                sb.append(" ")
            }
        }
        sb.append("\n")
        for (i in 0..<2 * (vars.size + f.size) + 2) {
            sb.append("-")
        }
        sb.append("\n")
        for (i in `in`.indices) {
            sb.append(join(`in`[i]))

            sb.append(" | ").append(join(out[i]))
            if (i < `in`.size - 1) {
                sb.append("\n")
            }
        }
        return sb.toString()
    }


    fun join(list: List<*>): String {
        val sb = StringBuilder()
        for (i in list.indices) {
            sb.append(list[i])
            if (i < list.size - 1) {
                sb.append(" ")
            }
        }
        return sb.toString()
    }

    val outStr: String
        get() {
            val sb = StringBuilder()
            for (cons in out) {
                sb.append(cons[0])
            }
            return sb.toString()
        }

    companion object {
        fun sortVars(list: List<Variable>) {
            list.sortedBy { it.name }
        }

        fun fix(str: String, total: Int): String {
            val remaining = abs((total - str.length).toDouble()).toInt()
            val sb = StringBuilder(str)
            for (i in 0..<remaining) {
                sb.insert(0, "0")
            }
            return sb.toString()
        }
    }
}
