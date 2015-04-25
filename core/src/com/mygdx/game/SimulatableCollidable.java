package com.mygdx.game;

public interface SimulatableCollidable extends Collidable
{
    Object simulateCollide(Collision[] collisions);
    // The object returned by simulateCollide must be sufficient to be passed
    // as the argument to commitCollide, such that collide(foo) has the exact
    // same effect as commitCollide(simulateCollide(foo)).
    void commitCollide(Object stateObject);
}
