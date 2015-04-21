package com.mygdx.game.desktop;

// This is pretty much a just really thin wrapper around the actual UI. Setup of
// the program as a whole would probably go here if it got more advanced - e.g.
// loading program-wide configuration, etc. But currently there's nothing to
// really 'do' in this regard.

public class DesktopLauncher {
    // Swing is not thread safe, so it is "good practice" to move all Swing
    // commands out of main, into a function that is run by the event dispatcher
    // thread once it's actually safe.
    private static void startSwingGUI() {
        new MainUI();
    }

    public static void main (String[] arg) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startSwingGUI();
            }
        });
    }
}
