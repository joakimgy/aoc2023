import days.Day
import utils.readFile
import utils.readRows
import kotlin.collections.HashMap

class Day5 : Day {

    override fun part1(): String {
        val seeds = readRows("Task5_Input_1").first().substringAfter(": ").split(" ").map { it.toLong() }
        val mappers = parseInput("Task5_Input_1")
        return seeds.minOfOrNull { findLocation("seed", it, mappers) }.toString()
    }

    override fun part2(): String {
        val seeds = readRows("Task5_Input_1")
            .first().substringAfter(": ").split(" ").map { it.toLong() }
            .chunked(2)
            .map { (start, length) -> start until start + length }

        var seedCount = 0L
        val mappers = parseInput("Task5_Input_1")
        return seeds
            .asSequence()
            .map { range ->
                range
                    .asSequence()
                    .map {
                        seedCount++
                        findLocation("seed", it, mappers)
                    }.min()
            }
            .min()
            .toString()
    }
}

fun findLocation(sourceCategory: String, source: Long, mappers: HashMap<String, List<CategoryMap>>): Long =
    (mappers[sourceCategory] ?: throw Error("Could not find mapper for $sourceCategory"))
        .findDestination(source)
        .let {
            if (it.destinationCategory == "location") {
                return it.destinationNumber
            } else {
                findLocation(it.destinationCategory, it.destinationNumber, mappers)
            }
        }

data class CategoryMap(
    val source: String,
    val destination: String,
    val destinationRangeStart: Long,
    val sourceRangeStart: Long,
    val range: Long,
)

data class MapperResult(val destinationNumber: Long, val destinationCategory: String)

private fun List<CategoryMap>.findDestination(sourceNumber: Long): MapperResult {
    val destination = this.first().destination
    this.forEach { map ->
        if ((map.sourceRangeStart until map.sourceRangeStart + map.range).contains(sourceNumber)) {
            return MapperResult(
                destinationNumber = map.destinationRangeStart + (sourceNumber - map.sourceRangeStart),
                destinationCategory = destination,
            )
        }
    }
    return MapperResult(destinationNumber = sourceNumber, destinationCategory = destination)
}

fun parseInput(filename: String): HashMap<String, List<CategoryMap>> {
    val map: HashMap<String, List<CategoryMap>> = HashMap()
    var lastSource = ""
    var lastDestination = ""
    readFile(filename).forEachLine { line ->
        if (line.startsWith("seeds")) {
            return@forEachLine
        } else if (line.isEmpty()) {
            return@forEachLine
        } else if (line.contains("map:")) {
            lastSource = line.substringBefore("-")
            lastDestination = line.substringBefore(" map:").split("-").last()
            map[lastSource] = emptyList()
        } else {
            map[lastSource] = map[lastSource]?.plus(
                line.split(" ").map { it.toLong() }.let {
                    CategoryMap(
                        source = lastSource,
                        destination = lastDestination,
                        destinationRangeStart = it[0],
                        sourceRangeStart = it[1],
                        range = it[2],
                    )
                },
            ) ?: emptyList()
        }
    }
    return map
}
