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
    public int hSpeed = 150;
    public boolean left = true;
    public boolean patrolling = false;
    public float leftPatPoint, rightPatPoint;
    
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
    
    public void SetPatrolPoints(float pp1, float pp2) {
        patrolling = true;
        leftPatPoint = pp1;
        rightPatPoint = pp2;
    }
    
    public void stopPatrol() {
        patrolling = false;
    }
    
    public boolean isPatrolling() {
        return patrolling;
    }
    
    public void update(float deltaTime) {
        if (x <= leftPatPoint)
            left = false;
        else if (x >= rightPatPoint )
            left = true;
        if (left)
            x -= hSpeed * deltaTime;
        else
            x += hSpeed * deltaTime;
    }
    
}
