package controller

import data.Context
import grpc.Client
import tornadofx.runAsync

class ClientWrapper(host: String, port: Int) {
    val client = Client(host, port)

    fun startUILoop(callback: (Context) -> Unit) {
        runAsync {
            while (true) {
                val context = client.getCurrentContext()
                callback(context)
            }
        }
    }
}