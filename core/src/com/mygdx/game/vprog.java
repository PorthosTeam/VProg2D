package com.mygdx.game;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class vprog extends ApplicationAdapter {
	
	// Test create project module
	private boolean CREATE_DEBUG = true;
	
	public static int WIDTH = 800, HEIGHT = 600;
	SpriteBatch batch;
	Texture img;

	@Override
	public void create() {
		batch = new SpriteBatch();
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
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		// batch.draw(img, 0, 0);
		batch.end();
	}
}
