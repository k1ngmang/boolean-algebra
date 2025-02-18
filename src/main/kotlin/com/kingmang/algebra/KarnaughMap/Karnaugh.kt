package com.kingmang.algebra.KarnaughMap

import com.kingmang.algebra.MathFunction
import com.kingmang.algebra.functions.And
import com.kingmang.algebra.functions.Or
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

class Karnaugh(f: MathFunction?) {
    @JvmField
    var tt: TruthTable = TruthTable(f)
    var rowVarSize: Int = 0
    var colVarSize: Int = 0
    lateinit var r: Array<Gray?>
    lateinit var c: Array<Gray?>
    var cellCountx: Int = 0
    var cellCounty: Int = 0
    lateinit var map: Array<IntArray>
    var groups: MutableList<Group> = ArrayList()

    init {
        tt.calc()
        init()
    }

    fun init() {
        val len = tt.vars.size

        rowVarSize = len / 2
        colVarSize = len - rowVarSize

        cellCountx = 2.0.pow(rowVarSize.toDouble()).toInt()
        cellCounty = 2.0.pow(colVarSize.toDouble()).toInt()

        r = arrayOfNulls(cellCountx)
        c = arrayOfNulls(cellCounty)
        Gray.set(r, rowVarSize)
        Gray.set(c, colVarSize)

        map = Array(cellCountx) { IntArray(cellCounty) }
        for (i in 0..<cellCountx) {
            for (j in 0..<cellCounty) {
                val gray = Gray(len)
                System.arraycopy(r[i]!!.arr, 0, gray.arr, 0, rowVarSize)
                System.arraycopy(c[j]!!.arr, 0, gray.arr, rowVarSize, colVarSize)
                map[i][j] = if (tt.out[gray.toInt()][0].value) 1 else 0
            }
        }
    }

    class Gray(len: Int) {
        var arr: ByteArray = ByteArray(len)

        fun init(i: Int) {
            val s = TruthTable.fix(Integer.toBinaryString(i xor (i shr 1)), arr.size)
            for (j in arr.indices) {
                arr[j] = (s[j].code - '0'.code).toByte()
            }
        }

        fun toInt(): Int {
            return toString().toInt(2)
        }

        override fun toString(): String {
            val sb = StringBuilder()
            for (b in arr) {
                sb.append(b.toInt())
            }
            return sb.toString()
        }

