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
        Graphics2D g2 = (Graphics2D)g;
        Composite was = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g.drawImage(water, this.getX(), this.getY(), new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }

            });
        g2.setComposite(was);

    }

    @Override
    public void recover() {

    }
}
