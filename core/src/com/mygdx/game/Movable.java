package com.mygdx.game;

// Basic Movable interface.

public interface Movable
{
 // An object implementing this interface should be set up such that it can
 // calculate its next position just by having this method called when inside
 // the render() function of the VProgEngine.
 // The move method takes an interval in seconds (chosen because that is exactly
 // what Gdx.graphics.getDeltaTime() returns).
 void move(float interval_s);
 
 // For example, the object is an instance of Mover, and it stores information
 // about its speed and acceleration. It can therefore calculate where it would
 // be next.
 // This way when we have many objects in the engine at once the code for their
 // possibly different movement logic is encapsulated in their classes, instead
 // of in the main render() function.
}
