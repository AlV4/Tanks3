package com.midgardabc.day7.tanks.bf.tanks;

import com.midgardabc.day7.tanks.Direction;
import com.midgardabc.day7.tanks.bf.Destroyable;
import com.midgardabc.day7.tanks.bf.Drawable;

import java.awt.*;

public class Bullet implements Drawable, Destroyable {

	private int speed = 2;
	
	private int x;
	private int y;
	private Direction direction;
	private int caliber = 10;
	public AbstractTank autor;

	private boolean destroyed;

	public Bullet(int x, int y, Direction direction, AbstractTank autor) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.destroyed = false;
		this.autor = autor;
	}

	public void updateX(int x) {
		this.x += x;
	}

	public void updateY(int y) {
		this.y += y;
	}
	
	@Override
	public void draw(Graphics g) {
		if (!destroyed) {
			g.setColor(autor.getTowerColor());
			g.fillOval(getX(), getY(), caliber, caliber);
		}
	}
	
	public void destroy() {
		destroyed = true;
	}
	
	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	public int getSpeed() {
		return speed;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Direction getDirection() {
		return direction;
	}
}
