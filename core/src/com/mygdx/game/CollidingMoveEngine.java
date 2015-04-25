package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

// Implements movable so VProgEngine can slap it in with the other Movables.
public class CollidingMoveEngine implements Movable
{
/*
    private class ColliderState
    {
        public float x;
        public float y;
        public float xSpeed;
        public float ySpeed;
        public float xAccel;
        public float yAccel;
        public ColliderState(Collider collider)
        {
            x = collider.x;
            y = collider.y;
            xSpeed = collider.xSpeed;
            ySpeed = collider.ySpeed;
            xAccel = collider.xAccel;
            yAccel = collider.yAccel;
        }
    }
*/
    
    private double getLength(Vector2 a, Vector2 b)
    {
System.out.println("a.x: " + a.x);
System.out.println("a.x: " + b.x);
        float xPart = a.x + b.x;
        float yPart = a.y + b.y;
        return Math.sqrt((xPart * xPart) + (yPart * yPart));
    }    

    private class MoveLine
    {
        public Vector2 a;
        public Vector2 b;
        public MoveLine(float x, float y)
        {
            a = new Vector2(x, y);
        }
        public void compute(float x, float y)
        {
            b = new Vector2(x, y);
        }
    }
    
    private class MoveLines
    {
        public MoveLine[] lines = new MoveLine[4];
        public double length;
        
        public MoveLines(Rectangle rectangle)
        {
            lines[0] = new MoveLine(rectangle.x, rectangle.y);
            lines[1] = new MoveLine(rectangle.x + rectangle.width, rectangle.y);
            lines[2] = new MoveLine(rectangle.x, rectangle.y+rectangle.height);
            lines[3] = new MoveLine(lines[1].a.x, lines[2].a.y);
        }
        public void compute(Rectangle rectangle)
        {
            lines[0].compute(rectangle.x, rectangle.y);
            lines[1].compute(rectangle.x + rectangle.width, rectangle.y);
            lines[2].compute(rectangle.x, rectangle.y + rectangle.height);
            lines[3].compute(lines[1].b.x, lines[2].b.y);
            length = getLength(lines[0].a, lines[0].b);
        }
    }
    
    private class BoS
    {
        public Collider c;
        public MoveLines lns;
        public BoS(Collider _c)
        {
            c = _c;
            lns = new MoveLines(_c);
        }
    }
    
    private class CollisionFull implements Comparable<CollisionFull>
    {
       public Collision[] collisions;
       public double interval_s;
       CollisionFull(double _interval_s, Vector2 coord,
           Collider collider1, Collider collider2)
       {
           collisions = new Collision[2];
           collisions[0] = new Collision(coord.x, coord.y, collider1);
           collisions[1] = new Collision(coord.x, coord.y, collider2);
           interval_s = _interval_s;
       }
       
       @Override
       public int compareTo(CollisionFull o)
       {
          if(interval_s < o.interval_s)
          {
              return -1;
          }
          else if(interval_s > o.interval_s)
          {
              return 1;
          }
          else //interval_s == o.interval_s
          {
              return 0;
          }
       }
    }
    
    private Array<BoS> boSs;
    
    public CollidingMoveEngine()
    {
        // Want unordered array for the performance benefit, but the constructor
        // requires we also set a capacity (and currently the libGDX default is
        // 16 for the Array type).
        boSs = new Array<BoS>(false, 16);
    }
    
    public void register(Collider collider)
    {
        boSs.add(new BoS(collider));
    }
    
    private CollisionFull computeCollisions(float interval_s)
    {
        Array<CollisionFull> collisions;
        int len = boSs.size;
        if(len == 0)
        {
            return null;
        }
        
        for(int i = 0; i < len; i += 1)
        {
            BoS boS = boSs.get(i);
            boS.lns = new MoveLines(boS.c);
            boS.lns.compute(boS.c.simulateMove(interval_s));
        }
        
        Vector2[] intersections
            = { new Vector2(), new Vector2(), new Vector2(), new Vector2()};
        collisions = new Array<CollisionFull>(false, 16);
        for(int i = len - 1; i >= 0; i -= 1)
        {
            BoS boS1 = boSs.get(i);
            for(int j = 0; j < i; j += 1)
            {
                BoS boS2 = boSs.get(j);
                Vector2 soonest = null;
                double shortestDistance = Double.POSITIVE_INFINITY;
                for(int k = 0; k < 4; k += 1)
                {
                    MoveLine line1 = boS1.lns.lines[k];
                    MoveLine line2 = boS2.lns.lines[k];
                    if(Intersector.intersectSegments(line1.a, line1.b,
                        line2.a, line2.b, intersections[k])
                    )
                    {
                        double distance = getLength(line1.a, intersections[k]);
                        if(distance < shortestDistance)
                        {
                            shortestDistance = distance;
                            soonest = intersections[k];
                        }
                    }
                }    
                if(soonest == null)
                {
                    continue;
                }
                double ratio = shortestDistance / boS1.lns.length;
                collisions.add(new CollisionFull(
                    interval_s * ratio, soonest, boS1.c, boS2.c
                ));
            }
        }
        if(collisions.size == 0)
        {
            return null;
        }
        
        // Currently assuming no collisions are truly SIMULTANEOUS. This will
        // mess up if collisions really are (albeit in a very non-noticeable
        // way).
        collisions.sort();
        // Really sucks that I have to discard the other collisions, but in this
        // naive implementation, we have to recompute all collisions after the
        // first one anyway.
        return collisions.get(0);
    }
    
    public void move(float interval_s)
    {
        float timeLeft_s = interval_s;
        CollisionFull collisionFull;
        // Some day I'd like to support multiple collisions at once, but right
        // now it's all actually just one collision.
        Collision[] collision = new Collision[1];
        while((collisionFull = computeCollisions(timeLeft_s)) != null)
        {
            float timeToCollision_s = (float )collisionFull.interval_s;
            timeLeft_s -= timeToCollision_s;
            actuallyMove(timeToCollision_s);
            collision[0] = collisionFull.collisions[1];
            collisionFull.collisions[0].collider.collide(collision);
            collision[0] = collisionFull.collisions[0];
            collisionFull.collisions[1].collider.collide(collision);
        }
        actuallyMove(timeLeft_s);
    }
    
    public void actuallyMove(float interval_s)
    {
        for(int i = 0, len = boSs.size; i < len; i += 1)
        {
            boSs.get(i).c.move(interval_s);
        }
    }
}
