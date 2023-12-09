package days

import utils.readRows

class Day9 : Day {

    override fun part1(): String = readRows("Day9")
        .map { row -> row.split(" ").map { it.toInt() } }
        .map { row -> row.createPyramid() }
        .map { row -> row.extraPolate() }
        .sumOf { row -> row[0]?.last() ?: throw Error("Did not find last value on row $row") }
        .toString()

    override fun part2(): String = readRows("Day9")
        .map { row -> row.split(" ").map { it.toInt() } }
        .map { row -> row.createPyramid() }
        .map { row -> row.extraPolate(backwards = true) }
        .sumOf { row -> row[0]?.first() ?: throw Error("Did not find first value on row $row") }
        .toString()
}

private fun List<Int>.createPyramid(): MutableMap<Int, List<Int>> {
    val pyramid: MutableMap<Int, List<Int>> = mutableMapOf(0 to this)
    while (true) {
        val largestIndex = pyramid.maxOf { (key, _) -> key }
        val pyramidLayer = pyramid[largestIndex] ?: throw Error("Did not find pyramid layer $largestIndex")
        pyramidLayer.mapIndexedNotNull { index, value ->
            when (index) {
                pyramidLayer.size - 1 -> null
                else -> pyramidLayer[index + 1] - value
            }
        }.also { layer ->
            pyramid[largestIndex + 1] = layer
            if (layer.all { it == 0 }) {
                return pyramid
            }
        }
    }
}

private fun MutableMap<Int, List<Int>>.extraPolate(backwards: Boolean = false): MutableMap<Int, List<Int>> {
    val largestIndex = this.maxOf { (key, _) -> key }
    (0..largestIndex).reversed().forEach { index ->
        when (index) {
            largestIndex -> {
                this[index] = this[index]!!.plus(listOf(0))
            }
            else -> {
                if (backwards) {
                    val extrapolatedValue = this[index]!!.first() - this[index + 1]!!.first()
                    this[index] = listOf(extrapolatedValue).plus(this[index]!!)
                } else {
                    val extrapolatedValue = this[index]!!.last() + this[index + 1]!!.last()
                    this[index] = this[index]!!.plus(extrapolatedValue)
                }
            }
        }
    }
    return this
}
