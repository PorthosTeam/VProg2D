package com.mygdx.game;

// This class is meant to be a low lever representation of a collision, ONLY for
// the other GameBaseObject involved in the collision. I.e. GameBaseObject A is
// given an instance of this object to inform it (GameBaseObject A) that it has
// collided with another GameBaseObject (GameBaseObject B).

// This is NOT intended to represent collisions unambiguously to parts of code
// besides the GameBaseObject involved in the collision. Doing so would require
// a three-element tuple, obviously, containing both colliders.

public class Collision
{
    // This is one of the objects in the collision. The other object is the one
    // that this collision information is passed to.
    public GameBaseObject objectHit;
    
    // This is the angle from the object's center that the collision was.
    public float angle;
    
    public Collision(GameBaseObject _objectHit, float _angle)
    {
        objectHit = _objectHit;
        angle = _angle;
    }
}