        companion object {
            fun set(b: Array<Gray?>, len: Int) {
                val x = b.size
                for (i in 0..<x) {
                    b[i] = Gray(len)
                    b[i]!!.init(i)
                }
            }
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (i in tt.vars.indices) {
            if (i == rowVarSize) {
                sb.append(",")
            }
            sb.append(tt.vars[i])
        }
        sb.append("\n")
        sb.append("    ")
        for (i in c.indices) {
            for (j in c[i]!!.arr.indices) {
                sb.append(c[i]!!.arr[j].toInt())
            }
            sb.append(" | ")
        }
        sb.append("\n")
        for (i in r.indices) {
            for (j in r[i]!!.arr.indices) {
                sb.append(r[i]!!.arr[j].toInt())
            }
            sb.append("   ")
            for (j in c.indices) {
                sb.append(map[i][j])
                sb.append("   ")
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    fun solve(): MathFunction {
        groups.clear()
        for (i in 0..<cellCountx) {
            for (j in 0..<cellCounty) {
                if (map[i][j] == 0) continue
                var w = 1
                var h = 1
                while (j + w < cellCounty && map[i][j + w] == 1) {
                    w++
                }
                while (!isPowerOf2(w * h)) {
                    w--
                }
                val g1 = Group(i, j, w, h)
                w = 1
                while (i + h < cellCountx && map[i + h][j] == 1) {
                    h++
                }
                while (!isPowerOf2(w * h)) {
                    h--
                }
                if (g1.w == 1 && h > 1) {
                    groups.add(Group(i, j, w, h))
                } else {
                    groups.add(g1)
                }
            }
        }
        eliminate()
        expand()
        val res: MutableList<MathFunction> = ArrayList()
        for (g in groups) {
            val terms: MutableList<MathFunction> = ArrayList()
            grayDown(terms, g)
            grayRight(terms, g)
            res.add(And(terms))
        }
        return Or(res)
    }

    fun grayDown(terms: MutableList<MathFunction>, g: Group) {
        val last = r[g.x]
        val changed = BooleanArray(last!!.arr.size)
        for (h in g.x + 1..<g.x + g.h) {
            val gray = r[h]
            for (a in last.arr.indices) {
                if (last.arr[a] != gray!!.arr[a]) {
                    changed[a] = true
                }
            }
        }
        for (a in changed.indices) {
            if (changed[a]) continue
            if (last.arr[a].toInt() == 1) {
                terms.add(tt.vars[a])
            } else {
                terms.add(tt.vars[a].not())
            }
        }
    }

    fun grayRight(terms: MutableList<MathFunction>, g: Group) {
        val last = c[g.y]
        val changed = BooleanArray(last!!.arr.size)
        for (h in g.y + 1..<g.y + g.w) {
            val gray = c[h]
            for (a in last.arr.indices) {
                if (last.arr[a] != gray!!.arr[a]) {
                    changed[a] = true
                }
            }
        }
        for (a in changed.indices) {
            if (changed[a]) continue
            if (last.arr[a].toInt() == 1) {
                terms.add(tt.vars[a + rowVarSize])
            } else {
                terms.add(tt.vars[a + rowVarSize].not())
            }
        }
    }

    fun expand() {
        for (g in groups) {
            if (g.w == 1) {
                var w = 2
                while (w < cellCounty) {
                    if (g.y + w > cellCounty) break
                    if (hasColZero(g.x, g.y + w - 1, w)) {
                        break
                    }
                    w++
                }
                g.w = w - 1
            } else if (g.h == 1) {
                var h = 2
                while (h < cellCountx) {
                    if (g.x + h > cellCountx) break
                    if (hasRowZero(g.x + h - 1, g.y, h)) {
                        break
                    }
                    h++
                }
                g.h = h - 1
            }
        }
    }

    fun hasRowZero(x: Int, y: Int, w: Int): Boolean {
        for (c in 0..<w) {
            if (map[x][y + c] == 0) {
                return true
            }
        }
        return false
    }

    fun hasColZero(x: Int, y: Int, h: Int): Boolean {
        for (r in 0..<h) {
            if (map[x + r][y] == 0) {
                return true
            }
        }
        return false
    }


    fun eliminate() {
        for (i in groups.indices) {
            val g = groups[i]
            val cov = Array(g.h) { BooleanArray(g.w) }
            for (j in groups.indices) {
                if (i == j) continue
                val g2 = groups[j]
                for (a in 0..<g2.h) {
                    for (b in 0..<g2.w) {
                        val x1 = g2.x - g.x + a
                        val y1 = g2.y - g.y + b
                        if (x1 < 0 || y1 < 0) continue
                        if (x1 < cov.size && y1 < cov[0].size) {
                            cov[x1][y1] = true
                        }
                    }
                }
            }
            var covered = true
            for (b in cov) {
                for (cell in b) {
                    if (!cell) {
                        covered = false
                        break
                    }
                }
            }
            if (covered) {
                groups.removeAt(i)
                eliminate()
                return
            }
        }
    }

    class Group(var x: Int, var y: Int, var w: Int, var h: Int) {
        override fun toString(): String {
            return String.format("(%d,%d,%d,%d)", x, y, w, h)
        }
    }

    companion object {
        @JvmStatic
        fun solve(f: MathFunction?): MathFunction {
            return Karnaugh(f).solve()
        }

        fun isPowerOf2(n: Int): Boolean {
            val d = ln(n.toDouble()) / ln(2.0)
            return d.toInt().toDouble() == d
        }
    }
}



