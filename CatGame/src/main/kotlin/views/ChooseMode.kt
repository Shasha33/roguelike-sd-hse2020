package views

import controller.MainController
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.Priority
import javafx.scene.paint.Color

import tornadofx.*
import java.io.File

class ChooseMode : View("Choose mode") {

    val controller: MainController by inject()

    override val root = vbox {

        button("Singleplayer") {
            vgrow = Priority.ALWAYS
            useMaxSize = true
            action {
                replaceWith<CreateGame>()
            }
        }
        button("Multiplayer") {
            vgrow = Priority.ALWAYS
            useMaxSize = true
            action {
                replaceWith<ConnectToServerView>()
            }
        }
        button("Server") {
            vgrow = Priority.ALWAYS
            useMaxSize = true
            action {
                replaceWith<ServerSetupView>()
            }
        }
    }
}
