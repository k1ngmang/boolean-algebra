package com.kingmang.algebra.parser

import com.kingmang.algebra.*
import com.kingmang.algebra.functions.*

class Parser(var lexer: Lexer) {
    private var peeked: Token? = null

    fun match(kind: TokenKind): Boolean {
        if (peeked == null) {
            peeked = lexer.nextToken()
        }
        if (peeked != null) {
            return peeked!!.kind == kind
        }
        return false
    }

    fun consume(kind: TokenKind): Token? {
        val tok: Token?
        if (peeked != null) {
            tok = peeked
            peeked = null
        } else {
            tok = lexer.nextToken()
            if (tok.kind == TokenKind.EOF) {
                throw RuntimeException("eof")
            }
        }
        if (tok!!.kind != kind) {
            throw RuntimeException("expecting $kind got:$tok")
        }
        return tok
    }

    fun expr(): MathFunction {
        var a = term()
        while (true) {
            if (match(TokenKind.OR)) {
                consume(TokenKind.OR)
                val b = expr()
                a = Or(a, b)
            } else if (match(TokenKind.XOR)) {
                consume(TokenKind.XOR)
                val b = expr()
                a = Xor(a, b)
            } else if (match(TokenKind.NOR)) {
                consume(TokenKind.NOR)
                val b = expr()
                a = Nor(a, b)
            } else if (match(TokenKind.XNOR)) {
                consume(TokenKind.XNOR)
                val b = expr()
                a = Xnor(a, b)
            } else {
                break
            }
        }
        return a
    }

    fun term(): MathFunction {
        var a = unary()
        while (true) {
            if (match(TokenKind.AND)) {
                consume(TokenKind.AND)
                val b = term()
                a = And(a, b)
            } else if (match(TokenKind.NAND)) {
                consume(TokenKind.NAND)
                val b = term()
                a = Nand(a, b)
            } else {
                break
            }
        }
        return a
    }

    fun unary(): MathFunction {
        if (match(TokenKind.TILDE)) {
            consume(TokenKind.TILDE)
            val a = element()
            return Not(a)
        }
        val a = element()
        if (match(TokenKind.QUOTE)) {
            consume(TokenKind.QUOTE)
            return Not(a)
        }
        return a
    }

    fun element(): MathFunction {
        if (match(TokenKind.LPAREN)) {
            consume(TokenKind.LPAREN)
            val a = expr()
            consume(TokenKind.RPAREN)
            return a
        } else if (match(TokenKind.IDENT)) {
            val token = consume(TokenKind.IDENT)
            return Variable(token!!.value)
        } else {
            return cons()
        }
    }

    fun cons(): MathFunction {
        if (match(TokenKind.ZERO)) {
            consume(TokenKind.ZERO)
            return Cons.LOW
        } else {
            consume(TokenKind.OR)
            return Cons.HIGH
        }
    }
}
