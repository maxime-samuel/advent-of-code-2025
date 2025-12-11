data class RangeId(val start: Long, val end: Long)

fun convertEntryToRangeIds(entry: String): List<RangeId> {
    val rangeIds = entry.split(",")
        .map {
            val ranges = it.split("-")
            val start = ranges[0].toLong()
            val end = ranges[1].toLong()
            RangeId(start, end)
        }

    return rangeIds
}

fun main() {

    fun findInvalidIDFromOneRangeIds(rangeId: RangeId): List<Long> {
        // 11-22, two invalid ids
        // 222220-222224 has one invalid ID, 222222.

        val invalidIDs = mutableListOf<Long>()

        for (i in rangeId.start..rangeId.end) {
            val nbr = i.toString()
            logDebug("****")

            // If the number can be split in two equal parts
            logDebug("id $nbr, pair? ${(nbr.length % 2 == 0)}")

            if (nbr.isNotEmpty() && nbr.length % 2 == 0) {
                val halfEnd = nbr.length / 2
                val firstPart = nbr.substring(0, halfEnd)
                val secondPart = nbr.substring(halfEnd, nbr.length)

                logDebug("$firstPart == $secondPart ? ${firstPart == secondPart}")

                if (firstPart == secondPart) {
                    invalidIDs.add(i)
                }

            }
        }

        return invalidIDs.toList()
    }

    fun findAllInvalidIDs(rangeIds: List<RangeId>): Long {
        val invalidIDs = mutableListOf<Long>()

        rangeIds.forEach {
            val invalidIdsOneRange = findInvalidIDFromOneRangeIds(it)
            logDebug("# invalidIdsOneRange $invalidIdsOneRange")
            invalidIDs.addAll(invalidIdsOneRange)
        }

        return invalidIDs.fold(0) { acc, next -> acc + next }
    }




    fun part1(input: List<String>): Long {
        logDebug("***************************")
        val rangeIds = input.flatMap { line -> convertEntryToRangeIds(line) }
        logDebug("** rangeIds -> $rangeIds")

        val totalOfIds = findAllInvalidIDs(rangeIds)
        logDebug("--> totalOfIds -> $totalOfIds")

        return totalOfIds
    }

    fun part2(input: List<String>): Int {

        return input.size
    }

    simpleAssert(1227775554, part1(readInput("test_input")))
    // simpleAssert(6, part2(readInput("test_input")))

    val day02TestInput = readInput("Day02_test")
    simpleAssert(1227775554, part1(day02TestInput))

    val input = readInput("Day02")
    part1(input).println()
    //part2(input).println()
}
