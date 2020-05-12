package views

import controller.MainController
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.Priority
import javafx.scene.paint.Color

import tornadofx.*
import java.io.File

class ServerLogView : View("Server log") {

    val controller: MainController by inject()

    val textRoot = textflow {  }
    override val root = hbox {
        add(textRoot)
    }
}
