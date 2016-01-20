package com.midgardabc.day7.tanks.bf;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Water extends SimpleBFObject {
    private Image water;
	
	public Water(int x, int y) {
		super(x, y);
		color = new Color(144, 149, 255);
        try{
            water = ImageIO.read(new File("WATER.png"));
        }catch (IOException e){
            System.out.println("No image of water!");
        }
	}

    @Override
    public void draw(Graphics g) {
        if(water != null){
            g.drawImage(water, this.getX(), this.getY(), new ImageObserver() {
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
