package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.BattleField;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Tiger extends AbstractTank {
	protected static final String nameImageUp = "Tiger_up.png";
	protected static final String nameImageDown = "Tiger_down.png";
	protected static final String nameImageLeft = "Tiger_left.png";
	protected static final String nameImageRight = "Tiger_right.png";


	private int armor;
	
	public Tiger(BattleField bf) {
		super(bf);
		tankColor = new Color(255, 0, 0);
		towerColor = new Color(35, 0, 255);
		armor = 1;
	}
	
	public Tiger(BattleField bf, int x, int y, Direction direction) {
		super(bf, x, y, direction);
		tankColor = new Color(255, 0, 0);
		towerColor = new Color(35, 0, 255);
		armor = 1;
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
	public void destroy() {
		if (armor > 0) {
			armor--;
		} else {
			super.destroy();
		}
	}
	
	@Override
	public Action setUp() {
		return moveRandom();
	}
	@Override
	public String toString() {
		return "Tiger{aggressor}";
	}
}
