package days

import Day1
import Day2
import Day3
import Day4
import Day5
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

fun main() {
    runLatest()
}

fun runAll() {
    days.forEachIndexed { index, day ->
        runDay(day, index + 1)
    }
}

fun runLatest() {
    days.last().also {
        runDay(it, days.size)
    }
}

@OptIn(ExperimentalTime::class)
fun runDay(day: Day, dayNumber: Int) {
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
}

val days: List<Day> = listOf(
    Day1(),
    Day2(),
    Day3(),
    Day4(),
    Day5(),
    Day6(),
    Day7(),
)
