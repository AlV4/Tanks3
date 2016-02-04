package com.midgardabc.day7.tanks;

import com.midgardabc.day7.tanks.bf.BFObject;
import com.midgardabc.day7.tanks.bf.BattleField;
import com.midgardabc.day7.tanks.bf.Blank;
import com.midgardabc.day7.tanks.bf.Water;
import com.midgardabc.day7.tanks.bf.tanks.Action;
import com.midgardabc.day7.tanks.bf.tanks.*;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Updated to object oriented style.
 *
 * @version 3.0
 */
public class ActionField extends JPanel {

    private boolean COLORDED_MODE = false;
    public boolean startTheGame;

    private BattleField battleField;
    private Tank defender;
    private Tank aggressor;
    private Bullet bullet;
    private ImageIcon tank1;
    private ImageIcon tank2;
    private Tank tiger;
    private Tank bt7;
    JFrame frame;
    JFrame choozer;
    private JTable table;
    private String [] names = {"Players", "WIN", "LOSE"};
    private Object[][] scoreTable = {
            {"Aggressor", 0, 0 },
            {"Defender", 0, 0},
            {"Eagle down", "", 0}
    };

    void runTheGame() throws Exception {

        while (true) {
            if (!aggressor.isDestroyed() && !defender.isDestroyed()) {
                processAction(aggressor.setUp(), aggressor);
            }
            if (!aggressor.isDestroyed() && !defender.isDestroyed()) {
                processAction(defender.setUp(), defender);
            }
            if (aggressor.isDestroyed() || defender.isDestroyed() || battleField.getEagleQuadrant() == null){
                if(aggressor.isDestroyed()){
                    int point = (int) scoreTable[0][2];
                    point++;
                    scoreTable[0][2] = point;

                    point = (int) scoreTable[1][1];
                    point++;
                    scoreTable[1][1] = point;

                } else if(defender.isDestroyed()){
                    int point = (int) scoreTable[0][1];
                    point++;
                    scoreTable[0][1] = point;

                    point = (int) scoreTable[1][2];
                    point++;
                    scoreTable[1][2] = point;

                }else if (battleField.getEagleQuadrant() == null){
                    int point = (int) scoreTable[0][1];
                    point++;
                    scoreTable[0][1] = point;

                    point = (int) scoreTable[1][2];
                    point++;
                    scoreTable[1][2] = point;
                    point = (int) scoreTable[2][2];
                    point++;
                    scoreTable[2][2] = point;

                }

                table.repaint();
                Thread.sleep(1000);
                frame.setVisible(false);
                startTheGame = false;
                return;
            }
        }
    }

    public void restartTheGame(){
        battleField = new BattleField();
        defender.resetPosition();
        defender.recover();
        aggressor.resetPosition();
        aggressor.recover();
    }

    private void processAction(Action a, Tank t) throws Exception {
        if (a == Action.MOVE) {
            processMove(t);
        } else if (a == Action.FIRE) {
            processTurn(t);
            processFire(t.fire());
        }
    }

    private void processTurn(Tank tank) throws Exception {
        repaint();
    }

