/*
 Game testing - WASD / arrows to move, space to jump, 1-3 to change player sprite, 
 M to set and play BGM, S to play a sound, P to save stuff, click to draw a circle 
 at the mouse position, E to spawn an enemy on the ground under the mouse.

**NEW- 'O' will start the first spawned enemy on patrol and 'I' will stop the
patrol, assuming an enemy is spawned. B will randomly swap between backgrounds,
and '5' will dynamically load the bg5 background from the set path in bg5Path.**

Lots of this needs to be hooked into the UI, among other things. 
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
//import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;

public class VProgEngine extends ApplicationAdapter {

    // Test create project module
    private boolean CREATE_DEBUG = false;
    private String bg5Path = "F:/data/bg5.png";
    
    public static int WIDTH = 800, HEIGHT = 600;

    // global vars
    private String name;
    private boolean frozen = true;
    
    // TODO 2015-04-22: This should NOT be static. But external classes link to
    // it. Should not be thus coupled. Will fix later.
    public static Preferences prefs;
    
    // location of an uploaded asset
    AssetManager manager;
    public String assetLocation;

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
    public static float ground = 36;
    public int bgIndex = 0;
    
    // Text
    private BitmapFont text;

    // Used to insert a circle at mouse position
    // also a crude form of drawing
    private ShapeRenderer shapeRenderer;
    public Array<Circle> circles;

    private Array<Sound> sounds;
    private Array<Music> bgms;

    // The infamous node. If only we knew how to use it.
    private Node node;
    
    // bounds of the game frame
    public int leftBound = 0;
    public int rightBound = WIDTH;
    SpriteBatch batch;
    Texture img;
    
    // This top constructor is valid, because you can then setName to pick your
    // instance name, but presently I can't think of a good reason to use it.
    public VProgEngine(){}
    public VProgEngine(String instanceName)
    {
        this.setName(instanceName);
    }

    @Override
    public void create() {
        this.loadEnginePrefs();

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
        sounds = new Array<Sound>();
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("jump.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("damaged.ogg")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("fall.ogg")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("KO.ogg")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("throw.ogg")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("menu.wav")));
        bgms = new Array<Music>();
        bgms.add(Gdx.audio.newMusic(Gdx.files.internal("bgm1.ogg")));
        
        // Text
        text = new BitmapFont();
        text.setColor(Color.RED);

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

        // draw the default player
        playerInstance = new Player(0);
        rightBound -= playerInstance.width;
    }

    // set the bgm music (also plays it)
    public void changeMusic(int num) {
        bgms.get(num).setLooping(true);
        bgms.get(num).play();
    }

    // set / play a sound file
    public void playSound(int num) {
        sounds.get(num).play();
    }

    @Override
    public void render() {
        // In the long run we will want this AFTER all of the 'draw' updates and
        // AFTER all of the editor-level code (e.g. if we let the user drag/drop
        // in the game UI as part of editing, as opposed to as part of play).
        // But then we want this BEFORE any in-game game mechanics state changes
        // such as the game-object primitives' move() and collide() calls, so
        // that even as the "game" (libGDX application) is running, the game of
        // the user is "frozen".
        if(frozen)
        {
            return;
        }
        
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // basic UI
        batch.begin();
        //batch.draw(logo, Gdx.graphics.getWidth() / 2 - logo.getWidth() / 2, Gdx.graphics.getHeight() - logo.getHeight());

        // draw assets
        // draw the background
        batch.draw(backgrounds.get(bgIndex), leftBound, 0);

        // draw the set player sprite at current location
        // monstrous method call but it's necessary for a simple texture flip
        if(playerInstance.playerSpriteIndex >= 0 && playerInstance.playerSpriteIndex <= 2) {
            batch.draw(playerSprites.get(playerInstance.playerSpriteIndex),
                playerInstance.x, playerInstance.y, playerInstance.width, playerInstance.height, 0, 0,
                (int) playerInstance.width, (int) playerInstance.height, playerInstance.left, false);
        }

        // render enemies
        for (Enemy currEnemy : enemies) {
            batch.draw(enemySprites.get(currEnemy.eType),
                currEnemy.x, currEnemy.y, currEnemy.width, currEnemy.height, 0, 0,
                (int) currEnemy.width, (int) currEnemy.height, !currEnemy.left, false);
            if (currEnemy.isPatrolling()) {
                currEnemy.update(Gdx.graphics.getDeltaTime());
            }
            // basic collision detection
            if (playerInstance.x >= currEnemy.x - currEnemy.width/1.25 && playerInstance.x <= currEnemy.x + currEnemy.width/1.25 && playerInstance.y <= currEnemy.y + currEnemy.height/1.5 )
                text.draw(batch, "OW", WIDTH/2, HEIGHT/1.25f);
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
                playSound(0);
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
            changeMusic(0);
        }

        // set sound + play
        if (Gdx.input.isKeyJustPressed(Keys.S)) {
            playSound(0);
        }

        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            this.saveEnginePrefs();
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
        
        // set first enemy to patrolling
        if (Gdx.input.isKeyJustPressed(Keys.O)) {
            if (enemies.size > 0) {
            Enemy e = enemies.get(0);
            e.SetPatrolPoints(e.x - 100, e.x + 100);
            }
        }
        
        // stop  first enemy from patrolling
        if (Gdx.input.isKeyJustPressed(Keys.I)) {
            if (enemies.size > 0) {
                enemies.get(0).stopPatrol();
            }
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
            if (backgrounds.size == 4)
                bgIndex = new Random().nextInt(4);
            else
                bgIndex = new Random().nextInt(5);
        }
        
        if (Gdx.input.isKeyPressed(Keys.NUM_5)) {
            manager.load(bg5Path, Texture.class);
            manager.update();
            manager.finishLoading();
            backgrounds.add(manager.get(bg5Path, Texture.class));
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
        shapeRenderer.dispose();
        manager.dispose();
        text.dispose();
        for (Texture e : playerSprites)
            e.dispose();
        for (Texture e : enemySprites)
            e.dispose();
        for (Texture e : backgrounds)
            e.dispose();
        for (Sound e : sounds)
            e.dispose();
        for (Music e : bgms)
            e.dispose();
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
    
    // This section contains numerous functions exposed to allow the UI to
    // manage the VProgEngine.
    public void run()
    {
        this.frozen = false;
    }
    public void freeze()
    {
        this.frozen = true;
    }
    
    // save editor state (only saves player sprite atm)
    public void saveEnginePrefs()
    {
        prefs.flush();
    }
    
    // Get/initialize game preferences/state. Details documented at:
    // https://github.com/libgdx/libgdx/wiki/Preferences
    public void loadEnginePrefs()
    {
        prefs = Gdx.app.getPreferences(name);
    }
    
    public void setName(String newName)
    {
        name = newName;
    }
}
