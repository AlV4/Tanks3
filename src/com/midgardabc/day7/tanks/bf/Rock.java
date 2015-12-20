package com.midgardabc.day7.tanks.bf;

import java.awt.Color;

public class Rock extends SimpleBFObject implements Destroyable{
	
	public Rock(int x, int y) {
		super(x, y);
		color = new Color(0, 0, 0);
	}

}
