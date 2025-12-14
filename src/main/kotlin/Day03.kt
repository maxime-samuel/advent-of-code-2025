/*
 * There are batteries nearby that can supply emergency power to the escalator for just such an occasion.
 * The batteries are each labeled with their joltage rating, a value from 1 to 9. You make a note of their
 * joltage ratings (your puzzle input). For example:
 *
 * 987654321111111
 * 811111111111119
 * 234234234234278
 * 818181911112111
 *
 * The batteries are arranged into banks; each line of digits in your input corresponds to a single bank of batteries.
 * Within each bank, you need to turn on exactly two batteries; the joltage that the bank produces is equal to the
 * number formed by the digits on the batteries you've turned on. For example, if you have a bank like 12345 and you
 * turn on batteries 2 and 4, the bank would produce 24 jolts. (You cannot rearrange batteries.)
 */

fun turnOn(batteriesTurnedOn: List<Battery>): Long {
    return batteriesTurnedOn.map { it.joltage.toString() }.fold("") { acc, b -> acc + b }.toLong()
}

data class Subset(var start: Int, var end: Int)

class Bank(val batteries: List<Battery>) {
    fun largestJoltage(numberOfBatteriesToTurnOn: Int): List<Battery> {
        // Try to compute batteries on a bank per joltage/index to easily parse them after
        // val batteryScore: Map<Int, List<Battery>> = emptyMap()
        val largestJoltageBatterySequence = mutableListOf<Battery>()
        val indexSubset = Subset(0, batteries.size - numberOfBatteriesToTurnOn + 1)

        for (i in 1 until numberOfBatteriesToTurnOn+1) {
            logDebug("*******************************")
            logDebug("i $i indexSubset $indexSubset batteries ${batteries.size}")
            val next = findNextLargestJoltage(batteries.subList(indexSubset.start, indexSubset.end))
            logDebug("found largest $next")
            largestJoltageBatterySequence.add(next)
            indexSubset.start = next.index + 1
            indexSubset.end++
            logDebug("next indexSubset $indexSubset")
        }

        return largestJoltageBatterySequence
    }

    private fun findNextLargestJoltage(batteriesSubset: List<Battery>): Battery {
        var batteryWithLargestJoltage = batteriesSubset[0]

        for (battery in batteriesSubset) {
            logDebug("comparing battery ${battery.joltage} with current largest ${batteryWithLargestJoltage.joltage}")
            if (battery.joltage > batteryWithLargestJoltage.joltage) {
                batteryWithLargestJoltage = battery
            }
        }

        logDebug("Found battery with largest joltage $batteryWithLargestJoltage")
        return batteryWithLargestJoltage
    }
}

data class Battery(val index: Int, val joltage: Int)


fun convertEntryToBank(entry: String): Bank {
    // 987654321111111
    logDebug(entry)
    val batteries = entry
        .mapIndexed { index, joltage -> Battery(index, joltage.toString().toInt()) }

    return Bank(batteries)
}

/*
You'll need to find the largest possible joltage each bank can produce. In the above example:

    In 987654321111111, you can make the largest joltage possible, 98, by turning on the first two batteries.
    In 811111111111119, you can make the largest joltage possible by turning on the batteries labeled 8 and 9, producing 89 jolts.
    In 234234234234278, you can make 78 by turning on the last two batteries (marked 7 and 8).
    In 818181911112111, the largest joltage you can produce is 92.

 */

fun main() {
    fun part1(input: List<String>): Long {
        logDebug("***************************")
        val banks = input.map { line -> convertEntryToBank(line) }

        val lll = banks.map { it.largestJoltage(2) }

        logDebug("batteries, largest joltage $lll")

        val results = lll.map { turnOn(it) }

        logDebug("results $results")



        return results.sum()
    }

    fun findLarger(bank: Bank) {

    }

    fun part2(input: List<String>): Long {
        logDebug("***************************")
        val banks = input.map { line -> convertEntryToBank(line) }

        /*
            987654321111111
            811111111111119
            234234234234278
            818181911112111

            Now, the joltages are much larger:

                In 987654321111111, the largest joltage can be found by turning on everything except some 1s at the end to produce 987654321111.
                In the digit sequence 811111111111119, the largest joltage can be found by turning on everything except some 1s, producing 811111111119.
                In 234234234234278, the largest joltage can be found by turning on everything except a 2 battery, a 3 battery, and another 2 battery near the start to produce 434234234278.
                In 818181911112111, the joltage 888911112111 is produced by turning on everything except some 1s near the front.

            The total output joltage is now much larger: 987654321111 + 811111111119 + 434234234278 + 888911112111 = 3121910778619.
         */

        val lll = banks.map { it.largestJoltage(12) }

        logDebug("batteries, largest joltage $lll")

        val results = lll.map { turnOn(it) }

        logDebug("results $results")

        return results.sum()
    }

    // simpleAssert(357, part1(readInput("test_input")))
    simpleAssert(3121910778619, part2(readInput("test_input")))

    val input = readInput("Day03")
    // part1(input).println()
    part2(input).println()
}
