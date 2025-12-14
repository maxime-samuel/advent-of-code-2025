import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/resources/$name").readText().trim().lines()

/**
 * Converts string to .md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun <T> simpleAssert(expected: T, actual: T) {
    val ANSI_RESET = "\u001B[0m"
    val ANSI_RED = "\u001B[31m"
    val ANSI_GREEN = "\u001B[32m"
    val ANSI_BLUE = "\u001B[34m"

    val result = expected == actual

    if (result) {
        println("${ANSI_GREEN}Ok${ANSI_RESET}   -------> exp:$expected == act:$actual")
    } else {
        println("${ANSI_RED}NOT OK!!${ANSI_RESET} ---> exp:$expected != act:$actual")
    }
}

const val debugOn = true
fun logDebug(message: String) {
    if (debugOn) { println(message) }
}