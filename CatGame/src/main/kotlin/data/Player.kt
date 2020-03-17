package data

class Player : Unit {
    var cnt = 0
}

class DoorUp : Furniture
class DoorDown : Furniture
class Wall : Furniture

interface GameObject

interface Unit : GameObject
interface Pickable : GameObject
interface Furniture : GameObject

class Clew : Pickable