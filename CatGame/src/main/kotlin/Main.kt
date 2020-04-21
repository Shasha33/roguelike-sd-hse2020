import controller.MainController
import javafx.stage.Stage
import tornadofx.*
import tornadofx.View
import views.LevelView
import views.MenuView


class MyApp: App(MenuView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
//        stage.width = 500.0
//        stage.height = 500.0
    }
    private val controller: MainController by inject()

    override fun stop() {
        controller.stopGame()
        super.stop()
    }
}


fun main(args: Array<String>) {
    launch<MyApp>(args)
}
