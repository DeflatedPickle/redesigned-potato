package com.deflatedpickle.radialmenu

import com.thoughtworks.xstream.XStream
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.*
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.UIManager


fun main(args: Array<String>) {
    // Load the XML
    val xStream = XStream()
    xStream.processAnnotations(Radial::class.java)
    xStream.processAnnotations(Button::class.java)
    val resultXML = xStream.fromXML(ClassLoader.getSystemResource("radial.xml")) as Radial
    
    // Frame
    val frame = JFrame("RadialMenu")

    val panel = JPanel(true)
    panel.layout = null
    panel.background = Color(0, 0, 0, 0)
    frame.add(panel)

    frame.type = Window.Type.UTILITY
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    frame.isUndecorated = true
    frame.isAlwaysOnTop = true
    frame.background = Color(0, 0, 0, 0)

    frame.extendedState = JFrame.MAXIMIZED_BOTH

    frame.pack()
    frame.isVisible = true

    frame.addKeyListener(object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
            if (e.keyChar.toLowerCase() == 'e') {
                panel.removeAll()

                val mouseInfo = MouseInfo.getPointerInfo().location

                val width = 1 * Toolkit.getDefaultToolkit().screenResolution
                val height = 1 * Toolkit.getDefaultToolkit().screenResolution / 2

                val radius = 10 * Toolkit.getDefaultToolkit().screenResolution / 10

                for ((index, i) in resultXML.buttonList.withIndex()) {
                    panel.add(JButton(i.text).apply {
                        background = Color(0, 0, 0, 0)

                        val t = 2 * Math.PI * index / resultXML.buttonList.size
                        val x = Math.round(mouseInfo.x + radius * Math.cos(t)).toInt()
                        val y = Math.round(mouseInfo.y + radius * Math.sin(t)).toInt()

                        setBounds(x - (width / 2), y - (height / 2), width, height)
                    })
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