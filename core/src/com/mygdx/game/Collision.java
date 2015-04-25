package com.mygdx.game;

public class Collision
{
   public float x;
   public float y;
   public Collider collider;
   public Collision(float _x, float _y, Collider _collider)
   {
       x = _x;
       y = _y;
       collider = _collider;
   }
}
