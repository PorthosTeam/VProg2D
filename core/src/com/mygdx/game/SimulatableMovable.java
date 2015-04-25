package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public interface SimulatableMovable extends Movable
{
    Rectangle simulateMove(float interval_s);
}
