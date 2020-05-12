package views

import controller.MainController
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.Priority
import javafx.scene.paint.Color

import tornadofx.*
import java.io.File

class ServerSetupView : View("Server setup") {

    val controller: MainController by inject()
    val input = SimpleStringProperty("6969")

    override val root = hbox {
        form {
            hgrow = Priority.ALWAYS
            useMaxSize = true
            fieldset {
                field("Port") {
                    textfield(input)
                }
            }
            button("Start server") {
                action {
//                    controller.startServer(input.value)
                    replaceWith<ServerLogView>()
                }
            }
        }
    }
}
