package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

// Implements movable so VProgEngine can slap it in with the other Movables.
public class CollidingMoveEngine implements Movable
{
    private double getLength(Vector2 a, Vector2 b)
    {
        float xPart = a.x + b.x;
        float yPart = a.y + b.y;
        return Math.sqrt((xPart * xPart) + (yPart * yPart));
    }
    
    private class BorderLine
    {
        public Vector2 a;
        public Vector2 b;
        public BorderLine(float a_x, float a_y, float b_x, float b_y)
        {
            a = new Vector2(a_x, a_y);
            b = new Vector2(b_x, b_y);
        }
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
    
    private class BorderLines
    {
        public BorderLine[] lines = new BorderLine[4];
        public BorderLines(Rectangle rectangle)
        {
            lines[0] = new BorderLine(rectangle.x, rectangle.y,
                rectangle.x + rectangle.width, rectangle.y);
            lines[1] = new BorderLine(rectangle.x, rectangle.y,
                rectangle.x, rectangle.y + rectangle.height);
            lines[2] = new BorderLine(
                rectangle.x + rectangle.width, rectangle.y,
                rectangle.x + rectangle.width, rectangle.y + rectangle.height
            );
            lines[3] = new BorderLine(
                rectangle.x, rectangle.y + rectangle.height,
                rectangle.x + rectangle.width, rectangle.y + rectangle.height
            );
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
        public BorderLines brd;
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
    
    Vector2 tempIntersection = new Vector2();
    private double checkForCollision(BoS boS1, BoS boS2, Vector2 coordinates)
    {
        double shortestDistance = Double.POSITIVE_INFINITY;
        for(int k1 = 0; k1 < 4; k1 += 1)
        {
            MoveLine line1 = boS1.lns.lines[k1];
            for(int k2 = 0; k2 < 4; k2 += 1)
            {
                BorderLine line2 = boS2.brd.lines[k2];
                if(Intersector.intersectSegments(line1.a, line1.b,
                    line2.a, line2.b, tempIntersection)
                )
                {
                    double distance = getLength(line1.a, tempIntersection);
                    if(distance < shortestDistance)
                    {
                        shortestDistance = distance;
                        coordinates.x = tempIntersection.x;
                        coordinates.y = tempIntersection.y;
                    }
                }
            }
        }    
        return shortestDistance;
    }
    
    private CollisionFull getCollisions(float interval_s)
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
            boS.brd = new BorderLines(boS.c);
            boS.lns.compute(boS.c.simulateMove(interval_s));
        }
        
        collisions = new Array<CollisionFull>(false, 16);
/*
        for(int i = len - 1; i >= 0; i -= 1)
        {
            BoS boS1 = boSs.get(i);
            for(int j = 0; j < i; j += 1)
*/
        for(int i = 0; i < len; i += 1)
        {
            BoS boS1 = boSs.get(i);
            for(int j = 0; j < len; j += 1)
            {
                if(i == j)
                {
                    continue;
                }
                BoS boS2 = boSs.get(j);
                Vector2 coordinates = new Vector2();
                double distance = checkForCollision(boS1, boS2, coordinates);
                if(distance == Double.POSITIVE_INFINITY)
                {
                    continue;
                }
                double ratio = distance / boS1.lns.length;
                collisions.add(new CollisionFull(
                    interval_s * ratio, coordinates, boS1.c, boS2.c
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
        // now it's all actually only one collision per collider per step.
        Collision[] collision = new Collision[1];
        while((collisionFull = getCollisions(timeLeft_s)) != null)
        {
            float timeToCollision_s = (float )collisionFull.interval_s;
            timeLeft_s -= timeToCollision_s;
            actuallyMove(timeToCollision_s);
            Collision[] collisions = collisionFull.collisions;
            // Don't want to change good interface now to make less good
            // implementation neater, so doing this kludgery with a single
            // array argument...
            collision[0] = collisions[1];
            Collider collisionResult
                = (Collider )collisions[0].collider.simulateCollide(collision);
            collision[0] = collisions[0];
            collisions[1].collider.collide(collision);
            collisions[0].collider.commitCollide(collisionResult);
        }
        actuallyMove(timeLeft_s);
    }

public void debugPrint(Collider c)
{
 System.out.println("x: " + c.x + " y: " + c.y + " xS: " + c.xSpeed
  + " yS: " + c.ySpeed + " xA: " + c.xAccel + " yA: " + c.yAccel);
}
    
    public void actuallyMove(float interval_s)
    {
        for(int i = 0, len = boSs.size; i < len; i += 1)
        {
            boSs.get(i).c.move(interval_s);
        }
    }
}
