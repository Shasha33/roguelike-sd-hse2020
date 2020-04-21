package views

import controller.MainController
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.Priority
import javafx.scene.paint.Color

import tornadofx.*
import java.io.File

class MenuView : View("Main menu") {

    val controller: MainController by inject()
    val input = SimpleStringProperty()

    override val root = hbox {
        prefHeight = 210.0
        prefWidth = 510.0

        button("Random") {
            hgrow = Priority.ALWAYS
            useMaxSize = true
            action {
                replaceWith<LevelView>()
            }
        }
        form {
            hgrow = Priority.ALWAYS
            useMaxSize = true
            fieldset {
                field("Path") {
                    textfield(input)
                }
            }
            button("Load from file") {
                action {
                    controller.usePath = File(input.value)
                    replaceWith<LevelView>()
                }
            }
        }
    }
}
