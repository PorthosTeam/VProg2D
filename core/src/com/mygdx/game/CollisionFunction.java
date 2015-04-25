package com.mygdx.game;

// Callback interface. Because Java has no sane way to pass callbacks.

public interface CollisionFunction
{
 // The first argument is a reference to the collider this function is operating
 // on. The next argument is a reference to every collider that function was in
 // a collision with.
 Object call(Collider collider, Collider[] colliders);
}
