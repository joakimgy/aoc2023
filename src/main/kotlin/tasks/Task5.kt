import utils.readRows
import java.io.File
import kotlin.collections.HashMap
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
private fun main() {
    measureTimedValue {
        part1()
    }.let {
        println("Part 1 answer: ${it.value} (${it.duration.inWholeMilliseconds} ms)")
    }
    part2()
}

private fun part1(): Long {
    val seeds = readRows("Task5_Input_1").first().substringAfter(": ").split(" ").map { it.toLong() }
    val mappers = parseInput("Task5_Input_1")
    return seeds
        .parallelStream()
        .map { findLocation("seed", it, mappers) }
        .toList()
        .min()
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

private fun part2() {
    return
}

fun parseInput(filename: String): HashMap<String, List<CategoryMap>> {
    val map: HashMap<String, List<CategoryMap>> = HashMap()
    var lastSource = ""
    var lastDestination = ""
    File("src/main/kotlin/tasks/$filename").forEachLine { line ->
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
        (map.sourceRangeStart..map.sourceRangeStart + map.range)
            .indexOf(sourceNumber)
            .let {
                if (it == -1) return@forEach
                return MapperResult(destinationNumber = map.destinationRangeStart + it, destinationCategory = destination)
            }
    }
    return MapperResult(destinationNumber = sourceNumber, destinationCategory = destination)
}
