/*
 Game testing - WASD / arrows to move, space to jump, 1-3 to change player sprite, 
 M to set and play BGM, S to play a sound, P to save stuff, click to draw a circle 
 at the mouse position, E to spawn an enemy on the ground under the mouse.

 *'O' will start the first spawned enemy on patrol and 'I' will stop the
 patrol, assuming an enemy is spawned. B will randomly swap between backgrounds,

**NEW - 0 will save an entire scene (for the most part), which will be loaded with 
Load Project **

 Lots of this needs to be hooked into the UI, among other things. 
 - Trevor
 */
package com.mygdx.game;

//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;

//import javax.swing.JFrame;

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

import com.mygdx.game.Callable;


public class VProgEngine extends ApplicationAdapter {

    public static int WIDTH = 800, HEIGHT = 600;

    // global vars
    private String name;
    private String reloadName;
    private boolean reloading = false;
    private boolean frozen = true;

    // TODO 2015-04-22: This should NOT be static. But external classes link to
    // it. Should not be thus coupled. Will fix later.
    public static Preferences prefs;

    // Asset Management -related variables.
    private AssetManager manager;
    public String assetLocation;
    public QueuedAssetChaperone queuedAssetChaperone;

    // Game assets
    public Array<Texture> backgrounds;
    public Array<Texture> playerSprites;
    public Array<Texture> enemySprites;

    // Player
    public Player playerInstance;

    // Enemies
    private Rectangle enemy;
    public Array<Enemy> enemies;

    // Environment
    public int ground = 36;
    public int bgIndex = 0;

    // Text
    private BitmapFont text;

    // Used to insert a circle at mouse position
    // also a crude form of drawing
    private ShapeRenderer shapeRenderer;
    public Array<Circle> circles;

    // Audio assets
    private Array<Sound> sounds;
    private Array<Music> bgms;

    // The infamous node. If only we knew how to use it.
    private Node node;

    // bounds of the game frame
    public int leftBound = 0;
    public int rightBound = WIDTH;
    SpriteBatch batch;
    Texture img;

    Doodad doodad;
    
    // hook Main UI frame to engine
    Callable callback;

    // This top constructor is valid, because you can then setName to pick your
    // instance name, but presently I can't think of a good reason to use it.
    public VProgEngine() {
    }

    public VProgEngine(String instanceName, Callable caller) {
        this.setName(instanceName);
        callback = caller;
    }

    @Override
    public void create() {

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        manager = new AssetManager();
        queuedAssetChaperone = new QueuedAssetChaperone(manager);

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
        backgrounds.add(new Texture(Gdx.files.internal("bg5.png")));
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
        
        // load up data structures after initializing all of them
        this.loadEnginePrefs();

        Doodad.setBatch(batch);
        doodad = new Doodad(50, 50, 50, 50, enemySprites.get(1));
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
        // If manager still has queued assets...
        int queuedAssets = manager.getQueuedAssets();
        if (queuedAssets != 0) {
            // ..then keep loading them, and if done loading any assets...
            if (manager.update() || queuedAssets > manager.getQueuedAssets()) {
                // ..then check after the update for anything the chaperone had
                // to do for each finished one.
                queuedAssetChaperone.check();
            }
        }
        // In the long run we will want this AFTER all of the 'draw' updates and
        // AFTER all of the editor-level code (e.g. if we let the user drag/drop
        // in the game UI as part of editing, as opposed to as part of play).
        // But then we want this BEFORE any in-game game mechanics state changes
        // such as the game-object primitives' move() and collide() calls, so
        // that even as the "game" (libGDX application) is running, the game of
        // the user is "frozen".
        if (frozen) {
            return;
        }
        
        if (reloading) {
            reloading = false;
            Gdx.graphics.setTitle(reloadName);
        }

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // basic UI
        batch.begin();

        // draw assets
        // draw the background
        batch.draw(backgrounds.get(bgIndex), leftBound, 0);

        // draw the set player sprite at current location
        // monstrous method call but it's necessary for a simple texture flip
        if (playerInstance.playerSpriteIndex >= 0 && playerInstance.playerSpriteIndex <= 2) {
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
            if (playerInstance.x >= currEnemy.x - currEnemy.width / 1.25 && playerInstance.x <= currEnemy.x + currEnemy.width / 1.25 && playerInstance.y <= currEnemy.y + currEnemy.height / 1.5) {
                text.draw(batch, "OW", WIDTH / 2, HEIGHT / 1.25f);
            }
        }
        doodad.draw();
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
        if (Gdx.input.isKeyJustPressed(Keys.SPACE)
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
        
        // save scene
        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            this.saveEnginePrefs();
        }
        
        // debug key
        if (Gdx.input.isKeyJustPressed(Keys.CONTROL_RIGHT)) {
            Gdx.graphics.setTitle(reloadName);
        }

        /* Add a circle to the circles array at the mouse pos on left-click 
         Use Gdx.input.isButtonPressed(Input.Buttons.LEFT) or use event 
         handling with touchDown for a more discrete capture so only 
         one circle is drawn at a time which is probably better in this
         case. */
        if (Gdx.input.isButtonPressed(Keys.LEFT)) {
            //Vector2 touchPos = new Vector2();
            //touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            // ShapeRenderer has an unintuitive way of using the Y value
            Circle newCircle = new Circle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY() + 5, 10);
            circles.add(newCircle);
        }

