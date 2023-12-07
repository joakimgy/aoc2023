package days

import utils.readRows

class Day7 : Day {

    override fun part1(): String {
        return readRows("Day7")
            .map { row ->
                row
                    .split(" ")
                    .let {
                        Hand(
                            type =
                            it.first().toType(),
                            cards = it.first(),
                            bid = it.last().toInt(),
                        )
                    }
            }
            .sortedWith { h1, h2 -> Type.values().indexOf(h1.type) - Type.values().indexOf(h2.type) }
            .groupBy { it.type }
            .flatMap { (_, hands) ->
                hands
                    .sortedWith { h1, h2 ->
                        h1.cards.toCharArray().zip(h2.cards.toCharArray()).firstNotNullOf { (c1, c2) ->
                            val c1Value = cardRating.indexOf(c1)
                            val c2Value = cardRating.indexOf(c2)
                            if (c1Value == c2Value) {
                                null
                            } else {
                                c1Value - c2Value
                            }
                        }
                    }
            }
            .reversed()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
            .toString()
    }

    // Last answer: 251106438
    override fun part2(): String {
        return readRows("Day7")
            .map { row ->
                row
                    .split(" ")
                    .let {
                        Hand(
                            type = it.first().toTypeWithJoker(),
                            cards = it.first(),
                            bid = it.last().toInt(),
                        )
                    }
            }
            .sortedWith { h1, h2 -> Type.values().indexOf(h1.type) - Type.values().indexOf(h2.type) }
            .groupBy { it.type }
            .flatMap { (_, hands) ->
                hands
                    .sortedWith { h1, h2 ->
                        h1.cards.toCharArray().zip(h2.cards.toCharArray()).firstNotNullOf { (c1, c2) ->
                            val c1Value = cardRatingWithJoker.indexOf(c1)
                            val c2Value = cardRatingWithJoker.indexOf(c2)
                            if (c1Value == c2Value) {
                                null
                            } else {
                                c1Value - c2Value
                            }
                        }
                    }
            }
            .reversed()
            .also { it.forEach { h -> println(h) } }
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
            .toString()
    }
}

val cardRating = listOf(
    'A',
    'K',
    'Q',
    'J',
    'T',
    '9',
    '8',
    '7',
    '6',
    '5',
    '4',
    '3',
    '2',
)

data class Hand(
    val type: Type,
    val cards: String,
    val bid: Int,
)

enum class Type {
    FiveOfAKind,
    FourOfAKind,
    FullHouse,
    ThreeOfAKind,
    TwoPair,
    OnePair,
    HighCard,
}

fun String.toType(): Type =
    this
        .map { c -> Pair(c, this.count { c == it }) }
        .distinctBy { it.first }
        .map { it.second }
        .toType()

fun List<Int>.toType(): Type {
    if (this.contains(5)) {
        return Type.FiveOfAKind
    } else if (this.contains(4)) {
        return Type.FourOfAKind
    } else if (this.contains(3) && this.contains(2)) {
        return Type.FullHouse
    } else if (this.contains(3)) {
        return Type.ThreeOfAKind
    } else if (this.count { it == 2 } == 2) {
        return Type.TwoPair
    } else if (this.contains(2)) {
        return Type.OnePair
    } else if (this.size == 5) {
        return Type.HighCard
    } else {
        throw Error("Could not find type  for hand_ $this")
    }
}

val cardRatingWithJoker = listOf(
    'A',
    'K',
    'Q',
    'T',
    '9',
    '8',
    '7',
    '6',
    '5',
    '4',
    '3',
    '2',
    'J',
)

fun String.toTypeWithJoker(): Type =
    this
        .also { print("$this: ") }
        .map { c -> Pair(c, this.count { c == it }) }
        .let { cards ->
            val numberOfJs = cards.find { it.first == 'J' }?.second
            when (numberOfJs) {
                5 -> {
                    cards
                }
                null -> {
                    cards
                }
                else -> {
                    val maxOccurences = cards.filter { it.first != 'J' }.maxOf { it.second }
                    var didUseJs = false
                    cards
                        .sortedWith { h1, h2 ->
                            val c1Value = cardRatingWithJoker.indexOf(h1.first)
                            val c2Value = cardRatingWithJoker.indexOf(h2.first)
                            c1Value - c2Value
                        }
                        .mapNotNull {
                            if (it.second == maxOccurences && it.first != 'J' && !didUseJs) {
                                didUseJs = true
                                Pair(it.first, it.second + numberOfJs)
                            } else if (it.first == 'J') {
                                null
                            } else {
                                it
                            }
                        }
                }
            }
        }
        .distinctBy { it.first }
        .also { print(it) }
        .map { it.second }
        .toType()
        .also { println(" -> $it") }
