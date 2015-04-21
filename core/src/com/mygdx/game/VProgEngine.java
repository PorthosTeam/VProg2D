/*
 Game testing - WASD / arrows to move, space to jump, 1-3 to change player sprite, 
 M to set and play BGM, S to play a sound, P to save stuff, click to draw a circle 
 at the mouse position, E to spawn an enemy on the ground under the mouse, B for 
 a text box because why not. Lots of this needs to be hooked into the UI, among 
 other things. 
 - Trevor
 */
package com.mygdx.game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import java.util.Random;

//import java.util.Iterator;
import com.badlogic.gdx.ApplicationAdapter;
//import com.badlogic.gdx.ApplicationListener;
//import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Circle;
//import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;

public class VProgEngine extends ApplicationAdapter {

    // Test create project module
    private boolean CREATE_DEBUG = false;

    // global vars
    public static Preferences prefs;
    
    // location of an uploaded asset
    AssetManager manager;
    public String assetLocation;

    // bounds of the game frame
    public int rightBound = 1000 - 56;
    public int leftBound = 200;

    // Game assets
    private Array<Texture> backgrounds;
    private Array<Texture> playerSprites;
    private Array<Texture> enemySprites;
    
    // Player
    public Player playerInstance;

    // Enemies
    private Rectangle enemy;
    public Array<Enemy> enemies;
    
    // Environment
    public static float ground = 156;
    public int bgIndex = 0;

    // Used to insert a circle at mouse position
    // also a crude form of drawing
    private ShapeRenderer shapeRenderer;
    public Array<Circle> circles;

    private Sound sound1;
    private Music bgm1;

    // The infamous node. If only we knew how to use it.
    private Node node;

    public static int WIDTH = 800, HEIGHT = 600;
    SpriteBatch batch;
    Texture img;

    @Override
    public void create() {
        // Get/initialize game preferences/state. Details documented at:
        // https://github.com/libgdx/libgdx/wiki/Preferences
        prefs = Gdx.app.getPreferences("vprog2d_savestate");

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        manager = new AssetManager();
        
        // load the pre-set assets for selection
        playerSprites = new Array<Texture>();
        playerSprites.add(new Texture(Gdx.files.internal("player.png")));
        playerSprites.add(new Texture(Gdx.files.internal("player2.png")));
        playerSprites.add(new Texture(Gdx.files.internal("player3.png")));
        enemySprites = new Array<Texture>();
        enemySprites.add(new Texture(Gdx.files.internal("enemy1.png")));
        enemySprites.add(new Texture(Gdx.files.internal("enemy2.png")));
        backgrounds = new Array<Texture>();
        backgrounds.add(new Texture(Gdx.files.internal("bg1.png")));
        backgrounds.add(new Texture(Gdx.files.internal("bg2.png")));
        backgrounds.add(new Texture(Gdx.files.internal("bg3.png")));
        backgrounds.add(new Texture(Gdx.files.internal("bg4.png")));
        //backgrounds.add(new Texture(Gdx.files.internal("bg5.png")));
        sound1 = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
        bgm1 = Gdx.audio.newMusic(Gdx.files.internal("bgm1.ogg"));

        // Store enemies
        enemies = new Array<Enemy>();

        // Store drawn circles
        circles = new Array<Circle>();

        // Debug stuff
        if (CREATE_DEBUG == true) {
            Gdx.app.log("Test", "Testing");
            JFrame frame = new JFrame("Create Project");
            User testUser = new User("TestUser");
            Project project = new Project();
            // testUser.addProject(project);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            frame.getContentPane().add(project);
            frame.setSize(project.getPreferredSize());
            frame.setVisible(true);
        }

        // UI panel (TBD)
        Rectangle panel = new Rectangle();
        panel.height = Gdx.graphics.getHeight();
        panel.width = Gdx.graphics.getWidth() / 10;

        // draw the default player
        playerInstance = new Player();
        playerInstance.changePlayer(0);

    }

    // set the bgm music (also plays it)
    public void changeMusic(int num) {
        switch (num) {
            case 1:
                bgm1.setLooping(true);
                bgm1.play();
        }
    }

