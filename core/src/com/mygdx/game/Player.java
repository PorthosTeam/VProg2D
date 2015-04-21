
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import static com.mygdx.game.VProgEngine.prefs;

/**
 *
 * @author T
 */
public class Player extends Rectangle {
    
    // player spawn point
    public float x = 300, y = 156;
    
    // set player speed
    public int hSpeed = 300;
    public int vSpeed = 600;
    public boolean jumpReady = true, jumpDone = false, jumping = false;
    public float jumpHeight = VProgEngine.ground + 150;

    // set the player sprite (0-2 currently)
    public int playerSpriteIndex = 0;
    
    // direction the player sprite is facing
    public boolean left = false;
    
    public Player() {
        //super();
        width = 56;
        height = 80;
    }
    
    
    // create a Rectangle to logically represent the player
    public void changePlayer(int index) {
        // swaps between the pre-set player sprites
        if(index >= 0 && index <= 2) {
            playerSpriteIndex = index;
            prefs.putInteger("player", playerSpriteIndex);
        }
    }
    
}
