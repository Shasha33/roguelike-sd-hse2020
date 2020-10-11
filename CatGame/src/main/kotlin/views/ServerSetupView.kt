package views

import controller.MainController
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.Priority

import tornadofx.*

class ServerSetupView : View("Server setup") {

    val controller: MainController by inject()
    val portProp = SimpleStringProperty("6969")

    override val root = hbox {
        form {
            hgrow = Priority.ALWAYS
            useMaxSize = true
            fieldset {
                field("Port") {
                    textfield(portProp)
                }
            }
            button("Start server") {
                action {
                    enableWhen { portProp.isNotEmpty.and(portProp.value.isInt()) }
                    controller.runAsServer(portProp.value.toInt())
                    replaceWith<ServerLogView>()
                }
            }
        }
    }
}
