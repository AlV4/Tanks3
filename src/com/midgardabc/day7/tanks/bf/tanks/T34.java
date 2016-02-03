package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.BattleField;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class T34 extends AbstractTank {
    protected static final String nameImageUp = "Tank_Player_up.png";
    protected static final String nameImageDown = "Tank_Player_down.png";
    protected static final String nameImageLeft = "Tank_Player_left.png";
    protected static final String nameImageRight = "Tank_Player_right.png";
    public T34(BattleField bf) {
        super(bf, 0, 512, Direction.UP);
        tankColor = new Color(0, 136, 255);
        towerColor = new Color(248, 255, 60);
        try{
            imageUp = ImageIO.read(new File(nameImageUp));
            imageDown = ImageIO.read(new File(nameImageDown));
            imageLeft = ImageIO.read(new File(nameImageLeft));
            imageRight = ImageIO.read(new File(nameImageRight));
        }catch (IOException e){
            System.out.println("No tank image!");
        }


    }

    public T34(BattleField bf, int x, int y, Direction direction) {
        super(bf, x, y, direction);
        tankColor = new Color(0, 136, 255);
        towerColor = new Color(248, 255, 60);


    }

    private int step = 0;

    @Override
    public Action setUp() {
//        return eagleDefence();
        return enemyHunt();
//            return eagleHunt();
//       return moveRandomSilence();
//        return moveRandom();
//        return clean();
//        return Action.NONE;

    }

    @Override
    public void resetPosition() {
        this.x = 64;
        this.y = 512;
    }

    @Override
    public String toString() {
        return "T34{defender}";
    }
}
