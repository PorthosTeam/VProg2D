/**
 * 
 */
package com.mygdx.game;

import java.util.ArrayList;

/**
 * @description Stores the project's title, an array of all existing nodes
 *              within the project, global variables relating to the project,
 *              and the associated JDK for the the game.
 * 
 * @author Trevor Richardson
 * @date Mar. 22th 2015
 * @module CreateProject
 */
public class Project {
	
	private String title;
	private ArrayList<Node> nodes;
	private ArrayList<String> globalVars;
	private JDK jdk;	

	/**
	 * 
	 */
	public Project() {
		// TODO Auto-generated constructor stub
	}
	
	public void setTitle(String _title) {
		title = _title;
	}

	public String getTitle() {
		return title;
	}
	
	public void addGlobalVar(String _var) {
		globalVars.add(_var);
	}

	public ArrayList<String> getGlobalVars() {
		return globalVars;
	}
	
	public void addNode(Node _node) {
		nodes.add(_node);
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public void setJDK(JDK _jdk) {
		jdk = _jdk;
	}

	public JDK getJDK() {
		return jdk;
	}
	
	public void export() {
		// TBD, need interface from other module
	}
	
	public void compile(String _file) {
		// TBD, need interface from other module
	}

}
