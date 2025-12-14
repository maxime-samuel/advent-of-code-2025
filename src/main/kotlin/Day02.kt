data class RangeId(val start: Long, val end: Long)

enum class Status {
    VALID,
    INVALID,
    UNKNOWN
}

class CompareId(val id: String, val baseValue: String, val values: List<String>, val compareLength: Int, var status: Status) {
    fun computeStatus() {
        //logDebug("computeStatus")
        var isValid = false
        for (value in values) {
            //logDebug("base $baseValue value $value")
            if (baseValue != value) {
                isValid = true
                break
            }
        }
        //logDebug("isValid $isValid")
        this.status = if (isValid) Status.VALID else Status.INVALID
    }

    override fun toString(): String {
        return buildString {
            append("[")
            append(id)
            append("]")
            append(" ")
            append("base $baseValue")
            append(" ")
            append("of $compareLength")
            append(", ")
            append("values $values")
            append(", ")
            append("status $status]  ")
        }
    }

    fun println() {
        println(this.toString())
    }
}

fun convertIdToCompareIds(id: Long): MutableList<CompareId> {
    val nbr = id.toString()
    val compareIds = mutableListOf<CompareId>()

    for (compareLength in 1 until nbr.length) {
        // logDebug("loop compareLength: $compareLength, nbr.length - compareLength ${nbr.length - compareLength}")
        val baseValue = nbr.substring(0, compareLength)
        //if (compareLength * 2 <= nbr.length) {
        if (nbr.length % compareLength == 0) {
            val values = mutableListOf<String>()
            for (j in 0 until nbr.length - compareLength step compareLength) {
                // logDebug("nbr $nbr length ${nbr.length} sub from ${j + compareLength} to ${j + compareLength + compareLength}")
                values.add(nbr.substring(j + compareLength, j + compareLength + compareLength))
            }

            if (values.isNotEmpty()) {
                val compareId = CompareId(nbr, baseValue, values, compareLength, Status.UNKNOWN)
                compareId.computeStatus()
                compareIds.add(compareId)

            }
        }
    }

    // logDebug("CompareIds $compareIds")

    return compareIds
}

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

    fun findInvalidTwiceIdsFromOneRangeId(rangeId: RangeId): List<Long> {
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

    fun findInvalidAtLeastTwiceIdsFromOneRangeId(rangeId: RangeId): List<Long> {
        // 11-22 still has two invalid IDs, 11 and 22.
        // 95-115 now has two invalid IDs, 99 and 111.
        // 998-1012 now has two invalid IDs, 999 and 1010.
        // 565653-565659 now has one invalid ID, 565656.
        // 824824821-824824827 now has one invalid ID, 824824824.

        val invalidIds = mutableListOf<Long>()

        for (id in rangeId.start..rangeId.end) {

            val compareIds = convertIdToCompareIds(id)
            compareIds.firstOrNull { it.status == Status.INVALID }?.let {
                invalidIds.add(id)
            }
        }

        if (invalidIds.isNotEmpty()) {
            logDebug("invalidIds $invalidIds")
        }

        return invalidIds.toList()
    }

    fun findAllInvalidIds(rangeIds: List<RangeId>, findInvalidIdsFromOneRangeId: (RangeId) -> List<Long>): Long {
        val invalidIDs = mutableListOf<Long>()

        rangeIds.forEach {
            val invalidIdsOneRange = findInvalidIdsFromOneRangeId(it)
            // logDebug("# invalidIdsOneRange $invalidIdsOneRange")
            invalidIDs.addAll(invalidIdsOneRange)
        }

        return invalidIDs.fold(0) { acc, next -> acc + next }
    }

    fun part1(input: List<String>): Long {
        logDebug("***************************")
        val rangeIds = input.flatMap { line -> convertEntryToRangeIds(line) }
        logDebug("** rangeIds -> $rangeIds")

        val totalOfIds = findAllInvalidIds(rangeIds, ::findInvalidTwiceIdsFromOneRangeId)
        logDebug("--> totalOfIds -> $totalOfIds")

        return totalOfIds
    }

    fun part2(input: List<String>): Long {
        logDebug("***************************")
        val rangeIds = input.flatMap { line -> convertEntryToRangeIds(line) }
        logDebug("** rangeIds -> $rangeIds")

        val totalOfIds = findAllInvalidIds(rangeIds, ::findInvalidAtLeastTwiceIdsFromOneRangeId)

        return totalOfIds
    }

    // simpleAssert(1227775554, part1(readInput("test_input")))
    simpleAssert(4174379265, part2(readInput("test_input")))

    // val day02TestInput = readInput("Day02_test")
    //simpleAssert(1227775554, part1(day02TestInput))

    val input = readInput("Day02")
    //part1(input).println()
    part2(input).println()
}
