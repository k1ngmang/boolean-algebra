package com.kingmang.algebra.parser

class Lexer(var input: String) {
    var pos: Int = 0


    fun nextToken(): Token {
        while (pos < input.length) {
            val c = input[pos]
            if (c == ' ' || c == '\r' || c == '\n' || c == '\t') {
                pos++
            } else {
                break
            }
        }
        if (pos >= input.length) return Token(TokenKind.EOF)
        val c = input[pos]
        val start = pos++
        if (Character.isLetter(c)) {
            while (pos < input.length && (Character.isLetter(input[pos]) || input[pos] == '_')) {
                pos++
            }
            val `val` = input.substring(start, pos)
            when (`val`) {
                "and" -> return Token(TokenKind.AND)
                "or" -> return Token(TokenKind.OR)
                "xor" -> return Token(TokenKind.XOR)
                "nor" -> return Token(TokenKind.NOR)
                "xnor" -> return Token(TokenKind.XNOR)
                "nand" -> return Token(TokenKind.NAND)
                "not" -> return Token(TokenKind.NOT)
            }
            return Token(`val`)
        }
        return when (c) {
            '0' -> Token(TokenKind.ZERO)
            '1' -> Token(TokenKind.ONE)
            '(' -> Token(TokenKind.LPAREN)
            ')' -> Token(TokenKind.RPAREN)
            '+', '|' -> Token(TokenKind.OR)
            '.', '&', '*' -> Token(TokenKind.AND)
            '^' -> Token(TokenKind.XOR)
            '\'' -> Token(TokenKind.QUOTE)
            '~' -> Token(TokenKind.TILDE)
            else -> throw RuntimeException("invalid char '$c' at pos $start")
        }
    }
}
