package com.deflatedpickle.radialmenu

import java.awt.Color
import java.awt.Point
import javax.swing.JButton

class RadialButton(text: String) : JButton(text) {
    var button: Button? = null

    var parent: RadialButton? = null
    var parentPoint = Point()
    val children = mutableListOf<RadialButton>()

    var shownSub = false

    init {
        background = Color(0, 0, 0, 0)
    }
}