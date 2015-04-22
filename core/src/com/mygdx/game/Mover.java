package com.mygdx.game;

// A Mover is basically a movable version of the Doodad.

// It's essentially just a Doodad (Rectangle with a reference to a Texture and
// to a SpriteBatch), and with X and Y axis speeds and acceleration values.

// These are needed for the constructors.
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;

public class Mover extends Doodad implements Movable
{
    public float xSpeed;
    public float ySpeed;
    public float xAccel;
    public float yAccel;
    
    // These are the fullest-featured constructors. All the options.
    public Mover(float x, float y, float width, float height, Texture texture,
        float _xSpeed, float _ySpeed, float _xAccel, float _yAccel)
    {
        super(x, y, width, height, texture);
        xSpeed = _xSpeed;
        ySpeed = _ySpeed;
        xAccel = _xAccel;
        yAccel = _yAccel;
    }
    public Mover(Rectangle rectange, Texture texture,
        float _xSpeed, float _ySpeed, float _xAccel, float _yAccel)
    {
        super(rectange, texture);
        xSpeed = _xSpeed;
        ySpeed = _ySpeed;
        xAccel = _xAccel;
        yAccel = _yAccel;
    }
    public Mover(Texture texture,
        float _xSpeed, float _ySpeed, float _xAccel, float _yAccel)
    {
        super(texture);
        xSpeed = _xSpeed;
        ySpeed = _ySpeed;
        xAccel = _xAccel;
        yAccel = _yAccel;
    }
    
    // Constructors which set the initial speed but not initial acceleration.
    public Mover(float x, float y, float width, float height, Texture texture,
        float _xSpeed, float _ySpeed)
    {
        super(x, y, width, height, texture);
        xSpeed = _xSpeed;
        ySpeed = _ySpeed;
	xAccel = yAccel = 0;
    }
    public Mover(Rectangle rectange, Texture texture,
        float _xSpeed, float _ySpeed)
    {
        super(rectange, texture);
        xSpeed = _xSpeed;
        ySpeed = _ySpeed;
	xAccel = yAccel = 0;
    }
    public Mover(Texture texture, float _xSpeed, float _ySpeed)
    {
        super(texture);
        xSpeed = _xSpeed;
        ySpeed = _ySpeed;
	xAccel = yAccel = 0;
    }

    // These constructors are basically copies of Doodad's constructors.
    public Mover(float x, float y, float width, float height, Texture texture)
    {
        super(x, y, width, height, texture);
        xSpeed = ySpeed = xAccel = yAccel = 0;
    }
    public Mover(Rectangle rectangle, Texture texture)
    {
        super(rectangle, texture);
        xSpeed = ySpeed = xAccel = yAccel = 0;
    }
    public Mover(Texture texture)
    {
        super(texture);
	xSpeed = ySpeed = xAccel = yAccel = 0;
    }
    
    public void move(float interval_s)
    {
        this.xSpeed += (this.xAccel * interval_s);
        this.ySpeed += (this.yAccel * interval_s);
        this.x += (this.xSpeed * interval_s);
        this.y += (this.ySpeed * interval_s);
    }
}
