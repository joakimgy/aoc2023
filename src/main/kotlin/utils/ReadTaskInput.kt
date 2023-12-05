package utils

import java.io.File

fun readRows(filename: String): List<String> {
    var list: List<String> = mutableListOf()
    readFile(filename).forEachLine {
        list = list.plus(it)
    }
    return list
}

fun readFile(filename: String) = File("src/main/resources/$filename")
