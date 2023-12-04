import utils.readRows
import kotlin.math.pow

private fun main() {
    part1()
    part2()
}

private fun part1() {
    readRows("Task4_Input_1").sumOf { row ->
        findWinningNumbers(row).let { winningNumbers ->
            findNumbersYouHave(row).filter {
                winningNumbers.contains(it)
            }.let {
                2.0.pow(it.size - 1).toInt()
            }
        }
    }.also {
        println("The cards are worth $it points")
    }
}

data class Scratchcard(
    val cardNumber: Int,
    val numberOfMatches: Int,
    val copies: Int,
)
private fun part2() {
    readRows("Task4_Input_1").mapIndexed { index, row ->
        val numbersYouHave = findNumbersYouHave(row)
        val winningNumbers = findWinningNumbers(row)
        val numberOfMatches = numbersYouHave.filter { winningNumbers.contains(it) }.size
        Scratchcard(cardNumber = index + 1, numberOfMatches = numberOfMatches, copies = 1)
    }.associateBy { it.cardNumber }
        .toMutableMap()
        .also { map ->
            map.forEach { (cardNumber, scratchcard) ->
                if (scratchcard.numberOfMatches == 0) {
                    return@forEach
                }
                (0 until scratchcard.copies).forEach { _ ->
                    (cardNumber + 1..cardNumber + scratchcard.numberOfMatches).forEach {
                        map[it] =
                            map[it]!!.copy(copies = map[it]!!.copies + 1)
                    }
                }
            }
        }.values
        .sumOf { it.copies }
        .also {
            println("Ended up with $it scratchcards")
        }
}

private fun findNumbersYouHave(row: String): List<Int> = row
    .substringAfter(":")
    .substringBefore("|")
    .split(" ")
    .filter { it.isNotEmpty() }
    .map { it.toInt() }

private fun findWinningNumbers(row: String): List<Int> = row
    .substringAfter("|")
    .split(" ")
    .filter { it.isNotEmpty() }
    .map { it.toInt() }
