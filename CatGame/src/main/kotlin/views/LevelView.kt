package views

import controller.MainController
import data.Context
import data.GameObject
import data.Point
import javafx.collections.FXCollections.observableArrayList
import javafx.scene.text.TextFlow
import tornadofx.*
import java.awt.TextField

class LevelView : View() {

    val controller: MainController by inject()

    override val root = borderpane{}

    fun drawContext(ctx: Map<Point, List<GameObject>>) {
        with(root) {
            center {
                textflow {
                    for ((point, list) in ctx.entries) {
//                        ctx

                    }
                }
            }
        }
    }


}