package com.mygdx.game.desktop;

import java.awt.event.ActionListener;
import javax.swing.JMenu;

class JMenuHelper
{
    private JMenu jMenu;
    
    public JMenuHelper(String name)
    {
        jMenu = new JMenu(name);
    }
    
    public JMenu jMenu()
    {
        return jMenu;
    }
    
    public void add(String name, String iconFilename)
    {
        jMenu.add(JMenuItemHelper.newJMenuItem(name, iconFilename));
    }
    
    public void add(String name, ActionListener action)
    {
        jMenu.add(JMenuItemHelper.newJMenuItem(name, action));
    }
    
    public void add(String name, String iconFilename, ActionListener action)
    {
        jMenu.add(JMenuItemHelper.newJMenuItem(name, iconFilename, action));
    }
}
