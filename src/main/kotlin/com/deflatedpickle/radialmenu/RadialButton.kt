package com.deflatedpickle.radialmenu

import java.awt.Point
import javax.swing.JButton

class RadialButton(text: String) : JButton(text) {
    var parent: RadialButton? = null
    var parentPoint = Point()
    val children = mutableListOf<RadialButton>()

    var shownSub = false
}