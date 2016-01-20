package com.midgardabc.day7.tanks.bf;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Brick extends SimpleBFObject implements Destroyable{

	private Image brick;

	public Brick(int x, int y) {
		super(x, y);
		color = new Color(180, 180, 180);
		try{
			brick = ImageIO.read(new File("BRICK.png"));
		}catch (IOException e){
			System.out.println("No image!");
		}
	}
	@Override
	public void draw(Graphics g) {
		if(brick != null && !this.isDestroyed()){
			g.drawImage(brick, this.getX(), this.getY(), new ImageObserver() {
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					return false;
				}
			});
		}else {
			g.setColor(this.color);
			g.fillRect(this.getX(), this.getY(), 64, 64);
		}
	}
}
