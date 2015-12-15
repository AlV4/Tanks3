package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.BattleField;

import java.awt.*;

public abstract class AbstractTank implements Tank {
	
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

	public void move() {
	}
	
	public Bullet fire() {
		int bulletX = -100;
		int bulletY = -100;
		if (direction == Direction.UP) {
			bulletX = x + 25;
			bulletY = y - 64;
		} else if (direction == Direction.DOWN) {
			bulletX = x + 25;
			bulletY = y + 64;
		} else if (direction == Direction.LEFT) {
			bulletX = x - 64;
			bulletY = y + 25;
		} else if (direction == Direction.RIGHT) {
			bulletX = x + 64;
			bulletY = y + 25;
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
	
	public void moveToQuadrant(int v, int h) throws Exception {
	}
	
	public void moveRandom() throws Exception { 
	}

	public void clean() throws Exception {
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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getSpeed() {
		return speed;
	}

	public Color getTowerColor(){
		return towerColor;
	}
	
	@Override
	public int getMovePath() {
		return movePath;
	}
}