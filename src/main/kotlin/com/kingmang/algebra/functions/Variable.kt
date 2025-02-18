package com.kingmang.algebra.functions

import com.kingmang.algebra.MathFunction

class Variable(var name: String) : MathFunction(), Comparable<Variable> {
    override fun toString(): String {
        return name
    }

    override fun get(v: Array<Variable>, c: Array<Cons>): Cons? {
        for (i in v.indices) {
            if (v[i].name == name) {
                return c[i]
            }
        }
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (other is Variable) {
            return name == other.name
        }
        return false
    }

    override fun hashCode(): Int {
        var hash = 7
        hash = 31 * hash + name.hashCode()
        return hash
    }

    override fun compareTo(other: Variable): Int {
        return name.compareTo(other.name)
    }
}
