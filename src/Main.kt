import src.Console
import src.Entity
import src.Player
import src.GAME_STATUS
import src.Invader


fun main() {
    //terminal size 120 x 29 ?
    val INVADER_MOVE_TIME_MS: Long = 30

    val c = src.Console()
    val player = Player(mutableSetOf(Pair(20,60),Pair(21,59),Pair(21,61),Pair(21,60)),c)
    c.clearScreen()
    //player.printCords()
    //readLine()
    //return
    val aliens = mutableSetOf<Invader>(
        Invader(mutableSetOf(Pair(5,60),Pair(5,59),Pair(5,61),Pair(6,60)),c)
    )


    fun gameLoop(console: Console, invaders: Collection<Invader>) : GAME_STATUS {

        // call invaders's WrapMove, if we get GAME_STATUS.OVER then we return it
        for(invader in invaders){
            if (invader.WrapMove()== GAME_STATUS.OVER){
                return GAME_STATUS.OVER
            }
        }

        val cords = mutableSetOf<Pair<Int, Int>>()
        cords.addAll(player.cords)

        for (invader in invaders){
            cords.addAll(invader.cords)
        }
        console.gameState(cords)

        return GAME_STATUS.CONTINUE
    }

    while(gameLoop(c,aliens) != GAME_STATUS.OVER){
        Thread.sleep(INVADER_MOVE_TIME_MS)
    }

    /*
        NEW DESIGN:
            class Board - holds a 2d table, the current state of the game
                every call to a move method (for both player and invader), does the following:
                    updates the table
                    and updates the diff list (difference from last state)
                    here the functionality diverges based on the call type - for player we call flush(), whereas for
                    invader we call flush() only in gameLoop (thus all invaders move at the same time)

     */
}