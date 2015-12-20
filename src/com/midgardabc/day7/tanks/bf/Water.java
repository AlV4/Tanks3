package com.midgardabc.day7.tanks.bf;

import java.awt.*;

public class Water extends SimpleBFObject {
	
	public Water(int x, int y) {
		super(x, y);
		color = new Color(144, 149, 255);
	}

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getX(), this.getY(), 64, 64);
    }
}
