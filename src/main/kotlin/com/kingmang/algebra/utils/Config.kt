package com.kingmang.algebra.utils

object Config {
    @JvmField
    var notMode: NotMode = NotMode.QUOTE
    var andMode: AndMode = AndMode.DOT
    @JvmField
    var orMode: OrMode = OrMode.PLUS
    @JvmField
    var xorMode: XorMode = XorMode.POW

    enum class NotMode {
        QUOTE, BANG, TILDE, STR
    }

    enum class AndMode {
        DOT, STAR, AMP, STR;

        fun str(): String {
            if (this == DOT) {
                return "."
            } else if (this == STAR) {
                return "*"
            } else if (this == AMP) {
                return "&"
            }
            return " And "
        }
    }

    enum class OrMode {
        PLUS, BAR, STR;

        fun str(): String {
            if (this == PLUS) {
                return "+"
            } else if (this == BAR) {
                return "|"
            }
            return " or "
        }
    }

    enum class XorMode {
        POW, STR;

        fun str(): String {
            if (this == POW) {
                return "^"
            }
            return " xor "
        }
    }
}
