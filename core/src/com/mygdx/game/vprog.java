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

//import java.util.Iterator;
import com.badlogic.gdx.ApplicationAdapter;
//import com.badlogic.gdx.ApplicationListener;
//import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntArray;
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
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Preferences;

public class vprog extends ApplicationAdapter {

    // Test create project module
    private boolean CREATE_DEBUG = false;

    // global vars
    public static Preferences prefs;

    // player spawn point
    public static float x = 300, y = 156;
    public static float ground = 156;

    // set player speed
    public static int hSpeed = 300;
    public static int vSpeed = 600;
    private static boolean jumpReady = true, jumpDone = false, jumping = false;
    public float jumpHeight = ground + 150;

    // set the player sprite (1-3 currently)
    public static int playerNum = 1;

    // bounds of the game frame
    private int rightBound = 1000 - 56;
    private int leftBound = 200;

    // diection the player sprite is facing
    private boolean left = false;

    // Game assets
    private Texture background1;
    private Texture playerSprite1;
    private Texture playerSprite2;
    private Texture playerSprite3;
    private Texture enemySprite1;

    // Player container
    private Rectangle player;

    // Enemies
    private Rectangle enemy;
    public Array<Rectangle> enemies;
    public IntArray enemiesType;
    private int enemyIterator = 0;

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
        
        // load the pre-set assets for selection
        playerSprite1 = new Texture(Gdx.files.internal("player.png"));
        playerSprite2 = new Texture(Gdx.files.internal("player2.png"));
        playerSprite3 = new Texture(Gdx.files.internal("player3.png"));
        enemySprite1 = new Texture(Gdx.files.internal("enemy1.png"));
        background1 = new Texture(Gdx.files.internal("bg1.png"));
        sound1 = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
        bgm1 = Gdx.audio.newMusic(Gdx.files.internal("bgm1.ogg"));

        // Store enemies
        enemies = new Array<Rectangle>();
        enemiesType = new IntArray();

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
        changePlayer(1);

    }

    // create a Rectangle to logically represent the player
    public Rectangle changePlayer(int num) {
        player = new Rectangle();
        player.x = x;
        player.y = y;
        player.width = 56;
        player.height = 80;
        // swaps between the pre-set player sprites
        switch (num) {
            case 1:
                playerNum = 1;
                prefs.putInteger("player", 1);
                break;
            case 2:
                playerNum = 2;
                prefs.putInteger("player", 2);
                break;
            case 3:
                playerNum = 3;
                prefs.putInteger("player", 3);
        }
        return player;
    }

    public Rectangle addEnemy(int num, int xPos, int yPos) {
        enemy = new Rectangle();
        enemy.x = xPos;
        enemy.y = yPos;
        enemy.width = 56;
        enemy.height = 80;
        // swaps between the pre-set enemy sprites
        switch (num) {
            case 1:
                enemies.add(enemy);
                enemiesType.add(1);
                break;
        }
        return enemy;
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
        batch.draw(background1, leftBound, 120);

        // draw the set player sprite at current location
        // monstrous method call but it's necessary for a simple texture flip
        switch (playerNum) {
            case 1:
                batch.draw(playerSprite1, player.x, player.y, player.width, player.height, 0, 0, (int) player.width, (int) player.height, left, false);
                break;
            case 2:
                batch.draw(playerSprite2, player.x, player.y, player.width, player.height, 0, 0, (int) player.width, (int) player.height, left, false);
                break;
            case 3:
                batch.draw(playerSprite3, player.x, player.y, player.width, player.height, 0, 0, (int) player.width, (int) player.height, left, false);
        }

        // render enemies by iterating through list and checking corresponding enemyType array
        enemyIterator = 0;
        for (Rectangle currEnemy : enemies) {
            if (enemiesType.get(enemyIterator) == 1) {
                batch.draw(enemySprite1, currEnemy.x, currEnemy.y);
            }
            enemyIterator++;
        }
        batch.end();

        // Player movement at set speed
        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            player.x -= hSpeed * Gdx.graphics.getDeltaTime();
            left = true;
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            player.x += hSpeed * Gdx.graphics.getDeltaTime();
            left = false;
        }

        // Player jump
        if (Gdx.input.isKeyJustPressed(Keys.SPACE) || Gdx.input.isKeyJustPressed(Keys.W)) {
            if (jumpReady == true) {
                playSound(1);
                jumpReady = false;
                jumping = true;
            }
        }
        if (jumping && !jumpDone) {
            player.y += vSpeed * Gdx.graphics.getDeltaTime();
        }
        if (player.y >= jumpHeight) {
            jumpDone = true;
        }
        if (player.y > ground && jumpDone) {
            player.y -= vSpeed * Gdx.graphics.getDeltaTime();
        }
        if (player.y <= ground && jumping) {
            player.y = ground;
            jumping = false;
            jumpReady = true;
            jumpDone = false;
        }

        // Various buttons to test functionalites
        // swap player sprite
        if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
            changePlayer(1);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
            changePlayer(2);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_3)) {
            changePlayer(3);
        }

        // set music + play
        if (Gdx.input.isKeyJustPressed(Keys.M)) {
            changeMusic(1);
        }

        // set sound + play
        if (Gdx.input.isKeyJustPressed(Keys.S)) {
            playSound(1);
        }

        // save editor state (only saves player sprite atm)
        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            prefs.flush();
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
            addEnemy(1, Gdx.input.getX(), (int) ground);
        }

        // render circles
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Circle circle : circles) {
            shapeRenderer.setColor(0, 0, 0, 1);
            shapeRenderer.circle(circle.x, circle.y, 10);
        }
        shapeRenderer.end();

        // erase circles and enemies
        if (Gdx.input.isKeyJustPressed(Keys.R)) {
            circles.clear();
            enemies.clear();
            enemiesType.clear();
        }

        // User text input (currently does nothing)
        if (Gdx.input.isKeyJustPressed(Keys.B)) {
            Gdx.input.getTextInput(new TextInputListener() {
                @Override
                public void input(String text) {
                }

                @Override
                public void canceled() {
                }
            }, "Test", "Test", "Test");
            node = new Node();
        }

        // make sure the player stays within the screen bounds
        if (player.x < leftBound) {
            player.x = leftBound;
        }
        if (player.x > rightBound) {
            player.x = rightBound;
        }

        // update player location
        x = player.x;
        y = player.y;
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
}
