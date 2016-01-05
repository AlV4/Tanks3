package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.*;

import java.awt.*;
import java.util.Random;
import java.util.Stack;

public abstract class AbstractTank implements Tank {
    public Tank enemyTank;

    private int speed = 10;
    protected int movePath = 1;

    // 1 - up, 2 - down, 3 - left, 4 - right
    private Direction direction;

    // current position on BF
    private int x;
    private int y;

    private boolean destroyed;

    private BattleField bf;

    protected Stack listOfMovements;
    protected Cell[][] roadMap;

    protected Color tankColor;
    protected Color towerColor;
    private int barrelWidth = 11;
    private int barrelHeight = 40;
    private Action[] actions = new Action[]{
            Action.MOVE,
            Action.FIRE
    };
    private Action[] actions2 = new Action[]{
            Action.MOVE
    };

    public AbstractTank(BattleField bf) {
        this(bf, 128, 512, Direction.UP);
    }

    public AbstractTank(BattleField bf, int x, int y, Direction direction) {
        this.bf = bf;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.destroyed = false;
        roadMap = roadMapCreator();
    }

    public void turn(Direction direction) {
        this.direction = direction;
    }

    public Action move() {
        return Action.MOVE;
    }

    public Bullet fire() {
        int bulletX = -100;
        int bulletY = -100;
        if (direction == Direction.UP) {
            bulletX = x + 26;
            bulletY = y;
        } else if (direction == Direction.DOWN) {
            bulletX = x + 26;
            bulletY = y + 64;
        } else if (direction == Direction.LEFT) {
            bulletX = x;
            bulletY = y + 26;
        } else if (direction == Direction.RIGHT) {
            bulletX = x + 64;
            bulletY = y + 26;
        }
        return new Bullet(bulletX, bulletY, direction, this);
    }

    public void draw(Graphics g) {
        if (!destroyed) {
            g.setColor(tankColor);
            g.fillRect(this.getX(), this.getY(), 64, 64);

            g.setColor(towerColor);
            if (this.getDirection() == Direction.UP) {
                g.fillRect(getX() + 27, getY() - 10, barrelWidth, barrelHeight);
            } else if (this.getDirection() == Direction.DOWN) {
                g.fillRect(getX() + 27, getY() + 34, barrelWidth, barrelHeight);
            } else if (this.getDirection() == Direction.LEFT) {
                g.fillRect(getX() - 10, getY() + 27, barrelHeight, barrelWidth);
            } else {
                g.fillRect(getX() + 34, getY() + 27, barrelHeight, barrelWidth);
            }
            g.fillOval(getX() + 12, getY() + 12, 40, 40);
        }
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }

    public Action moveToQuadrant(int v, int h) {
        int coordX = v * 64;
        int coordY = h * 64;
        if (getX() < coordX) {
            while (getX() != coordX) {
                turn(Direction.RIGHT);
                return Action.MOVE;
            }
        } else {
            while (getX() != coordX) {
                turn(Direction.LEFT);
                return Action.MOVE;
            }
        }

        if (getY() < coordY) {
            while (getY() != coordY) {
                turn(Direction.DOWN);
                return Action.MOVE;
            }
        } else {
            while (getY() != coordY) {
                turn(Direction.UP);
                return Action.MOVE;
            }
        }
        return Action.NONE;
    }

    public Action moveRandom() {
        Direction[] dirs = Direction.values();
        setDirection(dirs[new Random().nextInt(dirs.length)]);
//        if (lineScanner(getDirection())) {
//            return Action.FIRE;
//        }
        return actions[new Random().nextInt(actions.length)];
    }

    public Action moveRandomSilence() {
        Direction[] dirs = Direction.values();
        setDirection(dirs[new Random().nextInt(dirs.length)]);
//        if (lineScanner(getDirection())) {
//            return Action.FIRE;
//        }
        return actions2[new Random().nextInt(actions2.length)];
    }

    private boolean lineScanner(Direction direction) {
        int scanX = getX() / 64;
        int scanY = getY() / 64;

        if (direction == Direction.UP) {
            while (scanY >= 0) {
                BFObject bfObject = bf.scanQuadrant(scanY--, scanX);
                if (!(bfObject).isDestroyed() && !(bfObject instanceof Blank) && !(bfObject instanceof Water)) {
                    return true;
                }
            }
        }
        if (direction == Direction.DOWN) {
            while (scanY < 9) {
                BFObject bfObject = bf.scanQuadrant(scanY++, scanX);
                if (!(bfObject).isDestroyed() && !(bfObject instanceof Blank) && !(bfObject instanceof Water)) {
                    return true;
                }
            }
        }
        if (direction == Direction.LEFT) {
            while (scanX >= 0) {
                BFObject bfObject = bf.scanQuadrant(scanY, scanX--);
                if (!(bfObject).isDestroyed() && !(bfObject instanceof Blank) && !(bfObject instanceof Water)) {
                    return true;
                }
            }
        }
        if (direction == Direction.RIGHT) {
            while (scanX < 9) {
                BFObject bfObject = bf.scanQuadrant(scanY, scanX++);
                if (!(bfObject).isDestroyed() && !(bfObject instanceof Blank) && !(bfObject instanceof Water)) {
                    return true;
                }
            }
        }

        return false;
    }

    public class Cell {
        String s = "*";
        public int x, y;
        boolean isItWall = true;
        boolean marked = false;
        int trackNumber = -1;
        Cell up, down, left, right;
        Cell[] neighbors = {
                up, down, left, right
        };

        public String toString() {
            if(isItWall){
                s = "#";
            }else {
                s = " ";
            }
            return "[" + x + "{" + s + "}" + y + "]";
        }
    }

