package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.BattleField;

import java.awt.*;

public class T34 extends AbstractTank {

    public T34(BattleField bf) {
        super(bf, 64, 448, Direction.UP);
        tankColor = new Color(0, 136, 255);
        towerColor = new Color(248, 255, 60);
    }

    public T34(BattleField bf, int x, int y, Direction direction) {
        super(bf, x, y, direction);
        tankColor = new Color(0, 136, 255);
        towerColor = new Color(248, 255, 60);
    }

    private int step = 0;

    @Override
    public Action setUp() {
//            return cleanPerimeter();
        return enemyHunt();
//            return eagleHunt();
//       return moveRandomSilence();
//        return moveRandom();
//        return clean();
//        return Action.NONE;

    }

    @Override
    public String toString() {
        return "T34{defender}";
    }
}
