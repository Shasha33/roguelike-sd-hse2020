package views

import controller.MainController
import javafx.collections.FXCollections.observableArrayList
import javafx.scene.text.TextFlow
import tornadofx.*
import java.awt.TextField

class LevelView : View() {

    val controller: MainController by inject()

    override val root = vbox {
        useMaxSize = true
        textflow {
            text("lol")
        }
    }
}