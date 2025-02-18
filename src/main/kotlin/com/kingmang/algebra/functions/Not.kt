package com.kingmang.algebra.functions

import com.kingmang.algebra.utils.Config
import com.kingmang.algebra.MathFunction

class Not(@JvmField var f: MathFunction) : MathFunction() {
    override fun toString(): String {
        if (Config.notMode == Config.NotMode.BANG) {
            return "!" + f.top()
        } else if (Config.notMode == Config.NotMode.TILDE) {
            return "~" + f.top()
        } else if (Config.notMode == Config.NotMode.QUOTE) {
            return f.top() + "'"
        }
        return "not " + f.top()
    }

    override fun top(): String {
        if (f.isVar) {
            return toString()
        }
        return super.top()
    }

    override fun simplify(): MathFunction {
        if (f.isNot) {
            return f.asNot().f
        }
        return this
    }

    override fun not(): MathFunction {
        return f
    }

    override fun get(v: Array<Variable>, c: Array<Cons>): Cons {
        return f[v, c].not() as Cons
    }

    override fun equals(other: Any?): Boolean {
        if (other is Not) {
            return f == other.f
        }
        return false
    }
}