    private boolean processMove(Tank tank) throws Exception {
        processTurn(tank);
        Direction direction = tank.getDirection();
        int step = 1;

        for (int i = 0; i < tank.getMovePath(); i++) {
            int covered = 0;

            int[] tankQuadrantCoordinates = getQuadrant(tank.getX(), tank.getY());

            int h = tankQuadrantCoordinates[0];
            int v = tankQuadrantCoordinates[1];

            // check limits x: 0, 513; y: 0, 513
            if ((direction == Direction.UP && tank.getY() <= 0) || (direction == Direction.DOWN && tank.getY() >= 512)
                    || (direction == Direction.LEFT && tank.getX() == 0) || (direction == Direction.RIGHT && tank.getX() >= 512)) {
                System.out.println("[illegal move] direction: " + direction
                        + " tankX: " + tank.getX() + ", tankY: " + tank.getY() + " Border achieved " + tank.toString());
                return false;
            }

            // check next quadrant before move
            if (direction == Direction.UP) {
                v--;
            } else if (direction == Direction.DOWN) {
                v++;
            } else if (direction == Direction.RIGHT) {
                h++;
            } else if (direction == Direction.LEFT) {
                h--;
            }
            BFObject bfobject = battleField.scanQuadrant(v, h);
//|| bfobject instanceof Water
            if ((!(bfobject instanceof Blank)) && !bfobject.isDestroyed() || h == tank.getEnemyPosition()[0] && v == tank.getEnemyPosition()[1]) {
                System.out.println("[illegal move] direction: " + direction
                        + " tankX: " + tank.getX() + ", tankY: " + tank.getY() + " " + tank.toString());
                return false;
            }


            // process move

            while (covered < 64) {
                if (direction == Direction.UP) {
                    tank.updateY(-step);
                    //				System.out.println("[move up] direction: " + direction + " tankX: " + tank.getX() + ", tankY: " + tank.getY());
                } else if (direction == Direction.DOWN) {
                    tank.updateY(step);
                    //				System.out.println("[move down] direction: " + direction + " tankX: " + tank.getX() + ", tankY: " + tank.getY());
                } else if (direction == Direction.LEFT) {
                    tank.updateX(-step);
                    //				System.out.println("[move left] direction: " + direction + " tankX: " + tank.getX() + ", tankY: " + tank.getY());
                } else {
                    tank.updateX(step);
                    //				System.out.println("[move right] direction: " + direction + " tankX: " + tank.getX() + ", tankY: " + tank.getY());
                }
                covered += step;

                repaint();
                Thread.sleep(tank.getSpeed());
            }
        }
        return true;
    }

    private void processFire(Bullet bullet) throws Exception {
        this.bullet = bullet;
        int step = 1;
        while ((bullet.getX() > -14 && bullet.getX() < 590)
                && (bullet.getY() > -14 && bullet.getY() < 590)) {
            if (bullet.getDirection() == Direction.UP) {
                bullet.updateY(-step);
            } else if (bullet.getDirection() == Direction.DOWN) {
                bullet.updateY(step);
            } else if (bullet.getDirection() == Direction.LEFT) {
                bullet.updateX(-step);
            } else {
                bullet.updateX(step);
            }
            if (processInterception()) {
                bullet.destroy();
            }
            repaint();
            Thread.sleep(bullet.getSpeed());
            if (bullet.isDestroyed()) {
                break;
            }
        }
    }

    private boolean processInterception() {
        int[] coordinates = getQuadrant(bullet.getX(), bullet.getY());
        int x = coordinates[0];
        int y = coordinates[1];
        if (y >= 0 && y < 9 && x >= 0 && x < 9) {
            BFObject bfObject = battleField.scanQuadrant(y, x);
            if (!bfObject.isDestroyed() && !(bfObject instanceof Blank) && !(bfObject instanceof Water)) {
                battleField.destroyObject(y, x);
                return true;
            }

            // check aggressor
            if (!aggressor.isDestroyed() && !bullet.autor.equals(aggressor) && checkInterception(getQuadrant(aggressor.getX(), aggressor.getY()), coordinates)) {
                aggressor.destroy();
                return true;
            }

            // check defender
            if (!defender.isDestroyed() && !bullet.autor.equals(defender) && checkInterception(getQuadrant(defender.getX(), defender.getY()), coordinates)) {
                defender.destroy();
                return true;
            }
        }
        return false;
    }

    private boolean checkInterception(int[] object, int[] quadrant) {
        int oy = object[1];
        int ox = object[0];

        int qy = quadrant[1];
        int qx = quadrant[0];

        if (oy >= 0 && oy < 9 && ox >= 0 && ox < 9) {
            if (oy == qy && ox == qx) {
                return true;
            }
        }
        return false;
    }

