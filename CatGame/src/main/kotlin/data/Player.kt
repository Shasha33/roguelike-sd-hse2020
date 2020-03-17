package data

class Player : Unit {
    private var cnt = 0

    fun addClew() {
        cnt++
    }
}

class DoorUp : Furniture
class DoorDown : Furniture
class Wall : Furniture

interface GameObject

interface Unit : GameObject
interface Pickable : GameObject
interface Furniture : GameObject

class Clew : Pickable