    // set / play a sound file
    public void playSound(int num) {
        switch (num) {
            case 1:
                sound1.play();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // basic UI
        batch.begin();
        //batch.draw(logo, Gdx.graphics.getWidth() / 2 - logo.getWidth() / 2, Gdx.graphics.getHeight() - logo.getHeight());

        // draw assets
        // draw the background
        batch.draw(backgrounds.get(bgIndex), leftBound, 120);

        // draw the set player sprite at current location
        // monstrous method call but it's necessary for a simple texture flip
        if(playerInstance.playerSpriteIndex >= 0 && playerInstance.playerSpriteIndex <= 2) {
            batch.draw(playerSprites.get(playerInstance.playerSpriteIndex),
                playerInstance.x, playerInstance.y, playerInstance.width, playerInstance.height, 0, 0,
                (int) playerInstance.width, (int) playerInstance.height, playerInstance.left, false);
        }

        // render enemies
        for (Enemy currEnemy : enemies) {
            batch.draw(enemySprites.get(currEnemy.eType), currEnemy.x, currEnemy.y);
        }
        batch.end();

        // Player movement at set speed
        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            playerInstance.x -= playerInstance.hSpeed * Gdx.graphics.getDeltaTime();
            playerInstance.left = true;
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            playerInstance.x += playerInstance.hSpeed * Gdx.graphics.getDeltaTime();
            playerInstance.left = false;
        }

        // Player jump
        if(Gdx.input.isKeyJustPressed(Keys.SPACE)
        || Gdx.input.isKeyJustPressed(Keys.W)
        || Gdx.input.isKeyJustPressed(Keys.UP)) {
            if (playerInstance.jumpReady == true) {
                playSound(1);
                playerInstance.jumpReady = false;
                playerInstance.jumping = true;
            }
        }
        if (playerInstance.jumping && !playerInstance.jumpDone) {
            playerInstance.y += playerInstance.vSpeed * Gdx.graphics.getDeltaTime();
        }
        if (playerInstance.y >= playerInstance.jumpHeight) {
            playerInstance.jumpDone = true;
        }
        if (playerInstance.y > ground && playerInstance.jumpDone) {
            playerInstance.y -= playerInstance.vSpeed * Gdx.graphics.getDeltaTime();
        }
        if (playerInstance.y <= ground && playerInstance.jumping) {
            playerInstance.y = ground;
            playerInstance.jumping = false;
            playerInstance.jumpReady = true;
            playerInstance.jumpDone = false;
        }

        // Various buttons to test functionalites
        // swap player sprite
        if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
            playerInstance.changePlayer(0);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
            playerInstance.changePlayer(1);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_3)) {
            playerInstance.changePlayer(2);
        }

        // set music + play
        if (Gdx.input.isKeyJustPressed(Keys.M)) {
            changeMusic(1);
        }

        // set sound + play
        if (Gdx.input.isKeyJustPressed(Keys.S)) {
            playSound(1);
        }

        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            this.save();
        }

        /* Add a circle to the circles array at the mouse pos on left-click 
         Use Gdx.input.isButtonPressed(Input.Buttons.LEFT) or use event 
         handling with touchDown for a more discrete capture so only 
         one circle is drawn at a time which is probably better in this
         case. */
        if (Gdx.input.isTouched()) {
            //Vector2 touchPos = new Vector2();
            //touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            // ShapeRenderer has an unintuitive way of using the Y value
            Circle newCircle = new Circle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY() + 5, 10);
            circles.add(newCircle);
        }

        // spawn an enemy on the ground
        if (Gdx.input.isKeyJustPressed(Keys.E)) {
            enemies.add(new Enemy(new Random().nextInt(2), Gdx.input.getX(), (int) ground));
        }

        // render circles
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        for (Circle circle : circles) {
            shapeRenderer.circle(circle.x, circle.y, 10);
        }
        shapeRenderer.end();

        // erase circles and enemies
        if (Gdx.input.isKeyJustPressed(Keys.R)) {
            circles.clear();
            enemies.clear();
        }

        // Change background
        if (Gdx.input.isKeyJustPressed(Keys.B)) {
            bgIndex = new Random().nextInt(5);
        }
        
        if (Gdx.input.isKeyPressed(Keys.NUM_5)) {
            manager.load("F:/data/bg5.png", Texture.class);
            manager.update();
            manager.finishLoading();
            backgrounds.add(manager.get("F:/data/bg5.png", Texture.class));
        }

        // make sure the player stays within the screen bounds
        if (playerInstance.x < leftBound) {
            playerInstance.x = leftBound;
        }
        if (playerInstance.x > rightBound) {
            playerInstance.x = rightBound;
        }
    }

    @Override
    // dispose of all the native resources
    public void dispose() {
        // lots of stuff to add here
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
    
    // save editor state (only saves player sprite atm)
    public void save()
    {
        prefs.flush();
    }
}
