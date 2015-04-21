/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author T
 */
public class Enemy extends Rectangle {
    public int eType;
    
    public Enemy(int type, int xPos, int yPos) {
        x = xPos;
        y = yPos;
        width = 56;
        height = 80;
        eType = type;
    }
    
    // swaps between the pre-set enemy sprites
    public void swapEnemy(int type) {
        eType = type;
    }
    
}
