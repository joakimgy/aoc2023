package days

import utils.readRows

class Day6 : Day {

    override fun part1(): String =
        readRows("Task6_Input_1").map { row ->
            Regex("\\d+")
                .findAll(row)
                .map { it.value.toLong() }
                .toList()
        }.let {
            it[0].zip(it[1])
        }.map { (time, record) ->
            calculateDistances(time).filter { it > record }.size
        }.reduce { acc, i -> acc * i }
            .toString()

    override fun part2(): String = readRows("Task6_Input_1").map { row ->
        Regex("\\d+")
            .findAll(row)
            .map { it.value }
            .toList()
            .joinToString(separator = "")
            .toLong()
    }
        .let { (time, record) ->
            calculateDistances(time).filter { it > record }.size
        }
        .toString()
}

private fun calculateDistances(time: Long): List<Long> =
    (0..time).map {
        val startingSpeed = it
        val timeForRacing = time - startingSpeed
        startingSpeed * timeForRacing
    }
