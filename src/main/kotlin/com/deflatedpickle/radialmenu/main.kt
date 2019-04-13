package com.deflatedpickle.radialmenu

import com.thoughtworks.xstream.XStream
import java.awt.*
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.*
import kotlin.math.pow


fun insideRadius(origin: Point, point: Point, radius: Double): Boolean {
    if ((origin.x - point.x).toDouble().pow(2)
            + (origin.y - point.y).toDouble().pow(2)
            <= radius.pow(2)) {
        return true
    }
    return false
}


fun main(args: Array<String>) {
    // Load the XML
    val xStream = XStream()
    xStream.processAnnotations(Radial::class.java)
    xStream.processAnnotations(Button::class.java)
    val resultXML = xStream.fromXML(ClassLoader.getSystemResource("radial.xml")) as Radial

    // TODO: Move these to the XML file
    val width = 1 * Toolkit.getDefaultToolkit().screenResolution
    val height = 1 * Toolkit.getDefaultToolkit().screenResolution / 2

    val radius = 10 * Toolkit.getDefaultToolkit().screenResolution / 8

    var radialOpen = false
    var centre: Point
    var selectedPoint = Point()

    // Frame
    val frame = JFrame("RadialMenu")

    val panel = object : JPanel(true) {
        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)

            if (!radialOpen) return

            val mouseInfo = MouseInfo.getPointerInfo().location

            if (resultXML.connections) {
                g.color = UIManager.getColor("Button.select")

                fun drawLines(list: List<RadialButton>) {
                    for (i in list) {
                        g.drawLine(i.parentPoint.x, i.parentPoint.y, i.x + (width / 2), i.y + (height / 2))

                        drawLines(i.children)
                    }
                }
                drawLines(resultXML.buttonList.map { it.radialButton })
            }

            if (resultXML.follower) {
                // Test if the mouse is close enough to the selected point
                if (insideRadius(selectedPoint, mouseInfo, (radius * (Toolkit.getDefaultToolkit().screenResolution / 34)).toDouble())) {
                    g.color = UIManager.getColor("Button.select")
                    g.drawLine(selectedPoint.x, selectedPoint.y, mouseInfo.x, mouseInfo.y)
                }
            }
        }
    }
    panel.layout = null
    panel.background = Color(0, 0, 0, 0)
    frame.add(panel)

    frame.type = Window.Type.UTILITY
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    frame.isUndecorated = true
    frame.isAlwaysOnTop = true
    frame.background = Color(0, 0, 0, 0)

    frame.extendedState = JFrame.MAXIMIZED_BOTH

    val timer = Timer(30, ActionListener {
        frame.repaint()
    }).start()

    frame.pack()
    frame.isVisible = true

    frame.addKeyListener(object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
            if (e.keyChar.toLowerCase() == 'e') {
                panel.removeAll()

                radialOpen = !radialOpen

                val mouseInfo = MouseInfo.getPointerInfo().location
                centre = mouseInfo

                selectedPoint = centre

                if (radialOpen) {
                    fun addButtonRing(parent: JComponent, list: List<Button>, origin: Point, radius: Float) {
                        for ((index, i) in list.withIndex()) {
                            panel.add(RadialButton(i.text).apply {
                                this.button = i
                                i.radialButton = this

                                val t = 2 * Math.PI * index / list.size
                                val x = Math.round(origin.x + radius * Math.cos(t)).toInt()
                                val y = Math.round(origin.y + radius * Math.sin(t)).toInt()

                                setBounds(x - (width / 2), y - (height / 2), width, height)

                                if(parent is RadialButton) {
                                    this.parent = parent
                                    this.parentPoint = parent.location
                                    parent.children.add(this)
                                }
                                else {
                                    this.parentPoint = centre
                                }

                                if (i.buttonList != null) {
                                    addActionListener {
                                        if (!this.shownSub) {
                                            addButtonRing(this, i.buttonList, Point(x, y), radius)

                                            this.shownSub = true
                                            selectedPoint = Point(this.location.x + (width / 2), this.location.y + (height / 2))
                                        }
                                        else {
                                            val removeList = mutableListOf<RadialButton>()

                                            fun destroyTheChildren(list: List<RadialButton>) {
                                                for (child in list) {
                                                    if (child.children.isNotEmpty()) {
                                                        destroyTheChildren(child.children)
                                                    }

                                                    removeList.add(child)
                                                    panel.remove(child)
                                                }
                                            }
                                            destroyTheChildren(this.children)

                                            for(removedButton in removeList) {
                                                if (removedButton.parent is RadialButton) {
                                                    (removedButton.parent as RadialButton).children.remove(removedButton)
                                                }
                                            }

                                            this.shownSub = false

                                            selectedPoint = if (this.parent != null) {
                                                Point(this.parent!!.location.x + (width / 2), this.parent!!.location.y + (height / 2))
                                            }
                                            else {
                                                centre
                                            }
                                        }
                                    }
                                }
                                else {
                                    addActionListener {
                                        Runtime.getRuntime().exec(i.command)
                                    }
                                }
                            })
                        }
                    }
                    addButtonRing(panel, resultXML.buttonList, mouseInfo, resultXML.buttonList.size * Toolkit.getDefaultToolkit().screenResolution / 5f)
                }

                panel.revalidate()
                frame.repaint()
            }
        }
    })

    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    // System tray
    if (SystemTray.isSupported()) {
        val systemTray = SystemTray.getSystemTray()

        val menuContext = PopupMenu().apply {
            add(MenuItem("Exit").apply {
                addActionListener { System.exit(0) }
            })
        }

        val trayIcon = TrayIcon(Toolkit.getDefaultToolkit().getImage(""), "RadialMenu", menuContext)
        trayIcon.addActionListener { }

        systemTray?.add(trayIcon)
    }
}