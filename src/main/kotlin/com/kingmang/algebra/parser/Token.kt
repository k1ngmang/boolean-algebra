package com.kingmang.algebra.parser

class Token {
    var value: String = ""
    var kind: TokenKind

    constructor(value: String) {
        this.value = value
        this.kind = TokenKind.IDENT
    }

    constructor(kind: TokenKind) {
        this.kind = kind
    }

    override fun toString(): String {
        if (value != "") return value
        return kind.toString()
    }
}
