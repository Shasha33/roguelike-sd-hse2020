import javafx.stage.Stage
import tornadofx.*
import tornadofx.View
import views.LevelView


class MyApp: App(LevelView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.width = 600.0
        stage.height = 600.0
    }
}


fun main(args: Array<String>) {
    launch<MyApp>(args)
//    print("asd")
}
