package views

import javafx.collections.FXCollections.observableArrayList
import tornadofx.*

class LevelView : View() {
    private val data = listOf(listOf("1", "2", "3"), listOf("1", "2", "3"), listOf("1", "2", "3"))
//    override val root = tableview(data) {  }

//    private val defaultStyle = Gr
    override val root = gridpane {
        for (r in data) {
            row {

                for (c in r) {
                    text(c) {
                        useMaxSize = true
                    }
                }
            }
        }
        for (i in data.indices) {
            constraintsForRow(i).percentHeight = 100.0 / data.size
        }
        for (i in data[0].indices) {
            constraintsForColumn(i).percentWidth = 100.0 / data[0].size
        }
    }
}