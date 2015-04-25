package com.mygdx.game;

// Basic Collidable interface. In short, this interface is about how the class
// responds to collisions, not about how collisions are detected.

public interface Collidable
{
    // An object implementing this interface should be set up such that it can
    // respond to a collision and adjust its internal state correctly just from
    // having this method called when inside the render() function of
    // VProgEngine.
    void collide(Collider[] colliders);
    
    // The idea is that whatever the collision engine is, when it detects that a
    // collision occurs, it invokes this method on every object that was
    // involved in at least one collision that frame.
    
    // The argument is an array of objects that describe collisions (see
    // Collision.java). This allows each Collidable maximum flexibility with
    // regards to implementation of collision behavior:
    // They can do advanced math to resolve the details of every collision in
    // the order they would have occurred, they can just pretend every collision
    // within the same frame happened simultaneously and implement much simpler
    // logic, and they can either do just collision behavior, or use this as an
    // opportinity to also hook up other logic (e.g. check the type of the other
    // collidable to determine if the collision does damage to this collidable,
    // which is common in video game mechanics, or to play a sound on collision
    // based on what other object was collided with, etc.
    
    // As with the other base-building-block interfaces, the intent is to allow
    // for classes to neatly specify their collision behavior inside themselves,
    // while letting the render loop code stay clean and lean.
}
