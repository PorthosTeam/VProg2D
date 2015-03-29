package com.mygdx.game;

/**
 * @description Stores the JDK version number and the directory to reach the
 *              Java Development Kit for compilation and execution.
 * 
 * @author Trevor Richardson
 * @date Mar. 22 2015
 * @module CreateProject
 */
public class JDK {
	
	private String version;
	private String directory;

	// Init JDK variables
	public JDK(String _directory, String _version) {
		directory = _directory;
		version = _version;
	}
	
	// Getters/Setters
	public void setVersion(String _version) {
		version = _version;
	}

	public String getVersion() {
		return version;
	}
	
	public void setDirectory(String _directory) {
		directory = _directory;
	}

	public String getDirectory() {
		return directory;
	}

}
