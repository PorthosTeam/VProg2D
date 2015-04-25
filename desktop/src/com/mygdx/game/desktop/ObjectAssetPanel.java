package com.mygdx.game.desktop;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Player;
import com.mygdx.game.Enemy;
import javax.swing.SpringLayout;

public class ObjectAssetPanel extends JPanel {

    public String objectName;
    public JPanel parentPanel;
    private String type;
    private int enemyNum;
    int soundSelection;

    public ObjectAssetPanel(String name, String t, int eNum) {
        super();
        type = t;
        enemyNum = eNum - 1;
        soundSelection = 0;
        setLayout(new BorderLayout(0, 0));
        setMaximumSize(new Dimension(200, 29));

        JButton objBtn = new JButton(name);
        objBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        objBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Edit Object");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                // Check object type
                if (type.equals("player")) {
                    final Player p = MainUI.vprog.playerInstance;

                    // name
                    JPanel playerPanel = new JPanel(new SpringLayout());
                    playerPanel.add(new JLabel("Name: "));
                    JTextField name = new JTextField(16);
                    name.setText("Player");
                    playerPanel.add(name);

                    // sprites
                    playerPanel.add(new JLabel("Sprite: "));
                    String[] spriteList = {"GNU", "Linux", "Android"};
                    JComboBox comboBox = new JComboBox();
                    int count = 0;
                    for (int i = 0; i < spriteList.length; i++) {
                        comboBox.addItem(spriteList[count++]);
                    }
                    comboBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (((String) ((JComboBox) e.getSource()).getSelectedItem()).equals("GNU")) {
                                p.playerSpriteIndex = 0;
                            } else if (((String) ((JComboBox) e.getSource()).getSelectedItem()).equals("Linux")) {
                                p.playerSpriteIndex = 1;
                            } else if (((String) ((JComboBox) e.getSource()).getSelectedItem()).equals("Android")) {
                                p.playerSpriteIndex = 2;
                            }
                        }
                    });
                    playerPanel.add(comboBox);

                    // x pos
                    playerPanel.add(new JLabel("Position (X): "));
                    final JTextField x = new JTextField(10);
                    x.setText(String.valueOf((int) p.x));
                    x.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            x.setText((String) ((JTextField) e.getSource()).getText());
                            p.x = Integer.parseInt(x.getText());
                        }
                    });
                    playerPanel.add(x);

                    // y pos
                    playerPanel.add(new JLabel("Position (Y): "));
                    final JTextField y = new JTextField(10);
                    y.setText(String.valueOf((int) p.y));
                    y.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            y.setText((String) ((JTextField) e.getSource()).getText());
                            p.y = Integer.parseInt(y.getText());
                        }
                    });
                    playerPanel.add(y);

                    // hSpeed
                    playerPanel.add(new JLabel("Run Speed: "));
                    final JTextField run = new JTextField(10);
                    run.setText(String.valueOf((int) p.hSpeed));
                    run.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            run.setText((String) ((JTextField) e.getSource()).getText());
                            p.hSpeed = Integer.parseInt(run.getText());
                        }
                    });
                    playerPanel.add(run);

                    // vSpeed
                    playerPanel.add(new JLabel("Gravity: "));
                    final JTextField grav = new JTextField(10);
                    grav.setText(String.valueOf((int) p.vSpeed));
                    grav.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            grav.setText((String) ((JTextField) e.getSource()).getText());
                            p.vSpeed = Integer.parseInt(grav.getText());
                        }
                    });
                    playerPanel.add(grav);

                    // jHeight
                    playerPanel.add(new JLabel("Jump Height: "));
                    final JTextField jump = new JTextField(10);
                    jump.setText(String.valueOf((int) p.jumpHeight));
                    jump.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            jump.setText((String) ((JTextField) e.getSource()).getText());
                            p.jumpHeight = Integer.parseInt(jump.getText());
                        }
                    });
                    playerPanel.add(jump);
                    SpringUtilities.makeCompactGrid(playerPanel,
                            7, 2, //rows, cols
                            6, 6, //initX, initY
                            12, 12);       //xPad, yPad
                    playerPanel.setOpaque(true); //content panes must be opaque
                    frame.setContentPane(playerPanel);
                    frame.pack();

                } else if (type.equals("bg")) {
                    JPanel bgPanel = new JPanel(new SpringLayout());
                    int size = MainUI.vprog.backgrounds.size;
                    Array<Integer> bgs = new Array<Integer>();
                    for (int i = 1; i <= size; ++i) {
                        bgs.add(i);
                    }
                    Integer[] bgsArray = bgs.toArray(Integer.class);
                    bgPanel.add(new JLabel("Background: "));
                    JComboBox comboBox = new JComboBox();
                    int count = 0;
                    for (int i = 0; i < bgsArray.length; i++) {
                        comboBox.addItem(bgsArray[count++]);
                    }
                    comboBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainUI.vprog.bgIndex = ((Integer) ((JComboBox) e.getSource()).getSelectedItem()) - 1;
                        }
                    });
                    bgPanel.add(comboBox);
                    
                    SpringUtilities.makeCompactGrid(bgPanel,
                            1, 2, //rows, cols
                            6, 6, //initX, initY
                            12, 120);       //xPad, yPad
                    bgPanel.setOpaque(true); //content panes must be opaque
                    frame.setContentPane(bgPanel);
                    frame.pack();

                } else if (type.equals("bgm")) {
                    int size = MainUI.vprog.bgms.size;
                    Array<Integer> bgms = new Array<Integer>();
                    for (int i = 1; i <= size; ++i) {
                        bgms.add(i);
                    }
                    Integer[] bgmsArray = bgms.toArray(Integer.class);
                    JPanel bgmPanel = new JPanel(new SpringLayout());
                    bgmPanel.add(new JLabel("Background Music: "));
                    JComboBox comboBox = new JComboBox();
                    int count = 0;
                    for (int i = 0; i < bgmsArray.length; i++) {
                        comboBox.addItem(bgmsArray[count++]);
                    }
                    comboBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainUI.vprog.setMusic(((Integer) ((JComboBox) e.getSource()).getSelectedItem()) - 1);
                        }
                    });
                    bgmPanel.add(comboBox);
                    JButton playBtn = new JButton("Play");
                    playBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainUI.vprog.playMusic();
                        }
                    });
                    bgmPanel.add(playBtn);
                    JButton stopBtn = new JButton("Stop");
                    stopBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainUI.vprog.stopMusic();
                        }
                    });
                    bgmPanel.add(stopBtn);
                    
                    SpringUtilities.makeCompactGrid(bgmPanel,
                            1, 4, //rows, cols
                            6, 6, //initX, initY
                            12, 12);       //xPad, yPad
                    bgmPanel.setOpaque(true); //content panes must be opaque
                    frame.setContentPane(bgmPanel);
                    frame.pack();

                } else if (type.equals("sounds")) {
                    int size = MainUI.vprog.sounds.size;
                    Array<Integer> sounds = new Array<Integer>();
                    for (int i = 1; i <= size; ++i) {
                        sounds.add(i);
                    }
                    Integer[] soundsArray = sounds.toArray(Integer.class);
                    JPanel soundPanel = new JPanel(new SpringLayout());
                    soundPanel.add(new JLabel("Sound: "));
                    JComboBox comboBox = new JComboBox();
                    int count = 0;
                    for (int i = 0; i < soundsArray.length; i++) {
                        comboBox.addItem(soundsArray[count++]);
                    }
                    comboBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            soundSelection = ((Integer) ((JComboBox) e.getSource()).getSelectedItem()) - 1;
                        }
                    });
                    soundPanel.add(comboBox);

                    JButton playBtn = new JButton("Play");
                    playBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainUI.vprog.playSound(soundSelection);
                        }
                    });
                    soundPanel.add(playBtn);
                    JButton stopBtn = new JButton("Stop");
                    stopBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainUI.vprog.stopSound(soundSelection);
                        }
                    });
                    soundPanel.add(stopBtn);
                    JButton jumpBtn = new JButton("Set as Jump FX");
                    jumpBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainUI.vprog.playerInstance.setJumpFX(soundSelection);
                        }
                    });
                    soundPanel.add(jumpBtn);
                    
                    SpringUtilities.makeCompactGrid(soundPanel,
                            1, 5, //rows, cols
                            6, 6, //initX, initY
                            12, 12);       //xPad, yPad
                    soundPanel.setOpaque(true); //content panes must be opaque
                    frame.setContentPane(soundPanel);
                    frame.pack();
                    
                } else if (type.equals("enemy")) {
                    final Enemy selectedEnemy = MainUI.vprog.enemies.get(enemyNum);

                    // name
                    JPanel enemyPanel = new JPanel(new SpringLayout());
                    enemyPanel.add(new JLabel("Name: "));
                    JTextField name = new JTextField(16);
                    name.setText("Enemy" + String.valueOf(enemyNum + 1));
                    enemyPanel.add(name);

                    // sprite
                    enemyPanel.add(new JLabel("Sprite: "));
                    String[] spriteList = {"Pacman", "Kisi"};
                    JComboBox comboBox = new JComboBox();
                    int count = 0;
                    for (int i = 0; i < spriteList.length; i++) {
                        comboBox.addItem(spriteList[count++]);
                    }
                    comboBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (((String) ((JComboBox) e.getSource()).getSelectedItem()).equals("Pacman")) {
                                selectedEnemy.eType = 0;
                            } else if (((String) ((JComboBox) e.getSource()).getSelectedItem()).equals("Kisi")) {
                                selectedEnemy.eType = 1;
                            }
                        }
                    });
                    enemyPanel.add(comboBox);

                    // x pos
                    enemyPanel.add(new JLabel("Position (X): "));
                    final JTextField x = new JTextField(10);
                    x.setText(String.valueOf((int) selectedEnemy.x));
                    x.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            x.setText((String) ((JTextField) e.getSource()).getText());
                            selectedEnemy.x = Integer.parseInt(x.getText());
                        }
                    });
                    enemyPanel.add(x);

                    // y pos
                    enemyPanel.add(new JLabel("Position (Y): "));
                    final JTextField y = new JTextField(10);
                    y.setText(String.valueOf((int) selectedEnemy.y));
                    y.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            y.setText((String) ((JTextField) e.getSource()).getText());
                            selectedEnemy.y = Integer.parseInt(y.getText());
                        }
                    });
                    enemyPanel.add(y);

                    // hSpeed
                    enemyPanel.add(new JLabel("Move Speed: "));
                    final JTextField run = new JTextField(10);
                    run.setText(String.valueOf((int) selectedEnemy.hSpeed));
                    run.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            run.setText((String) ((JTextField) e.getSource()).getText());
                            selectedEnemy.hSpeed = Integer.parseInt(run.getText());
                        }
                    });
                    enemyPanel.add(run);

                    // patrolling
                    final JTextField pp1 = new JTextField(10);
                    final JTextField pp2 = new JTextField(10);
                    enemyPanel.add(new JLabel("Patrol: "));
                    String[] patChoice = {"On", "Off"};
                    JComboBox patComboBox = new JComboBox();
                    int patCount = 0;
                    for (int i = 0; i < patChoice.length; i++) {
                        patComboBox.addItem(patChoice[patCount++]);
                    }
                    patComboBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (((String) ((JComboBox) e.getSource()).getSelectedItem()).equals("Patrolling")) {
                                selectedEnemy.patrolling = true;
                            } else if (((String) ((JComboBox) e.getSource()).getSelectedItem()).equals("Not Patrolling")) {
                                selectedEnemy.patrolling = false;
                            }
                            if (selectedEnemy.patrolling) {
                                pp1.setEditable(true);
                                pp2.setEditable(true);
                            } else {
                                pp1.setEditable(false);
                                pp2.setEditable(false);
                            }
                        }
                    });
                    if (selectedEnemy.patrolling) {
                        pp1.setEditable(true);
                        pp2.setEditable(true);
                    } else {
                        pp1.setEditable(false);
                        pp2.setEditable(false);
                    }
                    enemyPanel.add(patComboBox);

                    // pp1
                    enemyPanel.add(new JLabel("Patrol Point 1: "));
                    pp1.setText(String.valueOf((int) selectedEnemy.leftPatPoint));
                    pp1.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            pp1.setText((String) ((JTextField) e.getSource()).getText());
                            selectedEnemy.leftPatPoint = Integer.parseInt(pp1.getText());
                        }
                    });
                    enemyPanel.add(pp1);

                    // pp2
                    enemyPanel.add(new JLabel("Patrol Point 2: "));
                    pp2.setText(String.valueOf((int) selectedEnemy.rightPatPoint));
                    pp2.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            pp2.setText((String) ((JTextField) e.getSource()).getText());
                            selectedEnemy.rightPatPoint = Integer.parseInt(pp2.getText());
                        }
                    });
                    enemyPanel.add(pp2);
                    
                    SpringUtilities.makeCompactGrid(enemyPanel,
                            8, 2, //rows, cols
                            6, 6, //initX, initY
                            12, 12);       //xPad, yPad
                    enemyPanel.setOpaque(true); //content panes must be opaque
                    frame.setContentPane(enemyPanel);
                    frame.pack();

                }
                frame.setVisible(true);
            }
        });

        add(objBtn, BorderLayout.NORTH);

        LineBorder border = new LineBorder(null);
        setBorder(border);
    }
}
