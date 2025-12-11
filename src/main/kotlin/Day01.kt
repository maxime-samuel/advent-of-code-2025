import kotlin.math.abs

data class Rotation(val startPosition: Int, val direction: Int, val nextPosition: Int)

fun countingZeros(rotation: Rotation): Int {
    // Init
    var nbrZeros = 0
    logDebug("startPosition ${rotation.startPosition} direction ${rotation.direction} nextPosition ${rotation.nextPosition}")

    // Initial zero
    nbrZeros = if (rotation.startPosition != 0 && rotation.nextPosition == 0) { 1 } else { 0 }
    logDebug("init nbrZeros $nbrZeros")

    if (rotation.direction > 0) {
        // right (positive)
        val totalRotation = rotation.startPosition + rotation.direction - (nbrZeros * 100)
        logDebug("right -> totalRotation $totalRotation")
        nbrZeros += totalRotation / 100

        if (rotation.nextPosition >= 100) {
            nbrZeros++
        }
        logDebug("right -> nbrZeros $nbrZeros")
    } else if (rotation.direction < 0) {
        // left (negative)
        val totalRotation = rotation.startPosition + rotation.direction // - (nbrZeros * 100)
        logDebug("left -> totalRotation $totalRotation")

        nbrZeros += if (totalRotation == 0) {
            (abs(totalRotation)) / 100
        } else {
            (abs(totalRotation) - (nbrZeros * 100)) / 100
        }

        if (totalRotation < 0 && rotation.startPosition != 0) {
            nbrZeros++
        }

        logDebug("left  -> nbrZeros $nbrZeros")
    } else {
        // neither
        if (rotation.nextPosition == 0) {
            nbrZeros++
        }
        logDebug("none  -> nbrZeros $nbrZeros")
    }

    return nbrZeros
}

fun main() {
/*
    The safe has a dial with only an arrow on it; around the dial are the numbers 0 through 99 in order.
    As you turn the dial, it makes a small click noise as it reaches each number.

    The attached document (your puzzle input) contains a sequence of rotations, one per line, which tell you how to open the safe.
    A rotation starts with an L or R which indicates whether the rotation should be to the left (toward lower numbers) or to the right
    (toward higher numbers). Then, the rotation has a distance value which indicates how many clicks the dial should be rotated in that direction.

    // The dial starts by pointing at 50.
    // Lx -> Turn left (lower number) by x
    // Ry -> Turn right (higher number) by y
    // The actual password is the number of times the dial is left pointing at 0 after any rotation in the sequence.
 */

    fun convertEntryToInt(entry: String): Int {
        val direction = entry[0]
        val nbr = entry.drop(1).toInt()

        val nbrAsInt = (if (direction == 'L') { -nbr } else { nbr })
        logDebug("$direction$nbr -> $nbrAsInt")


        return nbrAsInt
    }

    fun convertEntryNormalizedToInt(entry: String): Int {
        return convertEntryToInt(entry) % 100
    }

    fun rotate(startPosition: Int, direction: Int): Rotation {

        logDebug("startPosition $startPosition direction $direction")

        val diff = startPosition + (direction % 100)

        val nextPosition = if (diff < 0) {
            diff + 100
        } else if (diff >= 100) {
            diff - 100
        } else {
            diff
        }

        logDebug("  simpleDiff $diff, nextPosition $nextPosition")

        return Rotation(startPosition, direction, nextPosition)
    }


    fun nextPosition(currentPosition: Int, direction: Int): Int {

        logDebug("currentPos $currentPosition direction $direction")

        val diff = currentPosition + (direction % 100)

        val diffEdge = if (diff < 0) {
            diff + 100
        } else if (diff >= 100) {
            diff - 100
        } else {
            diff
        }

        logDebug("  diff $diff resulting in diffEdge $diffEdge")

        return diffEdge
    }

    fun part1(input: List<String>): Int {
        val startPosition = 50
        var password = 0

        val directions = input.map { line -> convertEntryNormalizedToInt(line) }

        directions.fold(startPosition) { acc, next ->
            run {
                val nextPos = nextPosition(acc, next)
                if (nextPos == 0) {
                    password++
                }
                nextPos
            }
        }

        logDebug("password $password")

        return password
    }

    fun part2(input: List<String>): Int {
        val startPosition = 50
        var password = 0

        val directions = input.map { line -> convertEntryToInt(line) }

        directions.fold(startPosition) { acc, next ->
            run {
                val rotation = rotate(acc, next)
                password += countingZeros(rotation)
                logDebug("    --> current password value $password")
                rotation.nextPosition
            }
        }

        logDebug("password $password")

        return password
    }

    // Test if implementation meets criteria from the description, like:
    // .simpleAssert(3, part1(.readInput("test_input")))
    simpleAssert(6, part2(readInput("test_input")))

    // Or read a large test input from the `src/Day01_test.txt` file:
    // val day01TestInput = .readInput("Day01_test")
    // simpleAssert(1, part1(day01TestInput))

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    //part1(input).println()
    part2(input).println()
}
