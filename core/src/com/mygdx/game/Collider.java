package com.mygdx.game;

// These are needed for the constructors.
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;

public class Collider extends Mover implements Collidable
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
    
    public void collide(Collider[] colliders)
    {
        collisionFunction.call(this, colliders);
    }
}
