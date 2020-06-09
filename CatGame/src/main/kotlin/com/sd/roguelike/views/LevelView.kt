package com.sd.roguelike.views

import com.sd.roguelike.controller.MainController
import com.sd.roguelike.data.GameObject
import com.sd.roguelike.data.Point
import javafx.beans.property.SimpleStringProperty
import javafx.scene.input.KeyEvent
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import tornadofx.*
import java.awt.Font


class LevelView : View("Level") {
    private val controller: MainController by inject()
    private val wSize = controller.getContext().width
    private val hSize = controller.getContext().height
    private lateinit var gameRoot: GridPane


    private val render = LevelRender()
    private val fieldProperty = SimpleStringProperty(this, "field", render.drawContext(wSize, hSize, mapOf()))
    private var field by fieldProperty

    init {
        controller.runGame()
        update(controller.getContext().getMap())
    }

    override val root = vbox {
        useMaxSize = true
        primaryStage.height = hSize * 21.0
        primaryStage.width = wSize * 10.0
        style {
            backgroundColor += Color.BLACK
        }
        keyboard {
            addEventHandler(KeyEvent.KEY_PRESSED) {
                controller.addToActionQueue(it.code.name)
            }
        }
        label {
            bind(fieldProperty)
            style {
                textFill = Color.WHITE
                padding = box(10.px)
                font = javafx.scene.text.Font.font(Font.MONOSPACED)
            }
        }
    }

    fun update(ctx: Map<Point, List<GameObject>>) {
        field = render.drawContext(wSize, hSize, ctx)
    }
}