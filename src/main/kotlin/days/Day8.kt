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
            return "Did not find ZZZ"
        }

    /** Least common multiple? */
    override fun part2(): String = readRows("Day8_2")
        .let { rows ->
            val networkMap = rows.toNetworkMap()
            val instructions = rows[0]
            var currentNodes = networkMap.keys.filter { it.last() == 'A' }
            val denominators: MutableMap<Int, Long?> = List(currentNodes.size) { index -> index to null }.toMap().toMutableMap()

            for (i in 0..Int.MAX_VALUE) {
                if (denominators.values.all { it != null }) {
                    break
                }
                currentNodes = currentNodes.mapIndexed { index, currentNode ->
                    instructions[i % instructions.length]
                        .readInstruction(currentNode, networkMap)
                        .let { newNode ->
                            if (newNode.last() == 'Z') {
                                denominators[index] = i + 1L
                            }
                            newNode
                        }
                }.also { newNodes ->
                    if (newNodes.all { it.last() == 'Z' }) {
                        return (i + 1).toString()
                    }
                }
            }

            return denominators.values.toList().filterNotNull().reduce(::lcm).toString()
        }
}

private fun List<String>.toNetworkMap() = this.drop(2)
    .associate { row ->
        Regex("[1-9A-Z]+").findAll(row)
            .map { match -> match.value }.toList()
            .let { it[0] to Pair(it[1], it[2]) }
    }

private fun Char.readInstruction(key: String, networkMap: Map<String, Pair<String, String>>) = when (this) {
    'L' -> networkMap[key]?.first ?: throw Error("Did not find key $key")
    'R' -> networkMap[key]?.second ?: throw Error("Did not find key $key")
    else -> throw Error("Invalid instruction $this")
}

private fun lcm(first: Long, second: Long): Long {
    return first * second / gcd(first, second)
}

private fun gcd(first: Long, second: Long): Long {
    return if (second == 0L) first else gcd(second, first % second)
}
