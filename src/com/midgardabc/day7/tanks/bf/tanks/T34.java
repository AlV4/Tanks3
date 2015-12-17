package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.BattleField;

import java.awt.*;
import java.util.Random;

public class T34 extends AbstractTank {

    public T34(BattleField bf) {
        super(bf, 128, 512, Direction.UP);
        tankColor = new Color(0, 136, 255);
        towerColor = new Color(248, 255, 60);
    }

    public T34(BattleField bf, int x, int y, Direction direction) {
        super(bf, x, y, direction);
        tankColor = new Color(0, 136, 255);
        towerColor = new Color(248, 255, 60);
    }

    private Action[] actions = new Action[]{

            Action.MOVE,
            Action.FIRE,

    };

    private int step = 0;

    @Override
    public Action setUp() {
        Direction[]dirs = Direction.values();
        setDirection(dirs[new Random().nextInt(dirs.length)]);
        return actions[new Random().nextInt(actions.length)];
    }

    @Override
    public String toString() {
        return "T34{defender}";
    }
}
