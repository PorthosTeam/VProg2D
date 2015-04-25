package com.mygdx.game;

public interface GameBaseObject extends Collidable, Movable, Drawable
{
    public float getX();
    public float getY();
    public float getXSpeed();
    public float getYSpeed();
    public float getXAccel();
    public float getYAccel();
}
