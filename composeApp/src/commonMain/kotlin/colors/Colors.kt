package colors

import androidx.compose.ui.graphics.Color

//https://www.color-hex.com/color-palettes/

val PRIMARY_ORANGE = Triple(255,78,80)
val PRIMARY_ORANGE_LIGHT = Triple(252,145,58)

val YELLOW = Triple(249,214,46)
val YELLOW_LIGHT = Triple(234,227,116)
val YELLOW_TINT = Triple(255,248,220)

val MAT_DARK = Triple(90,82,85)
val MAT_DARK_LIGHT = Triple(142, 142, 142 )
val MAT_WHITE = Triple(249,249,249)
val PLAIN_WHITE = Triple(255, 255, 255)
val GREEN = Triple(147,	197,	33)
val RED = Triple(	216,	63,	63)

val LIGHT_GRAY = Triple(	115, 147, 179)
val LIGHT_GRAY_TRANSPARENT = Triple(	115, 147, 179)
val LIGHT_GRAY_SHADOW = Triple(	116, 148, 180)
val EXTRA_LIGHT_BG = Triple(242, 244, 247)
val EXTRA_LIGHT_GRAY = Triple(223, 228, 235)



fun Triple<Int, Int, Int>.asColor() = Color(this.first, this.second, this.third)