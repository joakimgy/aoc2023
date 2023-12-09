package days

import Day1
import Day2
import Day3
import Day4
import Day5
import kotlinx.coroutines.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

suspend fun main() {
    runLatest()
}

suspend fun runAll() {
    days.forEachIndexed { index, day ->
        runDay(day, index + 1, 2000L)
    }
}

suspend fun runLatest() {
    days.last().also {
        runDay(it, days.size)
    }
}

@OptIn(ExperimentalTime::class, DelicateCoroutinesApi::class)
suspend fun runDay(day: Day, dayNumber: Int, timeoutMs: Long? = null) {
    try {
        withTimeout(timeoutMs ?: (1000 * 60 * 60L)) {
            GlobalScope.launch(Dispatchers.IO) {
                measureTimedValue {
                    day.part1()
                }.let {
                    println("Day $dayNumber Part 1: ${it.value} (${it.duration})")
                }
                measureTimedValue {
                    day.part2()
                }.let {
                    println("Day $dayNumber Part 2: ${it.value} (${it.duration})")
                }
            }.join()
        }
    } catch (e: TimeoutCancellationException) {
        println("Skipping Day $dayNumber because it timed out")
    }
}

val days: List<Day> = listOf(
    Day1(),
    Day2(),
    Day3(),
    Day4(),
    Day5(),
    Day6(),
    Day7(),
    Day8(),
    Day9(),
)
