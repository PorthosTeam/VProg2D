package com.mygdx.game;

// A doodad (named after those non-unit sprite-and-sometimes-collision-having
// objects in StarCraft, because I (Alex) can't think of a better name just
// makes up the fundamental core base of any "thing" that we'll use as game
// objects.

// It's essentially just a Rectangle, with a reference to a Texture and to a
// Batch.

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Doodad extends Rectangle implements Drawable
{
    // Internal references to the bare minumums needed to be able to render
    // itself:
    // The Batch these are all 'draw()'n onto. Currently no usecase for
    // having more than one for the entire class, so making static.
    private static Batch batch;
    // The texture to use during the draw.
    private Texture texture;
    
    // Not bothering to support constructors without a texture.
    public Doodad(float x, float y, float width, float height, Texture _texture)
    {
        super(x, y, width, height);
        this.setTexture(_texture);
    }
    public Doodad(Rectangle rectange, Texture _texture)
    {
        super(rectange);
        this.setTexture(_texture);
    }
    public Doodad(Texture _texture)
    {
        super();
        this.setTexture(_texture);
    }
    
    public void setTexture(Texture _texture)
    {
        texture = _texture;
    }
    
    // This needs to just be called once for the class as a whole, to set the
    // master Batch that these are all drawn on.
    public static void setBatch(Batch _batch)
    {
        batch = _batch;
    }
    
    public void draw()
    {
        batch.draw(texture, this.x, this.y, this.width, this.height);
    }
}
