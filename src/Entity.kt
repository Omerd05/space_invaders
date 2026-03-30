package src

val H_CONSTRAINT_MAX = 20
val W_CONSTRAINT_MIN = 10
val W_CONSTRAINT_MAX = 100

enum class GAME_STATUS{
    CONTINUE,
    OVER
}

abstract class Entity(var cords: MutableSet<Pair<Int, Int>>, val console: Console) {
    var movementVector = Pair(0,1)
    val id = counter++

    companion object {
        fun addPair(cord: Pair<Int, Int>, vec: Pair<Int,Int>) : Pair<Int, Int> {
            return Pair(cord.first+vec.first,cord.second+vec.second)
        }
        fun negatePair(cord: Pair<Int, Int>) : Pair<Int, Int>{
            return Pair(-cord.first,-cord.second)
        }
        fun validWidthConstraint(cord: Pair<Int, Int>): Boolean{
            return cord.second >= W_CONSTRAINT_MIN && cord.second <= W_CONSTRAINT_MAX
        }
        fun validHeightConstraint(cord: Pair<Int, Int>): Boolean{
            return cord.first <= H_CONSTRAINT_MAX
        }
        var counter = 0
    }

    protected fun RawMove(){
        val nextCords = mutableSetOf<Pair<Int, Int>>()
        for (cord in cords){
            nextCords.add(addPair(cord,this.movementVector))
        }
        this.cords.clear()
        this.cords = nextCords

    }
}

class Invader(cords: MutableSet<Pair<Int, Int>>, console: Console) : Entity(cords,console) {
    fun WrapMove() : GAME_STATUS {
        var wall_adjacent = false
        for (cord in cords){
            if (!validWidthConstraint(addPair(cord,this.movementVector))){
                wall_adjacent = true
                break
            }
        }
        if (!wall_adjacent){
            this.RawMove()
            return GAME_STATUS.CONTINUE
        }

        // Reached Player height?
        for (cord in cords){
            if (!validHeightConstraint(addPair(cord,Pair(0,1)))){
                return GAME_STATUS.OVER
            }
        }

        //No, so lower the height by 1, reverse the direction of the invaders and continue as before
        this.movementVector = addPair(negatePair(this.movementVector),Pair(1,0))
        this.RawMove()
        this.movementVector = addPair(this.movementVector,Pair(-1,0))
        return GAME_STATUS.CONTINUE
    }
}

class Player(cords: MutableSet<Pair<Int, Int>>, console: Console): Entity(cords, console){
    public fun clearCords(){
        for (cord in this.cords){
            this.console.printChar(cord,0, ' ')
        }
    }

    public fun printCords(){
        for (cord in this.cords){
            this.console.printChar(cord,color = 32,ch = '*') // 32 = green
        }
    }

    private fun tryMove(){
        var wallAdjacent = false
        for (cord in this.cords){
            if (!Entity.validWidthConstraint(Entity.addPair(this.movementVector,cord))){
                wallAdjacent = true
                break
            }
        }
        if (!wallAdjacent){
            this.RawMove()
        }
    }

    public fun moveRight(){
        this.movementVector = Pair(0,1)
        this.clearCords()
        this.tryMove()
        this.printCords()
    }

    public fun moveLeft(){
        this.movementVector = Pair(0,-1)
        this.clearCords()
        this.tryMove()
        this.printCords()
    }
}