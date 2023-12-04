import utils.indexesOf
import utils.readRows

private fun main() {
    part1()
    part2()
}

private fun part1() {
    readRows("Task1_Input_1")
        .sumOf { row ->
            row.filter {
                it.isDigit()
            }.let {
                "${it.first()}${it.last()}".toInt()
            }
        }.also {
            println("Answer to task 1 part 1 is $it")
        }
}

private fun part2() {
    val rows = readRows("Task1_Input_2")
    rows
        .sumOf { row ->
            val numbersWithIndexFromText =
                listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
                    .mapIndexedNotNull { index, text ->
                        val indexes = row.indexesOf(text)
                        if (indexes.isNotEmpty()) {
                            indexes.map {
                                NumberWithIndex(number = index + 1, index = it)
                            }
                        } else {
                            null
                        }
                    }
                    .flatten()

            val numbersWithIndexFromDigit = row
                .mapIndexedNotNull { index, char ->
                    if (char.isDigit()) {
                        NumberWithIndex(number = "$char".toInt(), index = index)
                    } else {
                        null
                    }
                }

            (numbersWithIndexFromText + numbersWithIndexFromDigit)
                .let { numbers ->
                    val first = numbers.minBy { it.index }.number
                    val last = numbers.maxBy { it.index }.number
                    "${first}$last".toInt()
                }
        }.let {
            println("Answer to task 1 part 2 is $it")
        }
}

private data class NumberWithIndex(
    val number: Int,
    val index: Int,
)