    private Cell[][] roadMapCreator() {
        Cell[][] cells = new Cell[9][9];
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                Cell temp = new Cell();
                if (bf.scanQuadrant(i, k) instanceof Blank || bf.scanQuadrant(i, k).isDestroyed() || bf.scanQuadrant(i, k) instanceof Eagle) {
                    temp.isItWall = false;
                }
                temp.x = i;
                temp.y = k;
                cells[i][k] = temp;
            }
        }
        findNeighbours(cells);
        return cells;
    }

    private void findNeighbours(Cell[][] cells) {
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                try {
                    cells[i][k].neighbors[2] = cells[i - 1][k];
                } catch (IndexOutOfBoundsException e) {
                }
                try {
                    cells[i][k].neighbors[3] = cells[i + 1][k];
                } catch (IndexOutOfBoundsException e) {
                }
                try {
                    cells[i][k].neighbors[0] = cells[i][k - 1];
                } catch (IndexOutOfBoundsException e) {
                }
                try {
                    cells[i][k].neighbors[1] = cells[i][k + 1];
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }
    }

    protected void shortestWay(int xFrom, int yFrom, int xTo, int yTo) {
        listOfMovements = new Stack();
        Cell[][] currentRoad = roadMapCreator();
        Cell start = currentRoad[xFrom][yFrom];
        start.trackNumber = 0;
        start.marked = true;
        Cell finish = currentRoad[xTo][yTo];
        boolean markingIsFinished = false;
        while (!finish.marked && !markingIsFinished) {
            markingIsFinished = true;
            for (int i = 0; i < 9; i++) {
                for (int k = 0; k < 9; k++) {
                    Cell cp = currentRoad[k][i];
                    if (cp.marked) {
                        for (int idx = 0; idx < 4; idx++) {
                            if (cp.neighbors[idx] != null && !cp.neighbors[idx].isItWall && !cp.neighbors[idx].marked) {
                                cp.neighbors[idx].marked = true;
                                cp.neighbors[idx].trackNumber = cp.trackNumber + 1;
                                markingIsFinished = false;
                            }
                        }
                    }
                }
            }
        }
        if (finish.marked) {
            Cell cp = finish;
            while (cp.trackNumber != 0) {
                listOfMovements.push(cp);
                for (int idx = 0; idx < 4; idx++) {
                    if (cp.neighbors[idx] != null && cp.neighbors[idx].trackNumber == cp.trackNumber - 1) {
                        cp = cp.neighbors[idx];
                        break;
                    }
                }
            }
        }
//        System.out.println(Arrays.asList(listOfMovements));
    }

    public Action eagleHunt() {
//        printMap();
        while (bf.getEagleQuadrant() != null) {
            shortestWay(getY() / 64, getX() / 64, bf.getEagleQuadrant()[0], bf.getEagleQuadrant()[1]);
            while (listOfMovements.size() > 0) {
                Cell cp = (Cell) listOfMovements.pop();
                if (listOfMovements.size() == 1) {
                    return eagleDemolition();
                }
                return moveToQuadrant(cp.y, cp.x);
            }
        }
        return moveRandom();
    }

    private int[] firePosition(Object o) {
        int[] quadrant = new int[2];
        if (o instanceof Eagle) {
            quadrant[0] = bf.getEagleQuadrant()[0];
            quadrant[1] = bf.getEagleQuadrant()[1];
        }
        return quadrant;
    }

    private Action eagleDemolition() {
        if (bf.getEagleQuadrant()[1] > getX() / 64) {
            setDirection(Direction.RIGHT);
        } else if (bf.getEagleQuadrant()[1] == getX() / 64) {
            setDirection(Direction.DOWN);
        } else {
            setDirection(Direction.LEFT);
        }
        return Action.FIRE;
    }

    public Action clean() {
        if (cleanPoint() != null) {
            return cleanPoint();

        } else if (bf.fieldScanner() != null) {
            return moveToQuadrant(bf.fieldScanner()[1], bf.fieldScanner()[0]);
        }
        return Action.NONE;
    }

    private Action cleanPoint() {
        for (int i = 0; i < Direction.values().length; i++) {
            turn(Direction.values()[i]);
            while (lineScanner(getDirection())) {
                return Action.FIRE;
            }
        }
        return null;
    }

    private boolean checkNextQuad() {
        int v = getY() / 64;
        int h = getX() / 64;
        if ((direction == Direction.UP && getY() <= 0) || (direction == Direction.DOWN && getY() >= 512)
                || (direction == Direction.LEFT && getX() == 0) || (direction == Direction.RIGHT && getX() >= 512)) {
            return false;
        }
        for (int i = 0; i < 1; i++) {

            if (direction == Direction.UP) {
                v--;
            } else if (direction == Direction.DOWN) {
                v++;
            } else if (direction == Direction.RIGHT) {
                h++;
            } else if (direction == Direction.LEFT) {
                h--;
            }
            BFObject bfobject = bf.scanQuadrant(v, h);
            if ((!(bfobject instanceof Blank) || bfobject instanceof Water) && !bfobject.isDestroyed() || h == getEnemyPosition()[0] && v == getEnemyPosition()[1]) {
                return false;
            }
        }
        return true;
    }

    private void printMap(){
        for (Cell[]cc:roadMap){
            for (Cell c:cc){
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void updateX(int x) {
        this.x += x;
    }

    public void updateY(int y) {
        this.y += y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public Color getTowerColor() {
        return towerColor;
    }

    public int[] getEnemyPosition() {
        int[] position = new int[2];
        position[0] = enemyTank.getX() / 64;
        position[1] = enemyTank.getY() / 64;
        return position;
    }

    @Override
    public void setEnemyTank(Tank tank) {
        enemyTank = tank;
    }

    @Override
    public int getMovePath() {
        return movePath;
    }
}