    public int[] getQuadrant(int x, int y) {
        // input data should be correct
        int[] quadrants = new int[2];
        quadrants[0] = x / 64;
        quadrants[1] = y / 64;
        return quadrants;
    }

    public ActionField() throws Exception {
        battleField = new BattleField();
        defender = new T34(battleField);

        int[] location = battleField.getAggressorLocation();
        bt7 = new BT7(battleField, 0, 0, Direction.DOWN);
        bt7.setEnemyTank(defender);
        tiger = new Tiger(battleField, location[1],location[0], Direction.DOWN);
        tiger.setEnemyTank(defender);
        aggressor = bt7;

        defender.setEnemyTank(aggressor);

        bullet = new Bullet(-100, -100, Direction.DOWN, new T34(battleField));

        choozer = new JFrame("BATTLE FIELD, DAY 7");
        choozer.setMinimumSize(new Dimension(battleField.getBfWidth() + 7, battleField.getBfHeight() + 29));
        choozer.setLocationRelativeTo(null);
        choozer.setResizable(false);
        choozer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        choozer.getContentPane().add(chooserCreator());
        choozer.pack();
        choozer.setVisible(true);

        frame = new JFrame("BATTLE FIELD, DAY 7");
        frame.setMinimumSize(new Dimension(battleField.getBfWidth() + 7, battleField.getBfHeight() + 29));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(false);
    }

    private JPanel chooserCreator() {
        tank1 = new ImageIcon("Tank_Enemy_up.png");
        tank2 = new ImageIcon("Tiger_up.png");
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel text = new JLabel("Choose your attack tank model!");
        text.setFont(new Font(Font.MONOSPACED, Font.BOLD, 28));
        text.setForeground(new Color(0, 82, 255));
        panel.add(text, new GridBagConstraints(
                0, 0, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        JButton model1 = new JButton("EASY");
        model1.setIcon(tank1);
        model1.setVerticalTextPosition(SwingConstants.BOTTOM);
        model1.setHorizontalTextPosition(SwingConstants.CENTER);
        model1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                aggressor = bt7;
                defender.setEnemyTank(aggressor);
                restartTheGame();
                startTheGame = true;
                frame.setVisible(true);

            }
        });

        JButton model2 = new JButton("HARD");
        model2.setIcon(tank2);
        model2.setVerticalTextPosition(SwingConstants.BOTTOM);
        model2.setHorizontalTextPosition(SwingConstants.CENTER);
        model2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
                aggressor = tiger;
                defender.setEnemyTank(aggressor);
                restartTheGame();
                startTheGame = true;
            }
        });

        panel.add(model1, new GridBagConstraints(
                0, 1, 1, 1, 1, 1, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(model2, new GridBagConstraints(
                1, 1, 1, 1, 1, 1, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        table = new JTable(new TableModel() {
            @Override
            public int getRowCount() {
                return 3;
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public String getColumnName(int columnIndex) {

                return names[columnIndex];
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return scoreTable.getClass();
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return scoreTable[rowIndex][columnIndex];
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

            }

            @Override
            public void addTableModelListener(TableModelListener l) {

            }

            @Override
            public void removeTableModelListener(TableModelListener l) {

            }
        });
        table.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        table.setGridColor(Color.white);
        table.setRowHeight(35);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        for(String column : names){
            table.getColumn(column).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(100, 200));
        panel.add(scrollPane, new GridBagConstraints(
                0, 2, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        return panel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int i = 0;
        Color cc;
        for (int v = 0; v < 9; v++) {
            for (int h = 0; h < 9; h++) {
                if (COLORDED_MODE) {
                    if (i % 2 == 0) {
                        cc = new Color(252, 241, 177);
                    } else {
                        cc = new Color(233, 243, 255);
                    }
                } else {
                    cc = new Color(180, 180, 180);
                }
                i++;
                g.setColor(cc);
                g.fillRect(h * 64, v * 64, 64, 64);
            }
        }

        battleField.draw(g);

        defender.draw(g);
        aggressor.draw(g);
        bullet.draw(g);
    }
}