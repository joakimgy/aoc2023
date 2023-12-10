package days

import utils.readRows
import java.lang.IndexOutOfBoundsException

class Day10 : Day {

    override fun part1(): String = readRows("Day10")
        .map { row ->
            row.map { Pipe.from(c = it) }
        }
        .let { grid ->
            val startPosition = grid.findStart()
            val history = mutableListOf(startPosition)
            while (true) {
                val nextPosition = grid.moveToNextPipe(history)
                if (nextPosition == startPosition) break
                history.add(nextPosition)
            }
            history
        }
        .let { loop ->
            loop.size / 2
        }
        .toString()

    override fun part2(): String = ""
}

private fun List<List<Pipe>>.findStart(): Pair<Int, Int> =
    this.mapIndexedNotNull { y, pipes ->
        val rowWithStart = pipes.mapIndexedNotNull { x, pipe ->
            if (pipe == Pipe.Start) x else null
        }
        if (rowWithStart.isNotEmpty()) Pair(rowWithStart.first(), y) else null
    }.first()

private fun List<List<Pipe>>.moveToNextPipe(history: List<Pair<Int, Int>>): Pair<Int, Int> {
    return this
        .checkPossibleMoves(
            history.last(),
        )
        .firstOrNull {
            if (history.size == 1) {
                true
            } else {
                it != history.dropLast(1).last()
            }
        } ?: throw Error(
        """Could not find next pipe for ${history.takeLast(2)}.
            Nearby: ${this.findNearbyPipes(history.last())}
            History: $history
        """.trimMargin(),
    )
}

private fun List<List<Pipe>>.findNearbyPipes(pos: Pair<Int, Int>): Map<String, Pair<Pipe?, Pair<Int, Int>>> {
    return mapOf(
        "west" to Pair(this.getPipe(pos.first - 1, pos.second), Pair(pos.first - 1, pos.second)),
        "east" to Pair(this.getPipe(pos.first + 1, pos.second), Pair(pos.first + 1, pos.second)),
        "south" to Pair(this.getPipe(pos.first, pos.second + 1), Pair(pos.first, pos.second + 1)),
        "north" to Pair(this.getPipe(pos.first, pos.second - 1), Pair(pos.first, pos.second - 1)),
    )
}

private fun List<List<Pipe>>.getPipe(x: Int, y: Int): Pipe? {
    return try {
        this[y][x]
    } catch (e: IndexOutOfBoundsException) {
        null
    }
}

private fun List<List<Pipe>>.checkPossibleMoves(position: Pair<Int, Int>): List<Pair<Int, Int>> {
    val currentPipe = this[position.second][position.first]
    return this
        .findNearbyPipes(position)
        .filter {
            when (it.key) {
                "west" -> it.value.first?.let { it1 -> currentPipe.connectsToWest(it1) }
                "east" -> it.value.first?.let { it1 -> currentPipe.connectsToEast(it1) }
                "south" -> it.value.first?.let { it1 -> currentPipe.connectsToSouth(it1) }
                "north" -> it.value.first?.let { it1 -> currentPipe.connectsToNorth(it1) }
                else -> throw Error("Could not find connection for pipe")
            } ?: false
        }.map { it.value.second }
}

private enum class Pipe(c: Char) {
    NorthSouth('|'),
    EastWest('-'),
    NorthEast('L'),
    NorthWest('J'),
    SouthWest('7'),
    SouthEast('F'),
    Ground('.'),
    Start('S'),
    ;

    companion object {
        fun from(c: Char) = when (c) {
            '|' -> NorthSouth
            '-' -> EastWest
            'L' -> NorthEast
            'J' -> NorthWest
            '7' -> SouthWest
            'F' -> SouthEast
            '.' -> Ground
            'S' -> Start
            else -> throw Error("Could to parse $c to Pipe")
        }
    }
}

private fun Pipe.connectsToSouth(other: Pipe): Boolean {
    return when (this) {
        Pipe.NorthSouth, Pipe.SouthWest, Pipe.SouthEast, Pipe.Start -> {
            when (other) {
                Pipe.NorthSouth, Pipe.NorthEast, Pipe.NorthWest, Pipe.Start -> true
                else -> false
            }
        }
        else -> false
    }
}

private fun Pipe.connectsToNorth(other: Pipe): Boolean {
    return when (this) {
        Pipe.NorthSouth, Pipe.NorthWest, Pipe.NorthEast, Pipe.Start -> {
            when (other) {
                Pipe.SouthWest, Pipe.SouthEast, Pipe.NorthSouth, Pipe.Start -> true
                else -> false
            }
        }
        else -> false
    }
}

private fun Pipe.connectsToWest(other: Pipe): Boolean {
    return when (this) {
        Pipe.NorthWest, Pipe.SouthWest, Pipe.EastWest, Pipe.Start -> {
            when (other) {
                Pipe.EastWest, Pipe.NorthEast, Pipe.SouthEast, Pipe.Start -> true
                else -> false
            }
        }
        else -> false
    }
}

private fun Pipe.connectsToEast(other: Pipe): Boolean {
    return when (this) {
        Pipe.EastWest, Pipe.NorthEast, Pipe.SouthEast, Pipe.Start -> {
            when (other) {
                Pipe.EastWest, Pipe.SouthWest, Pipe.NorthWest, Pipe.Start -> true
                else -> false
            }
        }
        else -> false
    }
}
