package strings

import io.ktor.util.decodeBase64String
import io.ktor.util.encodeBase64

fun String.encrypt(): String {
    return this.encodeBase64()
}

fun String.decrypt(): String {
    return this.decodeBase64String()
}
