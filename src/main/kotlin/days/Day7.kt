package days

import utils.readRows

data class Hand(
    val type: Type,
    val cards: String,
    val bid: Int,
)
enum class Type { FiveOfAKind, FourOfAKind, FullHouse, ThreeOfAKind, TwoPair, OnePair, HighCard, }

class Day7 : Day {

    private val cardRatingPart1 = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    override fun part1(): String {
        return readRows("Day7")
            .map { row -> row.toHand() }
            .countTotalWinnings(cardRatingPart1)
            .toString()
    }

    private val cardRatingPart2 = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
    override fun part2(): String {
        return readRows("Day7")
            .map { row -> row.toHandWithJoker() }
            .countTotalWinnings(cardRatingPart2)
            .toString()
    }
}

private fun String.toHand() = this
    .split(" ")
    .let {
        Hand(
            type = it.first().toType(),
            cards = it.first(),
            bid = it.last().toInt(),
        )
    }

fun handComparator(cardRating: List<Char>) = Comparator<Hand> { h1, h2 ->
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

private fun List<Hand>.countTotalWinnings(cardRating: List<Char>) = this
    .sortedWith { h1, h2 -> Type.values().indexOf(h1.type) - Type.values().indexOf(h2.type) }
    .groupBy { it.type }
    .flatMap { (_, hands) -> hands.sortedWith(handComparator(cardRating)) }
    .reversed()
    .mapIndexed { index, hand -> (index + 1) * hand.bid }
    .sum()

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
    } else if (this.isEmpty()) {
        return Type.FiveOfAKind
    } else if (this.contains(1)) {
        return Type.HighCard
    } else {
        throw Error("Can not find type for $this")
    }
}

private fun String.toHandWithJoker() = this.toHand().let { it.copy(type = it.cards.toTypeWithJoker()) }
fun String.toTypeWithJoker(): Type =
    this
        .map { c -> Pair(c, this.count { c == it }) }
        .distinctBy { it.first }
        .filter { it.first != 'J' }
        .map { it.second }
        .toType()
        .let { type ->
            val numberOfJokers = this.filter { it == 'J' }.length
            when (type to numberOfJokers) {
                Type.FourOfAKind to 1 -> Type.FiveOfAKind
                Type.FullHouse to 1 -> Type.FourOfAKind
                Type.FullHouse to 2 -> Type.FiveOfAKind
                Type.ThreeOfAKind to 1 -> Type.FourOfAKind
                Type.ThreeOfAKind to 2 -> Type.FiveOfAKind
                Type.TwoPair to 1 -> Type.FullHouse
                Type.TwoPair to 2 -> Type.FourOfAKind
                Type.TwoPair to 3 -> Type.FiveOfAKind
                Type.OnePair to 1 -> Type.ThreeOfAKind
                Type.OnePair to 2 -> Type.FourOfAKind
                Type.OnePair to 3 -> Type.FiveOfAKind
                Type.HighCard to 1 -> Type.OnePair
                Type.HighCard to 2 -> Type.ThreeOfAKind
                Type.HighCard to 3 -> Type.FourOfAKind
                Type.HighCard to 4 -> Type.FiveOfAKind
                else -> type
            }
        }
