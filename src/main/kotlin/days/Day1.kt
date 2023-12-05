import days.Day
import utils.indexesOf
import utils.readRows

class Day1 : Day {
    override fun part1(): String =
        readRows("Task1_Input_1")
            .sumOf { row ->
                row.filter {
                    it.isDigit()
                }.let {
                    "${it.first()}${it.last()}".toInt()
                }
            }.toString()

    override fun part2(): String {
        val rows = readRows("Task1_Input_2")
        return rows
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
            }.toString()
    }
}

private data class NumberWithIndex(
    val number: Int,
    val index: Int,
)
