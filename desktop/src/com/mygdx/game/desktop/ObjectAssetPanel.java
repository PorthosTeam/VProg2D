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

public class ObjectAssetPanel extends JPanel {

    public String objectName;
    public JPanel parentPanel;
    private String type;
    private int enemyNum;

    public ObjectAssetPanel(String name, String t, int eNum) {
        super();
        type = t;
        enemyNum = eNum - 1;
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
                frame.setLayout(new FlowLayout(FlowLayout.LEFT));
                // Check object type
                if (type.equals("player")) {
                    frame.setSize(new Dimension(250, 300));
                    final Player p = MainUI.vprog.playerInstance;

                    // name
                    JPanel namePanel = new JPanel();
                    namePanel.setAlignmentX(LEFT_ALIGNMENT);
                    namePanel.add(new JLabel("Name: ", JLabel.CENTER));
                    JTextField name = new JTextField(16);
                    name.setText("Player");
                    namePanel.add(name);
                    frame.add(namePanel);

                    // sprite
                    JPanel spritePanel = new JPanel();
                    spritePanel.setAlignmentX(LEFT_ALIGNMENT);
                    spritePanel.add(new JLabel("Sprite: ", JLabel.LEFT));
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
                    spritePanel.add(comboBox);
                    frame.add(spritePanel);

                    // x pos
                    JPanel xPanel = new JPanel();
                    xPanel.setAlignmentX(LEFT_ALIGNMENT);
                    xPanel.add(new JLabel("Position (X): ", JLabel.LEFT));
                    final JTextField x = new JTextField(10);
                    x.setText(String.valueOf((int) p.x));
                    x.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            x.setText((String) ((JTextField) e.getSource()).getText());
                            p.x = Integer.parseInt(x.getText());
                        }
                    });
                    xPanel.add(x);
                    frame.add(xPanel);

                    // y pos
                    JPanel yPanel = new JPanel();
                    yPanel.setAlignmentX(LEFT_ALIGNMENT);
                    yPanel.add(new JLabel("Position (Y): ", JLabel.LEFT));
                    final JTextField y = new JTextField(10);
                    y.setText(String.valueOf((int) p.y));
                    y.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            y.setText((String) ((JTextField) e.getSource()).getText());
                            p.y = Integer.parseInt(y.getText());
                        }
                    });
                    yPanel.add(y);
                    frame.add(yPanel);

                    // hSpeed
                    JPanel hPanel = new JPanel();
                    hPanel.setAlignmentX(LEFT_ALIGNMENT);
                    hPanel.add(new JLabel("Run Speed:    ", JLabel.LEFT));
                    final JTextField run = new JTextField(10);
                    run.setText(String.valueOf((int) p.hSpeed));
                    run.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            run.setText((String) ((JTextField) e.getSource()).getText());
                            p.hSpeed = Integer.parseInt(run.getText());
                        }
                    });
                    hPanel.add(run);
                    frame.add(hPanel);

                    // vSpeed
                    JPanel vPanel = new JPanel();
                    vPanel.setAlignmentX(LEFT_ALIGNMENT);
                    vPanel.add(new JLabel("Gravity:      ", JLabel.LEFT));
                    final JTextField grav = new JTextField(10);
                    grav.setText(String.valueOf((int) p.vSpeed));
                    grav.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            grav.setText((String) ((JTextField) e.getSource()).getText());
                            p.vSpeed = Integer.parseInt(grav.getText());
                        }
                    });
                    vPanel.add(grav);
                    frame.add(vPanel);

                    // jHeight
                    JPanel jPanel = new JPanel();
                    jPanel.setAlignmentX(LEFT_ALIGNMENT);
                    jPanel.add(new JLabel("Jump Height:  ", JLabel.LEFT));
                    final JTextField jump = new JTextField(10);
                    jump.setAlignmentX(RIGHT_ALIGNMENT);
                    jump.setText(String.valueOf((int) p.jumpHeight));
                    jump.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            jump.setText((String) ((JTextField) e.getSource()).getText());
                            p.jumpHeight = Integer.parseInt(jump.getText());
                        }
                    });
                    jPanel.add(jump);
                    frame.add(jPanel);

                } else if (type.equals("bg")) {
                    frame.setSize(new Dimension(150, 175));
                    int size = MainUI.vprog.backgrounds.size;
                    Array<Integer> bgs = new Array<Integer>();
                    for (int i = 1; i <= size; ++i) {
                        bgs.add(i);
                    }
                    Integer[] bgsArray = bgs.toArray(Integer.class);
                    JPanel bgPanel = new JPanel();
                    bgPanel.setAlignmentX(LEFT_ALIGNMENT);
                    bgPanel.add(new JLabel("Background: ", JLabel.LEFT));
                    JComboBox comboBox = new JComboBox();
                    int count = 0;
                    for (int i = 0; i < bgsArray.length; i++) {
                        comboBox.addItem(bgsArray[count++]);
                    }
                    comboBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainUI.vprog.bgIndex = ((Integer) ((JComboBox) e.getSource()).getSelectedItem()) - 1;
                            System.out.println(MainUI.vprog.bgIndex);
                        }
                    });
                    bgPanel.add(comboBox);

                    frame.add(bgPanel);

                } else if (type.equals("bgm")) {
                    frame.setSize(new Dimension(310, 75));
                    int size = MainUI.vprog.bgms.size;
                    Array<Integer> bgms = new Array<Integer>();
                    for (int i = 1; i <= size; ++i) {
                        bgms.add(i);
                    }
                    Integer[] bgmsArray = bgms.toArray(Integer.class);
                    JPanel bgmPanel = new JPanel();
                    bgmPanel.setAlignmentX(LEFT_ALIGNMENT);
                    bgmPanel.add(new JLabel("Background Music: ", JLabel.LEFT));
                    JComboBox comboBox = new JComboBox();
                    int count = 0;
                    for (int i = 0; i < bgmsArray.length; i++) {
                        comboBox.addItem(bgmsArray[count++]);
                    }
                    comboBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainUI.vprog.bgmIndex = ((Integer) ((JComboBox) e.getSource()).getSelectedItem()) - 1;
                        }
                    });
                    bgmPanel.add(comboBox);
                    JButton playBtn = new JButton("Play");
                    playBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
                    playBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainUI.vprog.playMusic();
                        }
                    });
                    bgmPanel.add(playBtn);
                    JButton stopBtn = new JButton("Stop");
                    stopBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
                    stopBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MainUI.vprog.stopMusic();
                        }
                    });
                    bgmPanel.add(stopBtn);
                    frame.add(bgmPanel);

                } else if (type.equals("enemy")) {
                    frame.setSize(new Dimension(250, 350));
                    final Enemy selectedEnemy = MainUI.vprog.enemies.get(enemyNum);

                    // name
                    JPanel namePanel = new JPanel();
                    namePanel.setAlignmentX(LEFT_ALIGNMENT);
                    namePanel.add(new JLabel("Name: ", JLabel.CENTER));
                    JTextField name = new JTextField(16);
                    name.setText("Enemy" + String.valueOf(enemyNum + 1));
                    namePanel.add(name);
                    frame.add(namePanel);

                    // sprite
                    JPanel spritePanel = new JPanel();
                    spritePanel.setAlignmentX(LEFT_ALIGNMENT);
                    spritePanel.add(new JLabel("Sprite: ", JLabel.LEFT));
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
                    spritePanel.add(comboBox);
                    frame.add(spritePanel);

                    // x pos
                    JPanel xPanel = new JPanel();
                    xPanel.setAlignmentX(LEFT_ALIGNMENT);
                    xPanel.add(new JLabel("Position (X): ", JLabel.LEFT));
                    final JTextField x = new JTextField(10);
                    x.setText(String.valueOf((int) selectedEnemy.x));
                    x.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            x.setText((String) ((JTextField) e.getSource()).getText());
                            selectedEnemy.x = Integer.parseInt(x.getText());
                        }
                    });
                    xPanel.add(x);
                    frame.add(xPanel);

                    // y pos
                    JPanel yPanel = new JPanel();
                    yPanel.setAlignmentX(LEFT_ALIGNMENT);
                    yPanel.add(new JLabel("Position (Y): ", JLabel.LEFT));
                    final JTextField y = new JTextField(10);
                    y.setText(String.valueOf((int) selectedEnemy.y));
                    y.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            y.setText((String) ((JTextField) e.getSource()).getText());
                            selectedEnemy.y = Integer.parseInt(y.getText());
                        }
                    });
                    yPanel.add(y);
                    frame.add(yPanel);

                    // hSpeed
                    JPanel hPanel = new JPanel();
                    hPanel.setAlignmentX(LEFT_ALIGNMENT);
                    hPanel.add(new JLabel("Move Speed:    ", JLabel.LEFT));
                    final JTextField run = new JTextField(10);
                    run.setText(String.valueOf((int) selectedEnemy.hSpeed));
                    run.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            run.setText((String) ((JTextField) e.getSource()).getText());
                            selectedEnemy.hSpeed = Integer.parseInt(run.getText());
                        }
                    });
                    hPanel.add(run);
                    frame.add(hPanel);

                    // patrolling
                    final JTextField pp1 = new JTextField(10);
                    final JTextField pp2 = new JTextField(10);
                    JPanel patPanel = new JPanel();
                    patPanel.setAlignmentX(LEFT_ALIGNMENT);
                    patPanel.add(new JLabel("Patrol: ", JLabel.LEFT));
                    String[] patChoice = {"Patrolling", "Not Patrolling"};
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
                    spritePanel.add(patComboBox);
                    frame.add(patPanel);

                    // pp1
                    JPanel pp1Panel = new JPanel();
                    pp1Panel.setAlignmentX(LEFT_ALIGNMENT);
                    pp1Panel.add(new JLabel("Patrol Point 1: ", JLabel.LEFT));
                    pp1.setAlignmentX(RIGHT_ALIGNMENT);
                    pp1.setText(String.valueOf((int) selectedEnemy.leftPatPoint));
                    pp1.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            pp1.setText((String) ((JTextField) e.getSource()).getText());
                            selectedEnemy.leftPatPoint = Integer.parseInt(pp1.getText());
                        }
                    });
                    pp1Panel.add(pp1);
                    frame.add(pp1Panel);

                    // pp1
                    JPanel pp2Panel = new JPanel();
                    pp2Panel.setAlignmentX(LEFT_ALIGNMENT);
                    pp2Panel.add(new JLabel("Patrol Point 2: ", JLabel.LEFT));
                    pp2.setAlignmentX(RIGHT_ALIGNMENT);
                    pp2.setText(String.valueOf((int) selectedEnemy.rightPatPoint));
                    pp2.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            pp2.setText((String) ((JTextField) e.getSource()).getText());
                            selectedEnemy.rightPatPoint = Integer.parseInt(pp2.getText());
                        }
                    });
                    pp2Panel.add(pp2);
                    frame.add(pp2Panel);

                }
                frame.setVisible(true);
            }
        });

        add(objBtn, BorderLayout.NORTH);

        LineBorder border = new LineBorder(null);
        setBorder(border);
    }
}
