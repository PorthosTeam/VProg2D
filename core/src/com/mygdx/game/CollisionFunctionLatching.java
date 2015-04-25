package com.mygdx.game;

public class CollisionFunctionLatching implements CollisionFunction
{
    public Object call(Collider self, Collision[] collisions)
    {
        for(int i = 0, len = collisions.length; i < len; i += 1)
        {
            if(self.y >= collisions[i].collider.getY())
            {
                self.ySpeed = 0;
                break;
            }
        }
        return null;
    }
}
