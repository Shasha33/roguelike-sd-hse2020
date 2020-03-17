package logic

import data.Context
import data.Point
import data.Wall
import java.lang.Integer.min
import kotlin.random.Random

class LevelProducer {
    private val levelHeight = 100
    private val levelWidth = 100
    private lateinit var random: Random
    private val field = Array(levelHeight) {Array(levelWidth) {0} }

    fun create(seed: Int): Context {
        random = Random(seed)

        val roomsNumber = random.nextInt(2, 10)
        var firstPoint = createRoom()
        for (i in (0..roomsNumber)) {
            val nextPoint = createRoom()
            connectPoints(firstPoint, nextPoint)
            firstPoint = nextPoint
        }

//        printField()
        return createContext()
    }

    private fun printField() {
        for (i in (0 until levelHeight)) {
            for (j in (0 until levelWidth)) {
                if (field[i][j] == 0) {
                    print("*")
                } else {
                    print(" ")
                }
            }
            println()
        }
    }

    private fun connectPoints(first: Point, second: Point) {
        val flag = random.nextBoolean()
        if (flag) {
            for (i in (first.x..second.x)) {
                field[i][first.y] = 1
            }
            for (i in (second.x..first.x)) {
                field[i][second.y] = 1
            }
            for (i in (first.y..second.y)) {
                field[second.x][i] = 1
            }
            for (i in (second.y..first.y)) {
                field[first.x][i] = 1
            }
        } else {
            for (i in (first.y..second.y)) {
                field[first.x][i] = 1
            }
            for (i in (second.y..first.y)) {
                field[second.x][i] = 1
            }
            for (i in (first.x..second.x)) {
                field[i][second.y] = 1
            }
            for (i in (second.x..first.x)) {
                field[i][first.y] = 1
            }
        }
    }

    private fun createContext(): Context {
        val context = Context()
        for (i in (0 until levelHeight)) {
            for (j in (0 until levelWidth)) {
                if (field[i][j] == 0) {
                    context.addObject(Wall(), Point(i, j))
                }
            }
        }
        createDoors(context)
        return context
    }

    private fun createDoors(context: Context) {

    }

    private fun createRoom(): Point {
        val walls = mutableListOf<Point>()
        for (i in (1 until levelHeight)) {
            for (j in (1 until levelWidth)) {
                if (field[i][j] == 0) {
                    walls.add(Point(i, j))
                }
            }
        }
        val index = random.nextInt(walls.size)
        val startPoint = walls[index]


        val maxHeight = min(levelHeight/2, levelHeight - startPoint.x - 1)
        val maxWidth = min(levelWidth/2, levelWidth - startPoint.y - 1)

        if (maxHeight <= 1 || maxWidth <= 1) {
            return createRoom()
        }

        val height = random.nextInt(1, maxHeight)
        val width = random.nextInt(1, maxWidth)

        val randomX = random.nextInt(startPoint.x, startPoint.x + height)
        val randomY = random.nextInt(startPoint.y, startPoint.y + width)

        for (i in (0 until height)) {
            for (j in (0 until width)) {
                field[startPoint.x +  i][startPoint.y + j] = 1
            }
        }

        return Point(randomX, randomY)
    }
}

//for testing
fun main(args: Array<String>) {
    val levelProducer = LevelProducer()
    levelProducer.create(Random.nextInt(0, 100))
}