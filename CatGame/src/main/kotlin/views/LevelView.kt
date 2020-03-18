package views

import controller.MainController
import data.*
import javafx.collections.FXCollections.observableArrayList
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Background
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.text.TextFlow
import tornadofx.*


class LevelView : View() {

    val controller: MainController by inject()
    private lateinit var gameRoot: GridPane

    override val root = vbox {
        style {
            backgroundColor += Color.BLACK
        }
        keyboard {
            addEventHandler(KeyEvent.KEY_PRESSED) {
                println(it.code)
            }
        }
        gridpane {
            gameRoot = this
        }
    }



    fun drawContext(ctx: Map<Point, List<GameObject>>) {
        with(gameRoot) {
            for ((point, list) in ctx.entries) {
                list.firstOrNull()?.let {
                    val sprite = when(it) {
                        is Wall -> "█"
                        is Player -> "\uD83D\uDC31"
                        is DoorUp -> "\uD83D\uDEAA"
                        is DoorDown -> "\uD83D\uDEAA"
                        else -> ""
                    }
                    text(sprite) {
                        gridpaneConstraints {
                            columnRowIndex(point.x,point.y)
                        }
                    }
                }
            }
        }
    }


}