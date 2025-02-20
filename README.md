<div align="center">
  <img src="icon.png" width="300">
   <h2>mamath boolean</h2>
library for Boolean algebra for jvm
</div>


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
