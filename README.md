# mamath_boolean
library for Boolean algebra for jvm

Example:
````kotlin
fun main(args: Array<String>) {
    val sum = MathFunction.parse("((a xor b) xor c) xor t")
    println(sum)
    println(TruthTable(sum))
}
````

Result:
````
((a^b)^c)^t

F1=((a^b)^c)^t
a b c t | F1
------------
0 0 0 0 | 0
0 0 0 1 | 1
0 0 1 0 | 1
0 0 1 1 | 0
0 1 0 0 | 1
0 1 0 1 | 0
0 1 1 0 | 0
0 1 1 1 | 1
1 0 0 0 | 1
1 0 0 1 | 0
1 0 1 0 | 0
1 0 1 1 | 1
1 1 0 0 | 0
1 1 0 1 | 1
1 1 1 0 | 1
1 1 1 1 | 0
````
