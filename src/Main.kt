//import src.Console
import src.Player
import src.GAME_STATUS
import src.Invader
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import src.Bullet
import src.Entity
import java.util.Dictionary
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource


fun main() {
    val terminal = DefaultTerminalFactory().createTerminal()
    terminal.enterPrivateMode()


    val width = terminal.terminalSize.columns // 80
    val height = terminal.terminalSize.rows // 24

    var entities_id = mutableMapOf<Int, Entity>()

    val board = Board(terminal, height = height, width = width)
    val player = Player(mutableSetOf(Pair(20,60),Pair(21,59),Pair(21,61),Pair(21,60)),board, entities_id)

    var aliens = mutableSetOf<Invader>(
        Invader(mutableSetOf(Pair(5,60),Pair(5,59),Pair(5,61),Pair(6,60)),board, entities_id)
    )

    player.initEntity()
    for(invader in aliens){
        invader.initEntity()
    }


    fun gameLoop(entities: Collection<Entity>) : GAME_STATUS {
        //possibly we can add a flag in rawMove with default value of true, which decides whether to flash or not
        //and a counter inside the for loop so we only pass value of true when we reached the final invader
        val old_entities = mutableSetOf<Entity>()
        for(entity in entities){
            old_entities.add(entity)
        }

        for(entity in old_entities){
            if(!entities.contains(entity)){
                continue
            }
            if(entity is Invader){
                if (entity.WrapMove() == GAME_STATUS.OVER){
                    return GAME_STATUS.OVER
                }
            }
            else if (entity is Bullet){
                entity.move()
                //while iterating entities, when a bullet reaches the end it tries to self destruct and removes itself
                // from the map, which casues the error

                //solutions -
            }
        }

        board.flush()
        return GAME_STATUS.CONTINUE
    }

    val timeSource = TimeSource.Monotonic
    var timeMark = timeSource.markNow()

    while(true){
        val ks = terminal.pollInput()
        if(ks != null){
            if(ks.keyType == KeyType.ArrowRight){
                player.moveRight()
            }
            else if(ks.keyType == KeyType.ArrowLeft){
                player.moveLeft()
            }
            else if(ks.keyType == KeyType.Character && ks.character == ' '){
                player.shoot()
            }
        }
        if (timeMark.elapsedNow() > 100.milliseconds) {
            if(gameLoop(entities_id.values) != GAME_STATUS.CONTINUE){
                break
            }
            timeMark = timeSource.markNow()
        }
    }

    terminal.exitPrivateMode()
}