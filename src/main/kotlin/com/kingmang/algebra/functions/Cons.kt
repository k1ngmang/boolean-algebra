package com.kingmang.algebra.functions

import com.kingmang.algebra.MathFunction

class Cons : MathFunction {
    @JvmField
    val value: Boolean

    constructor(value: Boolean) {
        this.value = value
    }

    constructor(value: Int) {
        this.value = (value == 1)
    }

    constructor(chr: Char) {
        this.value = chr == '1'
    }

    override fun toString(): String {
        return if (value) "1" else "0"
    }

    override fun not(): MathFunction {
        return Cons(!value)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Cons) {
            return value == other.value
        }
        return false
    }

    override fun get(v: Array<Variable>, c: Array<Cons>): Cons {
        return this
    }

    companion object {
        @JvmField
        val LOW: Cons = Cons(false)
        @JvmField
        val HIGH: Cons = Cons(true)
        val ZERO: Cons = LOW
        val ONE: Cons = HIGH
    }
}
