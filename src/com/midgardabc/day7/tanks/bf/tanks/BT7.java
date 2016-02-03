package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.BattleField;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BT7 extends AbstractTank {
	protected static final String nameImageUp = "Tank_Enemy_up.png";
	protected static final String nameImageDown = "Tank_Enemy_down.png";
	protected static final String nameImageLeft = "Tank_Enemy_left.png";
	protected static final String nameImageRight = "Tank_Enemy_right.png";
	
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
		try{
			imageUp = ImageIO.read(new File(nameImageUp));
			imageDown = ImageIO.read(new File(nameImageDown));
			imageLeft = ImageIO.read(new File(nameImageLeft));
			imageRight = ImageIO.read(new File(nameImageRight));
		}catch (IOException e){
			System.out.println("No tank image!");
		}
	}

    @Override
	public Action setUp() {
//		return moveRandomSilence();
		return enemyHunt();
//        return eagleHunt();
//		return moveRandom();
//		return clean();
//		return eagleDefence();
	}

    @Override
    public String toString() {
        return "BT7{aggressor}";
    }
}
