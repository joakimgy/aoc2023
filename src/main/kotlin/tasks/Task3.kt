import utils.readRows

fun main() {
    part1()
    part2()
}

private fun part1() {
    val symbols = findSymbols(null)
    findEngineParts()
        .filter { it.isValidPartNumber(symbols) }
        .sumOf { it.number.toInt() }
        .let {
            println("The sum of the part numbers is $it")
        }
}

private fun part2() =
    findSymbols('*').mapNotNull { gearSymbol ->
        findEngineParts()
            .filter { it.isValidPartNumber(listOf(gearSymbol)) }
            .let {
                if (it.size == 2) {
                    it.map { n -> n.number.toInt() }.reduce(Int::times)
                } else {
                    null
                }
            }
    }
        .sum()
        .let {
            println("Sum of the gear ratios is $it")
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
            } else {
                if (specificSymbol != null) {
                    if (char == specificSymbol) {
                        Symbol(coordinate = Coordinate(x = colIndex, y = rowIndex), symbol = char)
                    } else {
                        null
                    }
                } else {
                    Symbol(coordinate = Coordinate(x = colIndex, y = rowIndex), symbol = char)
                }
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

            schematics.toList()
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
        }.flatten()

private fun EnginePart.isValidPartNumber(symbols: List<Symbol>): Boolean =
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
