import utils.readFile
import utils.readRows
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
    measureTimedValue {
        part2()
    }.let {
        println("Part 2 answer: ${it.value} (${it.duration.inWholeMilliseconds} ms)")
    }
}

private fun part1(): Long? {
    val seeds = readRows("Task5_Input_1").first().substringAfter(": ").split(" ").map { it.toLong() }
    val mappers = parseInput("Task5_Input_1")
    return seeds.minOfOrNull { findLocation("seed", it, mappers) }
}

private fun part2(): Long {
    val seeds = readRows("Task5_Input_1")
        .first().substringAfter(": ").split(" ").map { it.toLong() }
        .chunked(2)
        .map { (start, length) -> start until start + length }

    println("Running part 2 with ${seeds.sumOf { it.last - it.first + 1 }} seeds")
    var seedCount = 0L
    val onePercent = 2387882574L / 100L
    val mappers = parseInput("Task5_Input_1")
    return seeds
        .asSequence()
        .map { range ->
            range
                .asSequence()
                .map {
                    seedCount++
                    if (seedCount % onePercent == 0L) {
                        println("Computed $seedCount seeds (${seedCount / onePercent}%)")
                    }
                    findLocation("seed", it, mappers)
                }.min()
        }
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
        if ((map.sourceRangeStart..map.sourceRangeStart + map.range).contains(sourceNumber)) {
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
