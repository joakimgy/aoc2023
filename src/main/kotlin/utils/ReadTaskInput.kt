package utils

import java.io.File

fun readRows(filename: String): List<String> {
    var list: List<String> = mutableListOf()
    File("src/main/kotlin/tasks/$filename").forEachLine {
        list = list.plus(it)
    }
    return list
}
