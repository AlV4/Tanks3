package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.BFObject;
import com.midgardabc.day7.tanks.bf.BattleField;
import com.midgardabc.day7.tanks.bf.Blank;
import com.midgardabc.day7.tanks.bf.Water;

import java.awt.*;
import java.util.Random;

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

    protected Color tankColor;
    protected Color towerColor;
    private int barrelWidth = 11;
    private int barrelHeight = 40;
    private Action[] actions = new Action[]{

            Action.MOVE,
//            Action.FIRE,

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
        if (lineScanner(getDirection())) {
            return Action.FIRE;
        }
        return actions[new Random().nextInt(actions.length)];
    }



    private boolean lineScanner(Direction direction) {
        int scanX = getX() / 64;
        int scanY = getY() / 64;

        if (direction == Direction.UP) {
            while (scanY >= 0) {
                BFObject bfObject = bf.scanQuadrant(scanY--, scanX);
                if (!(bfObject).isDestroyed()&& !(bfObject instanceof Blank) && !(bfObject instanceof Water)) {
                    return true;
                }
            }
        }
        if (direction == Direction.DOWN) {
            while (scanY < 9) {
                BFObject bfObject = bf.scanQuadrant(scanY++, scanX);
                if (!(bfObject).isDestroyed()&& !(bfObject instanceof Blank) && !(bfObject instanceof Water)) {
                    return true;
                }
            }
        }
        if (direction == Direction.LEFT) {
            while (scanX >= 0) {
                BFObject bfObject = bf.scanQuadrant(scanY, scanX--);
                if (!(bfObject).isDestroyed()&& !(bfObject instanceof Blank) && !(bfObject instanceof Water)) {
                    return true;
                }
            }
        }
        if (direction == Direction.RIGHT) {
            while (scanX < 9) {
                BFObject bfObject = bf.scanQuadrant(scanY, scanX++);
                if (!(bfObject).isDestroyed()&& !(bfObject instanceof Blank) && !(bfObject instanceof Water)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Action clean(){
        if(cleanPoint() != null){
            return cleanPoint();

        }else if(bf.fieldScanner() != null){
            return moveToQuadrant(bf.fieldScanner()[1], bf.fieldScanner()[0]);
        }
        return Action.NONE;
    }

    private Action cleanPoint(){
        for(int i = 0; i < Direction.values().length; i++) {
            turn(Direction.values()[i]);
            while (lineScanner(getDirection())) {
                return Action.FIRE;
            }
        }
        return null;
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