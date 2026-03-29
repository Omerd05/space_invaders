val H_CONSTRAINT_MAX = 0
val W_CONSTRAINT_MIN = 0
val W_CONSTRAINT_MAX = 0

enum class GAME_STATUS{
    CONTINUE,
    OVER
}

abstract class Entity(var cords: MutableSet<Pair<Int, Int>>) {
    var movementVector = Pair(1,0)

    companion object {
        fun addPair(cord: Pair<Int, Int>, vec: Pair<Int,Int>) : Pair<Int, Int> {
            return Pair(cord.first+vec.first,cord.second+vec.second)
        }
        fun negatePair(cord: Pair<Int, Int>) : Pair<Int, Int>{
            return Pair(-cord.first,-cord.second)
        }
        fun validWidthConstraint(cord: Pair<Int, Int>): Boolean{
            return cord.first >= W_CONSTRAINT_MIN && cord.second <= W_CONSTRAINT_MAX
        }
        fun validHeightConstraint(cord: Pair<Int, Int>): Boolean{
            return cord.second <= H_CONSTRAINT_MAX
        }
    }

    protected fun RawMove(){
        val nextCords = mutableSetOf<Pair<Int, Int>>()
        for (cord in cords){
            nextCords.add(Entity.addPair(cord,this.movementVector))
        }
        this.cords.clear()
        this.cords = nextCords
    }
    //abstract fun WrapMove() : GAME_STATUS
}

class Invader(cords: MutableSet<Pair<Int, Int>>) : Entity(cords) {
    fun WrapMove() : GAME_STATUS {
        var wall_adjacent = false
        for (cord in cords){
            if (!Entity.validWidthConstraint(Entity.addPair(cord,this.movementVector))){
                wall_adjacent = true
                break
            }
        }
        if (!wall_adjacent){
            this.RawMove()
            return GAME_STATUS.CONTINUE
        }

        // Reached Player
        for (cord in cords){
            if (!Entity.validHeightConstraint(Entity.addPair(cord,Pair(0,1)))){
                return GAME_STATUS.OVER
            }
        }

        //Lower the height by 1, reverse the direction of the invaders and continue as before
        this.movementVector = negatePair(this.movementVector)
        this.RawMove()
        return GAME_STATUS.CONTINUE
    }
}

class Player(cords: MutableSet<Pair<Int, Int>>): Entity(cords){

}