package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.Destroyable;
import com.midgardabc.day7.tanks.bf.Drawable;

public interface Tank extends Drawable, Destroyable {
	
	public Action setUp();

	public Action move();

	public Bullet fire();

	public int getX();

	public int getY();
	
	public Direction getDirection();
	
	public void updateX(int x);

	public void updateY(int y);
	
	public int getSpeed();
	
	public int getMovePath();

	public void setEnemyTank(Tank tank);

	public int[] getEnemyPosition();
}
