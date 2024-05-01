package Colors

import androidx.compose.ui.graphics.Color

//https://www.color-hex.com/color-palettes/

val PRIMARY_ORANGE = Triple(255,78,80)
val PRIMARY_ORANGE_LIGHT = Triple(252,145,58)

val YELLOW = Triple(249,214,46)
val YELLOW_LIGHT = Triple(234,227,116)

val MAT_DARK = Triple(90,82,85)
val MAT_WHITE = Triple(249,249,249)

fun Triple<Int, Int, Int>.asColor() = Color(this.first, this.second, this.third)