import days.Day
import utils.readRows

class Day3 : Day {
    override fun part1(): String {
        val allSymbols = findSymbols(null)
        return findEngineParts()
            .filter { it.hasValidPartNumber(allSymbols) }
            .sumOf { it.number.toInt() }
            .toString()
    }

    override fun part2(): String =
        findSymbols('*').mapNotNull { gearSymbol ->
            findEngineParts()
                .filter { it.hasValidPartNumber(listOf(gearSymbol)) }
                .let {
                    if (it.size == 2) {
                        it.first().number.toInt() * it.last().number.toInt()
                    } else {
                        null
                    }
                }
        }
            .sum()
            .toString()
}

data class Symbol(
    val symbol: Char,
    val coordinate: Coordinate,
)
fun findSymbols(specificSymbol: Char?): List<Symbol> {
    val rows = readRows("Task3_Input_1")
    return rows.mapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { colIndex, char ->
            if (char.isDigit() || char == '.') {
                null
            } else if (specificSymbol == null) {
                Symbol(coordinate = Coordinate(x = colIndex, y = rowIndex), symbol = char)
            } else if (char == specificSymbol) {
                Symbol(coordinate = Coordinate(x = colIndex, y = rowIndex), symbol = char)
            } else {
                null
            }
        }
    }.flatten()
}

data class EnginePart(
    val number: String,
    val coordinates: List<Coordinate>,
)
fun findEngineParts() =
    readRows("Task3_Input_1")
        .mapIndexed { rowIndex, row ->
            parseRow(row, rowIndex)
        }.flatten()

fun parseRow(row: String, rowIndex: Int): List<EnginePart> {
    val schematics = mutableListOf<MutableList<EnginePart>>()
    row.mapIndexed { columnIndex, char ->
        if (char.isDigit()) {
            EnginePart(number = char.toString(), coordinates = listOf(Coordinate(x = columnIndex, y = rowIndex)))
        } else {
            null
        }
    }.forEach {
        if (it == null) {
            schematics.add(mutableListOf())
        } else {
            if (schematics.isEmpty()) {
                schematics.add(mutableListOf(it))
            } else {
                schematics.last().add(it)
            }
        }
    }

    return schematics.toList()
        .map { it.toList() }
        .filter { schematic ->
            schematic.isNotEmpty()
        }.map {
            val coordinates = mutableListOf<Coordinate>()
            var number: String = ""
            it.forEach { c ->
                coordinates.add(c.coordinates.first())
                number += c.number
            }
            EnginePart(number = number, coordinates = coordinates)
        }
}

private fun EnginePart.hasValidPartNumber(symbols: List<Symbol>): Boolean =
    symbols.any { symbol ->
        this.coordinates.any { coordinate ->
            (
                symbol.coordinate.x == coordinate.x ||
                    symbol.coordinate.x == coordinate.x - 1 ||
                    symbol.coordinate.x == coordinate.x + 1
                ) &&
                (
                    symbol.coordinate.y == coordinate.y ||
                        symbol.coordinate.y == coordinate.y - 1 ||
                        symbol.coordinate.y == coordinate.y + 1
                    )
        }
    }

data class Coordinate(
    val x: Int,
    val y: Int,
)
