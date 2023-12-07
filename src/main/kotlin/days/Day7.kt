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

    override fun part2(): String = ""
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

fun String.toType(): Type {
    val cardNoOfOccurences = this
        .map { c -> Pair(c, this.count { c == it }) }
        .distinctBy { it.first }
        .map { it.second }
    if (cardNoOfOccurences.contains(5)) {
        return Type.FiveOfAKind
    } else if (cardNoOfOccurences.contains(4)) {
        return Type.FourOfAKind
    } else if (cardNoOfOccurences.contains(3) && cardNoOfOccurences.contains(2)) {
        return Type.FullHouse
    } else if (cardNoOfOccurences.contains(3)) {
        return Type.ThreeOfAKind
    } else if (cardNoOfOccurences.count { it == 2 } == 2) {
        return Type.TwoPair
    } else if (cardNoOfOccurences.contains(2)) {
        return Type.OnePair
    } else if (cardNoOfOccurences.size == 5) {
        return Type.HighCard
    } else {
        throw Error("Could not find type  for hand_ $this")
    }
}
