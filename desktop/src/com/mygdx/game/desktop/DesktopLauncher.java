package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.vprog;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class DesktopLauncher {
    // Swing is not thread safe, so it is "good practice" to move all Swing
    // commands out of main, into a function that is run by the event dispatcher
    // thread once it's actually safe.
    private static void startSwingGUI() {
        openTestWindow(50, 50, 200, 200);
    }
    
    private static void getScreenBounds() {}
    
    private static void openTestWindow(int x, int y, int width, int height) {
        String labelText = x + " " + y + " " + width + " " + height;
        JLabel testJLabel = new JLabel(labelText);
	
        JFrame testJFrame = new JFrame("Test JFrame");
        testJFrame.setBounds(x, y, width, height);
        testJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	testJFrame.getContentPane().add(testJLabel);
        testJFrame.setVisible(true);
    }
    
    public static void main (String[] arg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startSwingGUI();
            }
        });
        LwjglApplicationConfiguration config
            = new LwjglApplicationConfiguration();
        config.title = "VProg2D";
        config.height = 720;
        config.width = 1280;
        config.resizable = false;
        //new LwjglApplication(new vprog(), config);
    }
}
