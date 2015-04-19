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

public class vprog extends ApplicationAdapter {
	
	// Test create project module
	private boolean CREATE_DEBUG = false;
        
        // UI assets
        private Texture logo;
        private Texture construct;
        private Texture createProject;
        private Texture loadProject;
        private Texture runProgram;
        private Texture saveProject;
        private Texture test;
        private Texture uploadAsset;
	
	public static int WIDTH = 800, HEIGHT = 600;
	SpriteBatch batch;
	Texture img;

	@Override
	public void create() {
		batch = new SpriteBatch();
                logo = new Texture(Gdx.files.internal("logo.png"));
                construct = new Texture(Gdx.files.internal("construct.png"));
                createProject = new Texture(Gdx.files.internal("createProject.png"));
                loadProject = new Texture(Gdx.files.internal("loadProject.png"));
                runProgram = new Texture(Gdx.files.internal("runProgram.png"));
                saveProject = new Texture(Gdx.files.internal("saveProject.png"));
                test = new Texture(Gdx.files.internal("test.png"));
                uploadAsset = new Texture(Gdx.files.internal("uploadAsset.png"));
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
                panel.width = Gdx.graphics.getWidth()/10;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                
                // basic UI
		batch.begin();
		// batch.draw(img, 0, 0);
                batch.draw(logo, Gdx.graphics.getWidth()/2 - logo.getWidth()/2, Gdx.graphics.getHeight() - logo.getHeight());
                batch.draw(saveProject, 10, Gdx.graphics.getHeight() - createProject.getHeight()*9);
                batch.draw(loadProject, 10, Gdx.graphics.getHeight() - createProject.getHeight()*7);
                batch.draw(createProject, 10, Gdx.graphics.getHeight() - createProject.getHeight()*5);
                batch.draw(construct, 300, 10);
                batch.draw(runProgram, 600, 10);
                batch.draw(test, 900, 10);
		batch.end();
	}
}
