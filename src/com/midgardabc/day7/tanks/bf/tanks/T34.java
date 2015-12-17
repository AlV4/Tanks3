package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.BattleField;

import java.awt.*;

public class T34 extends AbstractTank {

    public T34(BattleField bf) {
        super(bf, 0, 512, Direction.UP);
        tankColor = new Color(0, 136, 255);
        towerColor = new Color(248, 255, 60);
    }

    public T34(BattleField bf, int x, int y, Direction direction) {
        super(bf, x, y, direction);
        tankColor = new Color(0, 136, 255);
        towerColor = new Color(248, 255, 60);
    }

    private Object[] actions = new Object[]{
            Direction.UP,
            Action.MOVE,
            Action.MOVE,
            Action.FIRE,
            Action.MOVE,
            Action.FIRE,
            Action.FIRE,
            Action.FIRE,
            Action.MOVE,
            Action.MOVE,
            Action.MOVE,
            Action.MOVE
    };

    private int step = 0;

    @Override
    public Action setUp() {
        if (step >= actions.length) {
            step = 0;
        }
        if (!(actions[step] instanceof Action)) {
            turn((Direction) actions[step++]);
        }
        if (step >= actions.length) {
            step = 0;
        }
        return (Action) actions[step++];
    }

    @Override
    public String toString() {
        return "T34{defender}";
    }
}
