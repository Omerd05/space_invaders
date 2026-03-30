import src.Player;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class KeyPressed {
    Player player;
    public KeyPressed(Player player){
        this.player = player;
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (KeyPressed.class) {
                    if(ke.getID() == KeyEvent.KEY_PRESSED){
                        if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                            player.moveLeft();
                        }
                        else if(ke.getKeyCode() == KeyEvent.VK_LEFT){
                            player.moveRight();
                        }
                    }
                    return false;
                }
            }
        });
    }
}
