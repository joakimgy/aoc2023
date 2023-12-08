package utils

fun lcm(first: Long, second: Long): Long {
    return first * second / gcd(first, second)
}

fun gcd(first: Long, second: Long): Long {
    return if (second == 0L) first else gcd(second, first % second)
}
