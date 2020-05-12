package views

import controller.MainController
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.Priority

import tornadofx.*

class ConnectToServerView : View("Connect to server") {

    val controller: MainController by inject()
    val ipProp = SimpleStringProperty("localhost")
    val portProp = SimpleStringProperty("6969")
    override val root = hbox {
        form {
            hgrow = Priority.ALWAYS
            useMaxSize = true
            fieldset {
                field("IP") {
                    textfield(ipProp)
                }

                field("Port") {
                    textfield(portProp)
                }
            }
            button("Connect") {
                action {
//                    controller.connectTo(ipProp.value, portProp.value)
                    replaceWith<GameListView>()
                }
            }
        }
    }
}
