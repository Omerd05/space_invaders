import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.terminal.Terminal

data class Entry(var entity_id: Int, var symbol: Char, var color: Int = 32)

class Board(val console: Terminal, val height: Int = 30, val width: Int = 100) {
    var table = Array(height) { Array<Entry>(width) {Entry(-1,' ',0)} }
    var diff = mutableListOf<Pair<Int, Int>>() //difference from last snapshot

    fun setEntry(cord: Pair<Int,Int>, entry: Entry){
        if(table[cord.first][cord.second] != entry){
            diff.add(cord)
        }
        table[cord.first][cord.second] = entry
    }

    fun flush(){
        for(cord in diff){
            console.setCursorPosition(cord.second,cord.first)
            console.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT)
            console.putCharacter(table[cord.first][cord.second].symbol)
            console.setForegroundColor(TextColor.ANSI.DEFAULT)
            console.setCursorPosition(0,0)
        }
        console.flush()
        diff.clear()
    }
}