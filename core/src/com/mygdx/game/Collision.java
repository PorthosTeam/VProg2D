package com.mygdx.game;

// This class is meant to be a low lever representation of a collision, ONLY for
// the other Collidable involved in the collision. I.e. Collidable A is given an
// instance of this object to inform it (Collidable A) that it has collided with
// another Collidable (Collidable B), and the specified time.

// This is NOT intended to represent collisions unambiguously to parts of code
// besides the Collidables involved in the collision. Doing so would require a
// three-element tuple, obviously, containing both colliders.

public class Collision
{
    // This is one of the objects in the collision. The other object is the one
    // that this collision information is passed to.
    public Collidable objectHit;
    
    // This is the time offset, in seconds, of when the collision is calculated
    // to have occurred, counting up from the LAST frame. That is to say:
    // If you use "a priori"/continuous/predictive collision detection, this
    // would be the predicted offset in the future that the collision is going
    // to occur at.
    // If you are using "a posteriori"/discrete collision detection, this would
    // be how long after the previous frame the collision happened.
    public float timeOffset_s;
    
    public Collision(Collidable _objectHit, float _timeOffset_s)
    {
        objectHit = _objectHit;
        timeOffset_s = _timeOffset_s ;
    }
}