        // spawn a random enemy on the ground
        if (Gdx.input.isKeyJustPressed(Keys.E)) {
            int enemType = new Random().nextInt(2);
            addEnemy(enemType, (int) Gdx.input.getX(), (int) ground, 0, 0, 0, 150);
        }

        // set first enemy to patrolling
        if (Gdx.input.isKeyJustPressed(Keys.O)) {
            if (enemies.size > 0) {
                Enemy e = enemies.get(0);
                e.SetPatrolPoints((int)e.x - 100, (int)e.x + 100);
            }
        }

        // stop  first enemy from patrolling
        if (Gdx.input.isKeyJustPressed(Keys.I)) {
            if (enemies.size > 0) {
                enemies.get(0).stopPatrol();
            }
        }
        
        // save the scene
        if (Gdx.input.isKeyJustPressed(Keys.NUM_0)) {
            saveScene();
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
            for (int i = 1; prefs.contains("Enemy" + String.valueOf(i)); i++) {
                prefs.remove("Enemy" + String.valueOf(i));
                System.out.println("Enemy" + String.valueOf(i));
            }
            saveEnginePrefs();
        }

        // Change background
        if (Gdx.input.isKeyJustPressed(Keys.B)) {
            if (backgrounds.size == 4) {
                bgIndex = new Random().nextInt(4);
            } else {
                bgIndex = new Random().nextInt(5);
            }
        }

