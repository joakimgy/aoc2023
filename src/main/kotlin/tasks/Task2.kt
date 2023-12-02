package tasks

import utils.readRows

fun main() {
    part1()
    part2()
}

fun part1() {
    val rows = readRows("Task2_Input_1")
    val validCubes = CubeSet(red = 12, green = 13, blue = 14)
    rows
        .map { rowToGame(it) }
        .filter { it.hasValidSets(validCubes) }
        .sumOf { it.gameNumber }
        .also {
            println("The sum of valid game IDs is $it")
        }
}

fun part2() {
    val rows = readRows("Task2_Input_1")
    rows
        .map { rowToGame(it) }
        .sumOf { game ->
            game.sets.maxBy { it.green }.green *
                game.sets.maxBy { it.blue }.blue *
                game.sets.maxBy { it.red }.red
        }
        .also {
            println(" The sum of the power of these sets are $it")
        }
}

fun Game.hasValidSets(validSet: CubeSet): Boolean {
    return this.sets
        .all {
            it.red <= validSet.red &&
                it.blue <= validSet.blue &&
                it.green <= validSet.green
        }
}

data class Game(
    val gameNumber: Int,
    val sets: List<CubeSet>,
)

data class CubeSet(
    val blue: Int,
    val red: Int,
    val green: Int,
)

fun rowToGame(row: String): Game {
    val gameNumber = row.split(":").first().split(" ").last().toInt()
    val set = row.split(":").last().split(";").map {
        val blue = Regex("\\d+ blue").find(it)?.value?.split(" ")?.first()?.toInt()
        val red = Regex("\\d+ red").find(it)?.value?.split(" ")?.first()?.toInt()
        val green = Regex("\\d+ green").find(it)?.value?.split(" ")?.first()?.toInt()
        CubeSet(blue = blue ?: 0, red = red ?: 0, green = green ?: 0)
    }
    return Game(gameNumber = gameNumber, sets = set)
}
