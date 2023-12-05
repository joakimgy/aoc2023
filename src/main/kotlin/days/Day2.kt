import days.Day
import utils.readRows

class Day2 : Day {

    override fun part1(): String {
        val rows = readRows("Task2_Input_1")
        val validCubes = CubeSet(red = 12, green = 13, blue = 14)
        return rows
            .map { rowToGame(it) }
            .filter { it.hasValidSets(validCubes) }
            .sumOf { it.gameNumber }
            .toString()
    }

    override fun part2(): String {
        val rows = readRows("Task2_Input_1")
        return rows
            .map { rowToGame(it) }
            .sumOf { game ->
                game.sets.maxBy { it.green }.green *
                    game.sets.maxBy { it.blue }.blue *
                    game.sets.maxBy { it.red }.red
            }
            .toString()
    }
}

private fun Game.hasValidSets(validSet: CubeSet): Boolean {
    return this.sets
        .all {
            it.red <= validSet.red &&
                it.blue <= validSet.blue &&
                it.green <= validSet.green
        }
}

private data class Game(
    val gameNumber: Int,
    val sets: List<CubeSet>,
)

private data class CubeSet(
    val blue: Int,
    val red: Int,
    val green: Int,
)

private fun rowToGame(row: String): Game {
    val gameNumber = row.substringBefore(":").substringAfter(" ").toInt()
    val set = row.substringAfter(":").split(";").map {
        val blue = Regex("\\d+ blue").find(it)?.value?.substringBefore(" ")?.toInt()
        val red = Regex("\\d+ red").find(it)?.value?.substringBefore(" ")?.toInt()
        val green = Regex("\\d+ green").find(it)?.value?.substringBefore(" ")?.toInt()
        CubeSet(blue = blue ?: 0, red = red ?: 0, green = green ?: 0)
    }
    return Game(gameNumber = gameNumber, sets = set)
}
