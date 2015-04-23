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
    public int leftPatPoint, rightPatPoint;
    
    // add enemy with specific attributes
    public Enemy(int type, int xPos, int yPos, int pp1, int pp2, int patrolling, int speed) {
        x = xPos;
        y = yPos;
        width = 56;
        height = 80;
        eType = type;
        if (patrolling == 1)
            SetPatrolPoints(pp1, pp2);
        hSpeed = speed;
    }
    
    // swaps between the pre-set enemy sprites
    public void swapEnemy(int type) {
        eType = type;
    }
    
    // set patrol behavior for enemy
    public void SetPatrolPoints(int pp1, int pp2) {
        patrolling = true;
        leftPatPoint = pp1;
        rightPatPoint = pp2;
    }
    
    // stop patrolling
    public void stopPatrol() {
        patrolling = false;
    }
    
    public boolean isPatrolling() {
        return patrolling;
    }
    
    // update patrol information
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
