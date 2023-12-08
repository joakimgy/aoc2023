package days

import utils.readRows

class Day8 : Day {

    override fun part1(): String = readRows("Day8")
        .let { rows ->
            val networkMap = rows.toNetworkMap()
            val instructions = rows[0]
            var currentNode = "AAA"
            for (i in 0..Int.MAX_VALUE) {
                instructions[i % instructions.length]
                    .readInstruction(currentNode, networkMap)
                    .let { newNode ->
                        if (newNode == "ZZZ") {
                            return (i + 1).toString()
                        } else {
                            currentNode = newNode
                        }
                    }
            }
            return "Did not find"
        }

    override fun part2(): String = "Part2"
}

private fun List<String>.toNetworkMap() = this.drop(2)
    .associate { row ->
        Regex("[A-Z]+").findAll(row)
            .map { match -> match.value }.toList()
            .let { it[0] to Pair(it[1], it[2]) }
    }

private fun Char.readInstruction(key: String, networkMap: Map<String, Pair<String, String>>) = when (this) {
    'L' -> networkMap[key]?.first ?: throw Error("Did not find key $key")
    'R' -> networkMap[key]?.second ?: throw Error("Did not find key $key")
    else -> throw Error("Invalid instruction $this")
}
