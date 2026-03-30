package src

class Console {
    public fun clearScreen(){
        print("\u001b[H\u001b[2J") //[H moves cursor to home position (0,0), while [2J erases the entire screen

        // https://stackoverflow.com/questions/18037576/how-do-i-check-if-the-user-is-pressing-a-key
    }
    public fun printChar(cord: Pair<Int,Int>, color: Int, ch: Char){
        print("\u001b[H\u001b[${cord.first}B\u001b[${cord.second}C\u001b[${color}m${ch}\u001b[0m\u001b[H")
    }

    public fun gameState(cords: MutableSet<Pair<Int, Int>>, color: Int = 32){
        clearScreen()
        for(cord in cords){
            printChar(cord, color, '*')
        }
    }
}