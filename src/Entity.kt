package src

import Board
import Entry
import kotlin.collections.mutableMapOf

enum class GAME_STATUS{
    CONTINUE,
    OVER
}

abstract class Entity(var cords: MutableSet<Pair<Int, Int>>, val board: Board, var entities_id: MutableMap<Int, Entity>) {
    var movementVector = Pair(0,0)
    val id = counter++

    fun validWidthConstraint(cord: Pair<Int, Int>): Boolean {
        return cord.second >= 0 && cord.second < this.board.width
    }
    fun validHeightConstraint(cord: Pair<Int, Int>): Boolean {
        return cord.first >= 0 && cord.first < board.height
    }

    companion object {
        fun addPair(cord: Pair<Int, Int>, vec: Pair<Int,Int>) : Pair<Int, Int> {
            return Pair(cord.first+vec.first,cord.second+vec.second)
        }
        fun negatePair(cord: Pair<Int, Int>) : Pair<Int, Int>{
            return Pair(-cord.first,-cord.second)
        }
        var counter = 1
    }

    protected fun RawMove(initFlag: Boolean = false){
        var nextCords = mutableSetOf<Pair<Int, Int>>()
        for (cord in cords){
            nextCords.add(addPair(cord,this.movementVector))
        }

        if(initFlag){
            nextCords = cords
        }
        for (cord in this.cords + nextCords){
            if (!this.cords.contains(cord) || initFlag){ //new one
                board.setEntry(cord, Entry(this.id,'*',32))
            }
            else if (!nextCords.contains(cord)){ //old one
                board.setEntry(cord, Entry(-1,' ',0))
            }
        }
        if(initFlag){
            board.flush()
            return
        }
        this.cords.clear()
        this.cords = nextCords
    }
    public open fun initEntity(){
        RawMove(true)
        entities_id[id] = this
    }
    fun selfDestruct(){
        //print("Ah?")
        for (cord in this.cords){
            this.board.setEntry(cord,Entry(-1,' '))
        }
        this.board.flush()
        entities_id.remove(id)
    }
}

class Invader(cords: MutableSet<Pair<Int, Int>>, board: Board, entities_id: MutableMap<Int, Entity>) : Entity(cords,board,entities_id) {
    init {
        this.movementVector = Pair(0,1)
    }
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

class Player(cords: MutableSet<Pair<Int, Int>>, board: Board, entities_id: MutableMap<Int, Entity>): Entity(cords, board, entities_id){
    private fun tryMove(){
        var wallAdjacent = false
        for (cord in this.cords){
            if (!validWidthConstraint(Entity.addPair(this.movementVector,cord))){
                wallAdjacent = true
                break
            }
        }
        if (!wallAdjacent){
            this.RawMove()
        }
        board.flush()
    }

    public fun moveRight(){
        this.movementVector = Pair(0,1)
        this.tryMove()
    }

    public fun moveLeft(){
        this.movementVector = Pair(0,-1)
        this.tryMove()
    }

    public fun shoot(){
        var lowest_point = Pair(1000,1000)
        for (cord in this.cords){
            if (lowest_point.first > cord.first){
                lowest_point = Pair(cord.first-1,cord.second)
            }
        }
        val bullet = Bullet(mutableSetOf(lowest_point), board, entities_id)
        bullet.initEntity()
    }
}

class Bullet(cords: MutableSet<Pair<Int, Int>>, board: Board, entities_id: MutableMap<Int, Entity>) : Entity(cords, board, entities_id){
    init {
        movementVector = Pair(-1,0)
    }

    override fun initEntity() {
        for (cord in this.cords){
            if (this.board.table[cord.first][cord.second].entity_id != -1){
                entities_id[this.board.table[cord.first][cord.second].entity_id]?.selfDestruct()
                return
            }
        }
        super.initEntity()
    }

    public fun move() {
        for (cord in this.cords){
            if (!validHeightConstraint(addPair(cord,this.movementVector))){
                this.selfDestruct()
                return
            }
        }
        for (cord in this.cords){
            val nextCord =  addPair(cord,this.movementVector)
            if (this.board.table[nextCord.first][nextCord.second].entity_id != -1){
                entities_id[this.board.table[nextCord.first][nextCord.second].entity_id]?.selfDestruct()
                this.selfDestruct()
                return
            }
        }
        this.RawMove()
    }
}