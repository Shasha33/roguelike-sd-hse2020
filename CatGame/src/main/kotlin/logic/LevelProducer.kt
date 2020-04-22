package logic

import data.*
import java.lang.Integer.min
import kotlin.random.Random

class LevelProducer {
    private val levelHeight = 30
    private val levelWidth = 100
    private val enemiesCount = 4
    private val clewCount = 20

    private lateinit var random: Random
    private var field = Array(levelHeight) {Array(levelWidth) {0} }

    fun create(seed: Int): Context {
        field = Array(levelHeight) {Array(levelWidth) {0} }
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
        val context = Context(height = levelHeight, width = levelWidth)
        for (i in (0 until levelHeight)) {
            for (j in (0 until levelWidth)) {
                if (field[i][j] == 0) {
                    context.addObject(Wall(), Point(j, i))
                }
            }
        }

        createDoors(context)
        createPlayer(context)
        createClews(context)
        createEnemies(context)
        return context
    }

    private fun createEnemies(context: Context) {
        val emptyPoints = getEmptyPoints().toMutableList()
        repeat(enemiesCount) {
            val randomIndex = random.nextInt(emptyPoints.size)
            val strategy =
                when(random.nextInt(3)) {
                    0 -> PassiveStrategy()
                    1 -> PassiveAggressiveStrategy()
                    else -> AggressiveStrategy()
                }
            context.addObject(Enemy(strategy), emptyPoints[randomIndex].flip())
            field[emptyPoints[randomIndex].x][emptyPoints[randomIndex].y] = 0
            emptyPoints.removeAt(randomIndex)
        }
    }

    private fun getEmptyPoints(): List<Point> {
        val emptyPoints = mutableListOf<Point>()
        for (i in (1 until levelHeight)) {
            for (j in (1 until levelWidth)) {
                if (field[i][j] == 1) {
                    emptyPoints.add(Point(i, j))
                }
            }
        }
        return emptyPoints
    }

    private fun createDoors(context: Context) {
        val emptyPoints = mutableListOf<Point>()
        for (i in (1 until levelHeight)) {
            for (j in (1 until levelWidth)) {
                if (field[i][j] == 1 && validDoorPoint(i, j)) {
                    emptyPoints.add(Point(i, j))
                }
            }
        }
        val randomUpIndex = random.nextInt(emptyPoints.size)
        context.addObject(DoorUp(), emptyPoints[randomUpIndex].flip())
        field[emptyPoints[randomUpIndex].x][emptyPoints[randomUpIndex].y] = 0
        emptyPoints.removeAt(randomUpIndex)

        val randomDownIndex = random.nextInt(emptyPoints.size)
        context.addObject(DoorDown(), emptyPoints[randomDownIndex].flip())
        field[emptyPoints[randomDownIndex].x][emptyPoints[randomDownIndex].y] = 0
    }

    private fun createPlayer(context: Context) {
        val emptyPoints = getEmptyPoints()
        val randomUpIndex = random.nextInt(emptyPoints.size)
        val point = emptyPoints[randomUpIndex]
        context.addObject(Player(), point.flip())
        field[point.x][point.y] = 0
    }

    private fun createClews(context: Context) {
        val emptyPoints = getEmptyPoints().toMutableList()
        val randomCount = random.nextInt(clewCount)
        repeat(randomCount) {
            val randomIndex = random.nextInt(emptyPoints.size)
            context.addObject(Clew(), emptyPoints[randomIndex].flip())
            field[emptyPoints[randomIndex].x][emptyPoints[randomIndex].y] = 0
            emptyPoints.removeAt(randomIndex)
        }
    }

    private fun validDoorPoint(x: Int, y: Int): Boolean {
        if (field[x + 1][y] + field[x - 1][y] == 2 && field[x][y + 1] + field[x][y - 1] == 0) {
            return false
        }
        if (field[x + 1][y] + field[x - 1][y] == 0 && field[x][y + 1] + field[x][y - 1] == 2) {
            return false
        }
        return true
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

fun Point.flip(): Point {
    return Point(this.y, this.x)
}

//for testing
fun main(args: Array<String>) {
    val levelProducer = LevelProducer()
    levelProducer.create(Random.nextInt(0, 100))
}