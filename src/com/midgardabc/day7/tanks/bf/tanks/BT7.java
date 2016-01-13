package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.BattleField;

import java.awt.*;

public class BT7 extends AbstractTank {
	
	public BT7(BattleField bf) {
		super(bf);
		tankColor = new Color(0, 0, 0);
		towerColor = new Color(255, 0, 0);
		movePath = 2;
	}
	
	public BT7(BattleField bf, int x, int y, Direction direction) {
		super(bf, x, y, direction);
		tankColor = new Color(0, 0, 0);
		towerColor = new Color(255, 0, 0);
		movePath = 1;
	}




	@Override
	public Action setUp() {
//		return moveRandomSilence();
//		return enemyHunt();
//		return forcedMoveToQuadrant(0, 8);
        return eagleHunt();
//		return moveRandom();
//		return clean();
	}

    @Override
    public String toString() {
        return "BT7{aggressor}";
    }
}
