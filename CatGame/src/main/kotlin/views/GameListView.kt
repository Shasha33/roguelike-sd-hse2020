package views

import controller.MainController
import controller.SessionInfo
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.text.Font
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.hse.kek.Roguelike
import tornadofx.*

class GameListView : View("Connect to server") {

    val controller: MainController by inject()
    val gameList = mutableListOf<SessionInfo>().asObservable()
    val newRoom = SimpleStringProperty("new game")
    val selectedGameName = SimpleObjectProperty<SessionInfo>()

    var addr = ""
    var port = 0

    override fun onDock() {

        addr = params["addr"] as String? ?: "localhost"
        port = (params["port"] as String? ?: "0").toInt()
        GlobalScope.launch {
            gameList.addAll(controller.connectToServer(addr, port))
        }

    }
    override val root = vbox {
        listview(gameList) {
            bindSelected(selectedGameName)
        }
        hbox {
            textfield(newRoom)
            button("Add") {
                enableWhen { newRoom.isNotEmpty }
                action {
                    controller.createNewSessionAndConnect(newRoom.value)
                    replaceWith<LevelView>()
                }
            }
            separator(Orientation.VERTICAL)
            button("Join") {
                enableWhen { selectedGameName.isNotNull }
                action {
                    controller.connectToExistingSession(selectedGameName.value)
                    replaceWith<LevelView>()
                }
            }
        }
        alignment = Pos.CENTER_LEFT
        paddingAll = 10.0
        spacing = 4.0
    }
}