        // make sure the player stays within the screen bounds
        if (playerInstance.x < leftBound) {
            playerInstance.x = leftBound;
        }
        if (playerInstance.x > rightBound) {
            playerInstance.x = rightBound;
        }
    }
    
    // add patrol behavior to a specific enemy
    public void addPatrol(int enemy) {
        if (enemies.size > enemy) {
                Enemy e = enemies.get(enemy);
                e.SetPatrolPoints((int)e.x - 100, (int)e.x + 100);
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
        for (Texture e : playerSprites) {
            e.dispose();
        }
        for (Texture e : enemySprites) {
            e.dispose();
        }
        for (Texture e : backgrounds) {
            e.dispose();
        }
        for (Sound e : sounds) {
            e.dispose();
        }
        for (Music e : bgms) {
            e.dispose();
        }
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
    public void run() {
        this.frozen = false;
    }

    public void freeze() {
        this.frozen = true;
    }
    
    public void reloadApp(String newName) {
        reloading = true;
        reloadName = newName;
        circles.clear();
        enemies.clear();
        prefs = Gdx.app.getPreferences(newName);
        // load saved enemies into the scene
        String enemyBuffer;
        int[] enemyArr = new int[7];
        for (int i = 1; !(prefs.getString("Enemy" + i, "").equals("")); i++) {
            int j = 0;
            enemyBuffer = prefs.getString("Enemy" + i);
            for (String retval : enemyBuffer.split(";")) {
                if (retval.equals("false"))
                    enemyArr[j] = 0;
                else if (retval.equals("true"))
                    enemyArr[j] = 1;
                else
                    enemyArr[j] = Integer.parseInt(retval);
                j++;
            }
            addEnemy(enemyArr[0], enemyArr[1], enemyArr[2], enemyArr[3], enemyArr[4], enemyArr[5], enemyArr[6]);
        }
        // load the player in the scene
        if (prefs.getString("player", "").equals("")) {
            playerInstance = new Player(0);
        } else {
            int j = 0;
            String playerBuffer = prefs.getString("player");
            int[] playerArr = new int[6];
            for (String retval : playerBuffer.split(";")) {
                if (retval.equals("false"))
                    playerArr[j] = 0;
                else if (retval.equals("true"))
                    playerArr[j] = 1;
                else
                    playerArr[j] = Integer.parseInt(retval);
                j++;
            }
            playerInstance = new Player(playerArr[0], playerArr[1], playerArr[2], playerArr[3], playerArr[4], playerArr[5]);
        }
        
        // load the background
        if (prefs.getInteger("bg", -1) != -1)
            bgIndex = prefs.getInteger("bg");
        
        // send loading finished signal
        callback.call("load");
    }

    // save editor state
    public void saveEnginePrefs() {
        prefs.flush();
    }

    // add an enemy from the preferences
    public int addEnemy(int etype, int pos, int ground, int pp1, int pp2, int pat, int speed) {
        enemies.add(new Enemy(etype, pos, ground, pp1, pp2, pat, speed));
        prefs.putString("Enemy" + String.valueOf(enemies.size), String.valueOf(etype) + ";" + String.valueOf(pos) + ";" + String.valueOf(ground) + ";" + String.valueOf(pp1) + ";" + String.valueOf(pp2) + ";" + String.valueOf(pat) + ";" + String.valueOf(speed));
        saveEnginePrefs();
        // return index of added enemy
        return enemies.size - 1;
    }
    
    // save the scene (enemies, player, and background atm)
    public void saveScene() {
        int i = 1;
        for (Enemy e : enemies) {
            prefs.putString("Enemy" + String.valueOf(i), String.valueOf(e.eType) + ";" + String.valueOf((int)e.x) + ";" + String.valueOf((int)e.y) + ";" + String.valueOf(e.leftPatPoint) + ";" + String.valueOf(e.rightPatPoint) + ";" + String.valueOf(e.patrolling) + ";" + String.valueOf(e.hSpeed));
            i++;
        }
        prefs.putString("player", String.valueOf(playerInstance.playerSpriteIndex) + ";" + String.valueOf((int)playerInstance.x) + ";" + String.valueOf((int)playerInstance.y) + ";" + String.valueOf((int)playerInstance.hSpeed) + ";" + String.valueOf((int)playerInstance.vSpeed) + ";" + String.valueOf((int)playerInstance.jumpHeight));
        prefs.putInteger("bg", bgIndex);
        prefs.flush();
        }

    
    // Loads the scene information for the opened project using its preferences file
    public void loadEnginePrefs() {
        // load project's prefs
        prefs = Gdx.app.getPreferences(name);
        
        // load saved enemies into the scene
        String enemyBuffer;
        int[] enemyArr = new int[7];
        for (int i = 1; !(prefs.getString("Enemy" + i, "").equals("")); i++) {
            int j = 0;
            enemyBuffer = prefs.getString("Enemy" + i);
            for (String retval : enemyBuffer.split(";")) {
                if (retval.equals("false"))
                    enemyArr[j] = 0;
                else if (retval.equals("true"))
                    enemyArr[j] = 1;
                else
                    enemyArr[j] = Integer.parseInt(retval);
                j++;
            }
            addEnemy(enemyArr[0], enemyArr[1], enemyArr[2], enemyArr[3], enemyArr[4], enemyArr[5], enemyArr[6]);
            
        }
        
        // load the player in the scene
        if (prefs.getString("player", "").equals("")) {
            playerInstance = new Player(0);
        } else {
            int j = 0;
            String playerBuffer = prefs.getString("player");
            int[] playerArr = new int[6];
            for (String retval : playerBuffer.split(";")) {
                if (retval.equals("false"))
                    playerArr[j] = 0;
                else if (retval.equals("true"))
                    playerArr[j] = 1;
                else
                    playerArr[j] = Integer.parseInt(retval);
                j++;
            }
            playerInstance = new Player(playerArr[0], playerArr[1], playerArr[2], playerArr[3], playerArr[4], playerArr[5]);
        }
        rightBound -= playerInstance.width;
        
        // load the background
        if (prefs.getInteger("bg", -1) != -1)
            bgIndex = prefs.getInteger("bg");
        
        callback.call("load");
    }

    public void setName(String newName) {
        name = newName;
    }

    public void loadTexture(String filepath) {
        // This just starts the queuing - the bulk of the work is done by the
        // QueuedAssetChaperone and the callback assigned it it.
        manager.load(filepath, Texture.class);
    }
    
    public void closeGame() {
        Gdx.app.exit();
    }
}
