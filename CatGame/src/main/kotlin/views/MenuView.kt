package views

import controller.MainController
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.io.File

class MenuView : View("My View") {

    val controller: MainController by inject()
    val input = SimpleStringProperty()

    override val root = borderpane {
        top {
            button("Random") {
                useMaxSize = true
                action {
                    replaceWith<LevelView>()
                }
            }
        }
        bottom {
           form {
               fieldset {
                   field("Path") {
                       textfield(input)
                   }
               }
               button("Load from file") {
                   useMaxSize = true
                   action {
                       controller.usePath = File(input.value)
                       replaceWith<LevelView>()
                   }
               }
           }
        }
    }
}
