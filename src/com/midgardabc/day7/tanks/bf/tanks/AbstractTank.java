package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.*;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
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

    protected Stack<Cell> listOfMovements;
    private boolean movementPossibility;
    private boolean endOfMovement;
    protected Cell[][] roadMap;

    protected Image imageUp;
    protected Image imageDown;
    protected Image imageLeft;
    protected Image imageRight;


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
    private int[][] defencePerimeter = {
            {8, 2}, {7, 2}, {6, 2}, {6, 3}, {6, 4}, {6, 5}, {6, 6}, {7, 6}, {8, 6}
    };

    public AbstractTank(BattleField bf) {
        this(bf, 128, 512, Direction.RIGHT);
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
            Graphics2D g2 = (Graphics2D)g;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                if (this.getDirection() == Direction.UP) {
                    g.drawImage(imageUp, x, y, new ImageObserver() {
                        @Override
                        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                            return false;
                        }
                    });
                }
                if (this.getDirection() == Direction.DOWN) {
                    g.drawImage(imageDown, x, y, new ImageObserver() {
                        @Override
                        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                            return false;
                        }
                    });
                }
                if (this.getDirection() == Direction.LEFT) {
                    g.drawImage(imageLeft, x, y, new ImageObserver() {
                        @Override
                        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                            return false;
                        }
                    });
                }
                if (this.getDirection() == Direction.RIGHT) {
                    g.drawImage(imageRight, x, y, new ImageObserver() {
                        @Override
                        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                            return false;
                        }
                    });
                }

        }
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }

    private void turn(int v, int h) {
        int coordX = v * 64;
        int coordY = h * 64;
        if (getX() < coordX) {
            if (getX() != coordX) {
                turn(Direction.RIGHT);
            }
        } else {
            if (getX() != coordX) {
                turn(Direction.LEFT);
            }
        }

        if (getY() < coordY) {
            if (getY() != coordY) {
                turn(Direction.DOWN);
            }
        } else {
            if (getY() != coordY) {
                turn(Direction.UP);
            }
        }
    }

    public Action moveToQuadrant(int v, int h) {
        int coordX = v * 64;
        int coordY = h * 64;
        if (getX() < coordX) {
            if (getX() != coordX) {
                turn(Direction.RIGHT);
                return Action.MOVE;
            }
        } else {
            if (getX() != coordX) {
                turn(Direction.LEFT);
                return Action.MOVE;
            }
        }

        if (getY() < coordY) {
            if (getY() != coordY) {
                turn(Direction.DOWN);
                return Action.MOVE;
            }
        } else {
            if (getY() != coordY) {
                turn(Direction.UP);
                return Action.MOVE;
            }
        }
        return Action.NONE;
    }

    public Action forcedMoveToQuadrant(int v, int h) {
        int coordX = v * 64;
        int coordY = h * 64;
        if (getX() < coordX) {
            if (getX() != coordX) {
                turn(Direction.RIGHT);
                if(!checkNextQuad(1)){
                    return Action.FIRE;
                }
                return Action.MOVE;
            }
        } else {
            if (getX() != coordX) {
                turn(Direction.LEFT);
                if(!checkNextQuad(1)){
                    return Action.FIRE;
                }
                return Action.MOVE;
            }
        }

        if (getY() < coordY) {
            if (getY() != coordY) {
                turn(Direction.DOWN);
                if(!checkNextQuad(1)){
                    return Action.FIRE;
                }
                return Action.MOVE;
            }
        } else {
            if (getY() != coordY) {
                turn(Direction.UP);
                if(!checkNextQuad(1)){
                    return Action.FIRE;
                }
                return Action.MOVE;
            }
        }
        return Action.NONE;
    }

    public Action moveRandom() {
        Direction[] dirs = Direction.values();
        setDirection(dirs[new Random().nextInt(dirs.length)]);
        return actions[new Random().nextInt(actions.length)];
    }

    public Action moveRandomSilence() {
        Direction[] dirs = Direction.values();
        setDirection(dirs[new Random().nextInt(dirs.length)]);
        return actions2[new Random().nextInt(actions2.length)];
    }

    private boolean isItBrick(BFObject bfObject) {
        if (bfObject instanceof Brick && !bfObject.isDestroyed()) {
            return true;
        }
        return false;
    }

    private boolean lineScanner(Direction direction) {
        int scanX = getX() / 64;
        int scanY = getY() / 64;
        BFObject bfObject;
        if (direction == Direction.UP) {
            while (scanY >= 0) {
                bfObject = bf.scanQuadrant(scanY--, scanX);
                if (isItBrick(bfObject)) {
                    return true;
                }
            }
        }
        if (direction == Direction.DOWN) {
            while (scanY < 9) {
                bfObject = bf.scanQuadrant(scanY++, scanX);
                if (isItBrick(bfObject)) {
                    return true;
                }
            }
        }
        if (direction == Direction.LEFT) {
            while (scanX >= 0) {
                bfObject = bf.scanQuadrant(scanY, scanX--);
                if (isItBrick(bfObject)) {
                    return true;
                }
            }
        }
        if (direction == Direction.RIGHT) {
            while (scanX < 9) {
                bfObject = bf.scanQuadrant(scanY, scanX++);
                if (isItBrick(bfObject)) {
                    return true;
                }
            }
        }

        return false;
    }

    private class Cell {
        public int x, y;
        boolean isItWall = true;
        boolean marked = false;
        int trackNumber = -1;
        Cell up, down, left, right;
        Cell[] neighbors = {
                up, down, left, right
        };

        public String toString() {

            return "[" + x + "{" + trackNumber + "}" + y + "]";
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

    private Cell[][] roadChecker() {
        Cell[][] currentRoad = roadMapCreator();
        Cell start = currentRoad[getY() / 64][getX() / 64];
        start.trackNumber = 0;
        start.marked = true;
        boolean markingIsFinished = false;
        while (!markingIsFinished) {
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
        return currentRoad;
    }

    private Cell[][] mapChecker() {
        Cell[][] currentRoad = roadMapCreator();
        Cell start = currentRoad[getY() / 64][getX() / 64];
        start.trackNumber = 0;
        start.marked = true;
        boolean markingIsFinished = false;
        while (!markingIsFinished) {
            markingIsFinished = true;
            for (int i = 0; i < 9; i++) {
                for (int k = 0; k < 9; k++) {
                    Cell cp = currentRoad[k][i];
                    if (cp.marked) {
                        for (int idx = 0; idx < 4; idx++) {
                            if (cp.neighbors[idx] != null && !cp.neighbors[idx].marked) {
                                cp.neighbors[idx].marked = true;
                                cp.neighbors[idx].trackNumber = cp.trackNumber + 1;
                                markingIsFinished = false;
                            }
                        }
                    }
                }
            }
        }
        return currentRoad;
    }

    private void finalDestination(Cell[][] currentRoad, int[] xyTo) {
        Cell finish = currentRoad[xyTo[0]][xyTo[1]];
        listOfMovements = new Stack<>();
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
    }

    protected void shortestWay(int[] xyTo) {
        Cell[][] currentRoad = roadChecker();
        finalDestination(currentRoad, xyTo);
    }

    private boolean isDefencePerimeterClear() {
        for (int[] quadCoordinates : defencePerimeter) {
            BFObject quad = bf.scanQuadrant(quadCoordinates[0], quadCoordinates[1]);
            if (isItBrick(quad)) {
                return false;
            }
        }
        return true;
    }

    private Action cleanPerimeter(){
        if(!isDefencePerimeterClear() && !(getX()/64==2 && getY()/64 ==6) && !movementPossibility){
            return forcedMoveToQuadrant(2, 6);
        }
        movementPossibility = true;
        turn(Direction.RIGHT);
        if(!isDefencePerimeterClear() && !checkNextQuad(4)){
            return Action.FIRE;
        }
        if(!isDefencePerimeterClear() && !(getX()/64==6 && getY()/64 ==6)) {
            return moveToQuadrant(6, 6);
        }
        turn(Direction.DOWN);
        if(!isDefencePerimeterClear() && !checkNextQuad(2)){
            return Action.FIRE;
        }
        return cleanPoint();
    }

    public Action eagleDefence(){
        if(!isDefencePerimeterClear()){
            return cleanPerimeter();
        }
        return enemyHunt();
    }

    public Action enemyHunt() {
        if (firePositionExist("ENEMY") && !endOfMovement) {
            shortestWay(firePosition("ENEMY"));
            Cell nextStep = listOfMovements.pop();
            if (getX() / 64 == getEnemyPosition()[0] || getY() / 64 == getEnemyPosition()[1]) {
                endOfMovement = true;
                return enemyDemolition();
            }
            return forcedMoveToQuadrant(nextStep.y, nextStep.x);
        }
        endOfMovement = false;
        return moveRandom();
    }

    public Action eagleHunt() {
        if (eagleExist() && firePositionExist("EAGLE") && !endOfMovement) {
            shortestWay(firePosition("EAGLE"));
            Cell nextStep = listOfMovements.pop();
            if (listOfMovements.empty()) {
                endOfMovement = true;
            }
            return moveToQuadrant(nextStep.y, nextStep.x);
        }
        if (endOfMovement && eagleExist()) {
            return eagleDemolition();
        }
        return moveRandom();
    }

    public Action shootTargetXY(int[] target) {
        if (target[0] > getX() / 64 && target[1] == getY() / 64) {
            setDirection(Direction.RIGHT);
        } else if (target[0] < getX() / 64 && target[1] == getY() / 64) {
            setDirection(Direction.LEFT);
        } else if (target[1] > getY() / 64 && target[0] == getX() / 64) {
            setDirection(Direction.DOWN);
        } else if (target[1] < getY() / 64 && target[0] == getX() / 64) {
            setDirection(Direction.UP);
        }
        return Action.FIRE;

    }

    private boolean eagleExist() {
        if (bf.getEagleQuadrant() != null) {
            return true;
        }
        return false;
    }

    private boolean firePositionExist(String target) {
        if (firePosition(target) != null) {
            return true;
        }
        return false;
    }

    private int[] firePosition(String target) {
        int[] quadrant = new int[2];
        if (target.trim().toUpperCase().equals("EAGLE")) {
            quadrant[1] = bf.getEagleQuadrant()[0];
            quadrant[0] = bf.getEagleQuadrant()[1];
        } else if (target.trim().toUpperCase().equals("ENEMY")) {
            quadrant = getEnemyPosition();
        }
        if (quadrant == null) {
            return null;
        }
        Cell[][] currentRoad = roadChecker();
        ArrayList<Cell> possibleFirePositions = new ArrayList();
        for (int i = 8; i > 0; i--) {
            Cell c = currentRoad[quadrant[1]][i];
            Cell c2 = currentRoad[i][quadrant[0]];
            if (!c.isItWall && c.trackNumber > 0) {
                possibleFirePositions.add(c);
            }
            if (!c2.isItWall && c2.trackNumber > 0) {
                possibleFirePositions.add(c2);
            }
        }
        if (possibleFirePositions.isEmpty()) {
            return null;
        }
        Cell cp = possibleFirePositions.get(0);
        for (Cell cell : possibleFirePositions) {
            if (cell.trackNumber >= 0 && cell.trackNumber < cp.trackNumber) {
                cp = cell;
            }
        }
        quadrant[0] = cp.x;
        quadrant[1] = cp.y;
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

    private Action enemyDemolition() {
        if (getEnemyPosition()[0] > getX() / 64 && getEnemyPosition()[1] == getY() / 64) {
            setDirection(Direction.RIGHT);
        } else if (getEnemyPosition()[0] < getX() / 64 && getEnemyPosition()[1] == getY() / 64) {
            setDirection(Direction.LEFT);
        } else if (getEnemyPosition()[1] > getY() / 64 && getEnemyPosition()[0] == getX() / 64) {
            setDirection(Direction.DOWN);
        } else if (getEnemyPosition()[1] < getY() / 64 && getEnemyPosition()[0] == getX() / 64) {
            setDirection(Direction.UP);
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

    private boolean checkNextQuad(int quantity) {
        int numX = getX() / 64;
        int numY =  getY()/ 64;
        if ((direction == Direction.UP && getY() <= 0) || (direction == Direction.DOWN && getY() >= 512)
                || (direction == Direction.LEFT && getX() <= 0) || (direction == Direction.RIGHT && getX() >= 512)) {
            return false;
        }
        for (int i = 0; i < quantity; i++) {

            if (getDirection() == Direction.UP) {
                numY--;
            } else if (getDirection() == Direction.DOWN) {
                numY++;
            } else if (getDirection() == Direction.RIGHT) {
                numX++;
            } else if (getDirection() == Direction.LEFT) {
                numX--;
            }
            try {

                BFObject bfobject = bf.scanQuadrant(numY, numX);
                if (isItBrick(bfobject) || numX == getEnemyPosition()[0] && numY == getEnemyPosition()[1]) {
                    return false;
                }
            }catch (IndexOutOfBoundsException e){
                return true;
            }
        }
        return true;
    }

    private void printMap(Cell[][] roadMap) {
        for (Cell[] cc : roadMap) {
            for (Cell c : cc) {
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