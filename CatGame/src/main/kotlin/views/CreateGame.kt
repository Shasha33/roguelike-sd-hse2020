package views

import controller.MainController
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.Priority
import javafx.scene.paint.Color

import tornadofx.*
import java.io.File

class CreateGame : View("Create game") {

    val controller: MainController by inject()
    val input = SimpleStringProperty()

    override val root = vbox {
        button("Random") {
            hgrow = Priority.ALWAYS
            useMaxSize = true
            action {
                controller.startGame()
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
                    controller.startGame(input.value)
                    replaceWith<LevelView>()
                }
            }
        }
    }
}
