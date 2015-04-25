package com.mygdx.game;

public class CollisionFunctionBouncy implements CollisionFunction
{
    public Object call(Collider self, Collision[] collisions)
    {
        for(int i = 0, len = collisions.length; i < len; i += 1)
        {
            self.xSpeed += collisions[i].collider.xSpeed;
            self.ySpeed += collisions[i].collider.ySpeed;
        }
        return null;
    }
}
