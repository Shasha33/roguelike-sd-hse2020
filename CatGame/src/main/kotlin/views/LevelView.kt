package views

import controller.MainController
import data.GameObject
import data.Point
import javafx.beans.property.SimpleStringProperty
import javafx.scene.input.KeyEvent
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import tornadofx.*
import java.awt.Font


class LevelView : View() {
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
        style {
            backgroundColor += Color.BLACK
        }
        keyboard {
            addEventHandler(KeyEvent.KEY_PRESSED) {
                controller.addToActionQueue(it.code.name)
//                println(it.code)
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

//class GameFieldModel(field: String) : ItemViewModel<String>(field) {
//    val field = bind(String)
//}