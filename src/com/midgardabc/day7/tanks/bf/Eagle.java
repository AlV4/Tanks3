package com.midgardabc.day7.tanks.bf;

import java.awt.Color;

public class Eagle extends SimpleBFObject implements Destroyable{

	public Eagle(int x, int y) {
		super(x, y);
		color = new Color(255, 255, 0);
	}
}
