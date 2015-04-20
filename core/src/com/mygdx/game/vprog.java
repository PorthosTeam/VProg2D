package com.mygdx.game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Preferences;

public class vprog extends ApplicationAdapter {

    // Test create project module
    private boolean CREATE_DEBUG = false;
    
    // global vars
    public static Preferences prefs;
    public static int hSpeed = 300;
    public static float x = 300, y = 156;
    public static int playerNum = 1;
    private int rightBound = 1000;
    private int leftBound = 200;
    private boolean right = true;

    // UI assets
    private Texture logo;
    private Texture construct;
    private Texture createProject;
    private Texture loadProject;
    private Texture runProgram;
    private Texture saveProject;
    private Texture test;
    private Texture uploadAsset;

    private Texture background1;
    private Texture playerSprite1;
    private Texture playerSprite2;
    private Texture playerSprite3;
    private Rectangle player;
    
    private Sound sound1;
    private Music bgm1;
    
    private Node node;
    

    public static int WIDTH = 800, HEIGHT = 600;
    SpriteBatch batch;
    Texture img;

    @Override
    public void create() {
        // save info @ %UserProfile%/.prefs/My Preferences (~/.prefs/My Preferences in UNIX)
        prefs = Gdx.app.getPreferences("savestate");
        
        batch = new SpriteBatch();
        logo = new Texture(Gdx.files.internal("logo.png"));
        construct = new Texture(Gdx.files.internal("construct.png"));
        createProject = new Texture(Gdx.files.internal("createProject.png"));
        loadProject = new Texture(Gdx.files.internal("loadProject.png"));
        runProgram = new Texture(Gdx.files.internal("runProgram.png"));
        saveProject = new Texture(Gdx.files.internal("saveProject.png"));
        test = new Texture(Gdx.files.internal("test.png"));
        uploadAsset = new Texture(Gdx.files.internal("uploadAsset.png"));
        
        playerSprite1 = new Texture(Gdx.files.internal("player.png"));
        playerSprite2 = new Texture(Gdx.files.internal("player2.png"));
        playerSprite3 = new Texture(Gdx.files.internal("player3.png"));
        background1 = new Texture(Gdx.files.internal("bg1.png"));
        
        sound1 = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
        bgm1 = Gdx.audio.newMusic(Gdx.files.internal("bgm1.ogg"));
        // img = new Texture("badlogic.jpg");
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
        Rectangle panel = new Rectangle();
        panel.height = Gdx.graphics.getHeight();
        panel.width = Gdx.graphics.getWidth() / 10;
        
        changePlayer(1);

    }

    // create a Rectangle to logically represent the player
    public Rectangle changePlayer(int num) {
        player = new Rectangle();
        player.x = x;
        player.y = y;
        player.width = 56;
        player.height = 80;
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
    
    public void changeMusic(int num) {
        switch (num) {
            case 1:
                bgm1.setLooping(true);
                bgm1.play();
        }
    }
    
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
        // batch.draw(img, 0, 0);
        batch.draw(logo, Gdx.graphics.getWidth() / 2 - logo.getWidth() / 2, Gdx.graphics.getHeight() - logo.getHeight());
        batch.draw(saveProject, 10, Gdx.graphics.getHeight() - createProject.getHeight() * 9);
        batch.draw(loadProject, 10, Gdx.graphics.getHeight() - createProject.getHeight() * 7);
        batch.draw(createProject, 10, Gdx.graphics.getHeight() - createProject.getHeight() * 5);
        batch.draw(construct, 300, 10);
        batch.draw(runProgram, 600, 10);
        batch.draw(test, 900, 10);
        batch.draw(background1, 200, 120);
        switch (playerNum) {
            case 1:
                batch.draw(playerSprite1, player.x, player.y);
                break;
            case 2:
                batch.draw(playerSprite2, player.x, player.y);
                break;
            case 3:
                batch.draw(playerSprite3, player.x, player.y);
        }
        batch.end();
        
      if(Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
          player.x -= hSpeed * Gdx.graphics.getDeltaTime();
      }
      if(Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
          player.x += hSpeed * Gdx.graphics.getDeltaTime();
      }
      
      if(Gdx.input.isKeyPressed(Keys.NUM_1)) {
          changePlayer(1);
      }
      if(Gdx.input.isKeyPressed(Keys.NUM_2)) {
          changePlayer(2);
      }
      if(Gdx.input.isKeyPressed(Keys.NUM_3)) {
          changePlayer(3);
      }
      if(Gdx.input.isKeyJustPressed(Keys.M)) {
          changeMusic(1);
      }
      if(Gdx.input.isKeyJustPressed(Keys.S)) {
          playSound(1);
      }
      if(Gdx.input.isKeyJustPressed(Keys.P)) {
          prefs.flush();
      }
      if (Gdx.input.isKeyJustPressed(Keys.B)) {
			Gdx.input.getTextInput(new TextInputListener() {
				@Override
				public void input (String text) {
				}

				@Override
				public void canceled () {
				}
			}, "Test", "Test", "Test");
                        node = new Node();
		}

      // make sure the player stays within the screen bounds
      if(player.x < leftBound) player.x = leftBound;
      if(player.x > rightBound) player.x = rightBound;
      
      x = player.x;
      y = player.y;
    }
    
    @Override
   public void dispose() {
      // dispose of all the native resources
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
