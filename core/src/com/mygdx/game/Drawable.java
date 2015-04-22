package com.mygdx.game;

// Basic Drawable interface.

public interface Drawable
{
 // An object implementing this interface should be set up such that it can draw
 // itself just by having this method called when inside the render() function
 // of the VProgEngine.
 void draw();
 
 // For example, the object is an instance of Doodad, and it stores a reference
 // to the master SpriteBatch, its Texture, and its coordinates. The point is,
 // this way the large ~10 argument invocations of SpriteBatch.draw can be moved
 // out of the main render() function and isolated in their appropriate class.
}
