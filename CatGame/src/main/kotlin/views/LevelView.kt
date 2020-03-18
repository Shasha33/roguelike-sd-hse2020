package views

import controller.MainController
import data.*
import javafx.collections.FXCollections.observableArrayList
import javafx.scene.layout.Background
import javafx.scene.paint.Color
import javafx.scene.text.TextFlow
import tornadofx.*


class LevelView : View() {

    val controller: MainController by inject()

    override val root = borderpane{
        style {
            backgroundColor += Color.BLACK
        }
    }

    fun drawContext(ctx: Map<Point, List<GameObject>>) {
        with(root) {
            center {
                gridpane {
                    for ((point, list) in ctx.entries) {
                        list.firstOrNull()?.let {
                            val sprite = when(it) {
                                is Wall -> "█"
                                is Player -> "\uD83D\uDC31"
                                is DoorUp -> "\uD83D\uDEAA"
                                is DoorDown -> "\uD83D\uDEAA"
                                else -> ""
                            }
                            text(sprite)
                        }
                    }
                }
            }
        }
    }


}