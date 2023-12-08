package days

import utils.lcm
import utils.readRows

class Day8 : Day {

    override fun part1(): String = readRows("Day8")
        .let { rows ->
            return goToDestination(
                networkMap = rows.toNetworkMap(),
                instructions = rows[0],
                startNode = "AAA",
                isGoalNode = { node -> node == "ZZZ" },
            ).toString()
        }

    override fun part2(): String = readRows("Day8_2")
        .let { rows ->
            val networkMap = rows.toNetworkMap()
            networkMap.keys.filter { it.last() == 'A' }
                .map {
                    goToDestination(
                        networkMap = networkMap,
                        instructions = rows[0],
                        startNode = it,
                        isGoalNode = { node -> node.last() == 'Z' },
                    )
                }.reduce(::lcm).toString()
        }
}

private fun List<String>.toNetworkMap() = this.drop(2)
    .associate { row ->
        Regex("[1-9A-Z]+").findAll(row)
            .map { match -> match.value }.toList()
            .let { it[0] to Pair(it[1], it[2]) }
    }

private fun goToDestination(
    networkMap: Map<String, Pair<String, String>>,
    instructions: String,
    startNode: String,
    isGoalNode: (node: String) -> Boolean,
): Long {
    var currentNode = startNode
    var step = 0L
    while (!isGoalNode(currentNode)) {
        currentNode = when (val instruction = instructions[(step % instructions.length).toInt()]) {
            'L' -> networkMap[currentNode]?.first ?: throw Error("Did not find currentNode $currentNode")
            'R' -> networkMap[currentNode]?.second ?: throw Error("Did not find currentNode $currentNode")
            else -> throw Error("Invalid instruction $instruction")
        }
        step++
    }
    return step
}
