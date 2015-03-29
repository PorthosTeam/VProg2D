package com.mygdx.game;

import java.util.ArrayList;

/**
 * @description Stores the User's name and an array of all projects belonging to
 *              the User.
 * 
 * @author Trevor Richardson
 * @date Mar. 22 2015
 * @module CreateProject
 */
public class User {

	private String name;
	private ArrayList<Project> projects;

	// Init user
	public User(String _name) {
		name = _name;
	}
	
	public User() {}

	// Getters/Setters
	public void setName(String _name) {
		name = _name;
	}

	public String getName() {
		return name;
	}

	public void addProject(Project _project) {
		projects.add(_project);
	}

	/* returns the actual Projects and not a copy */
	public ArrayList<Project> getProjects() {
		return projects;
	}

}
