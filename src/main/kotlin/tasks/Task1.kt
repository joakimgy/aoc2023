import utils.readRows

val input = listOf(
    "1abc2",
    "pqr3stu8vwx",
    "a1b2c3d4e5f",
    "treb7uchet",
)

fun main() {
    part1()
    part2()
}

fun part1() {
    val sum = readRows("Task1_Input_1")
        .sumOf { row ->
            row.filter {
                it.isDigit()
            }.let {
                "${it.first()}${it.last()}".toInt()
            }
        }
    println("Answer to task 1 part 1 is $sum")
}

data class NumberWithIndex(
    val number: Int,
    val index: Int,
)

fun part2() {
    val rows = readRows("Task1_Input_2")
    val textNumbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    rows
        .sumOf { row ->
            val textNumbers = textNumbers.mapIndexed { index, text ->
                val indexes = row.indexesOf(text)
                if (indexes.isNotEmpty()) {
                    indexes.map {
                        NumberWithIndex(number = index + 1, index = it)
                    }
                } else {
                    null
                }
            }
                .filterNotNull()
                .flatten()

            val actualNumbers = row.mapIndexed { index, char ->
                if (char.isDigit()) {
                    NumberWithIndex(number = "$char".toInt(), index = index)
                } else {
                    null
                }
            }.mapNotNull { it }
            (textNumbers + actualNumbers)
                .let { numbers ->
                    val first = numbers.minBy { it.index }.number
                    val last = numbers.maxBy { it.index }.number
                    "${first}$last".toInt()
                }
        }.let {
            println("Answer to task 1 part 2 is $it")
        }
}

fun ignoreCaseOpt(ignoreCase: Boolean) =
    if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else emptySet()

fun String?.indexesOf(pat: String, ignoreCase: Boolean = true): List<Int> =
    pat.toRegex(ignoreCaseOpt(ignoreCase))
        .findAll(this ?: "")
        .map { it.range.first }
        .toList()
