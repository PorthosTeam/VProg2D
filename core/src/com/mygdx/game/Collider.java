package com.mygdx.game;

// These are needed for the constructors.
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;

public class Collider extends Mover implements SimulatableCollidable
{
    private CollisionFunction collisionFunction;
    
    // Wrappers around the really featureful constructors.
    public Collider(float x, float y, float width, float height,
        Texture texture, float xSpeed, float ySpeed,
        float xAccel, float yAccel, CollisionFunction _collisionFunction)
    {
        super(x, y, width, height, texture, xSpeed, ySpeed, xAccel, yAccel);
        collisionFunction = _collisionFunction;
    }
    public Collider(Rectangle rectangle, Texture texture,
        float xSpeed, float ySpeed, float xAccel, float yAccel,
        CollisionFunction _collisionFunction)
    {
        super(rectangle, texture, xSpeed, ySpeed, xAccel, yAccel);
        collisionFunction = _collisionFunction;
    }
    public Collider(Texture texture, float xSpeed, float ySpeed,
        float xAccel, float yAccel, CollisionFunction _collisionFunction)
    {
        super(texture, xSpeed, ySpeed, xAccel, yAccel);
        collisionFunction = _collisionFunction;
    }
    
    // Wrappers around Mover constructors which set speed but not acceleration.
    public Collider(float x, float y, float width, float height,
        Texture texture, float xSpeed, float ySpeed,
        CollisionFunction _collisionFunction)
    {
        super(x, y, width, height, texture, xSpeed, ySpeed);
        collisionFunction = _collisionFunction;
    }
    public Collider(Rectangle rectangle, Texture texture,
        float xSpeed, float ySpeed, CollisionFunction _collisionFunction)
    {
        super(rectangle, texture, xSpeed, ySpeed);
        collisionFunction = _collisionFunction;
    }
    public Collider(Texture texture, float xSpeed, float ySpeed,
        CollisionFunction _collisionFunction)
    {
        super(texture, xSpeed, ySpeed);
        collisionFunction = _collisionFunction;
    }
    
    // These are just wrappers around Mover constructors which just wrap 
    // Doodad constructors.
    public Collider(float x, float y, float width, float height,
        Texture texture, CollisionFunction _collisionFunction)
    {
        super(x, y, width, height, texture);
        collisionFunction = _collisionFunction;
    }
    public Collider(Rectangle rectangle, Texture texture,
        CollisionFunction _collisionFunction)
    {
        super(rectangle, texture);
        collisionFunction = _collisionFunction;
    }
    public Collider(Texture texture, CollisionFunction _collisionFunction)
    {
        super(texture);
        collisionFunction = _collisionFunction;
    }
    
    // Copy constructor
    public Collider(Collider collider)
    {
        super(collider);
        collisionFunction = collider.collisionFunction;
    }
    
    public Object simulateCollide(Collision[] collisions)
    {
        Collider newStateHolder = new Collider(this);
        newStateHolder.collide(collisions);
        return newStateHolder;
    }
    
    public void commitCollide(Object newStateHolder)
    {
        x = ((Collider )newStateHolder).x;
        y = ((Collider )newStateHolder).y;
        xSpeed = ((Collider )newStateHolder).xSpeed;
        ySpeed = ((Collider )newStateHolder).ySpeed;
        xAccel = ((Collider )newStateHolder).xAccel;
        yAccel = ((Collider )newStateHolder).yAccel;
    }
    
    public void collide(Collision[] collisions)
    {
        collisionFunction.call(this, collisions);
    }
}
