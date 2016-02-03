package com.midgardabc.day7.tanks;

public class Launcher {

	public static void main(String[] args) throws Exception {
		ActionField af = new ActionField();
        while (true) {
            Thread.sleep(1000);
            if(af.startTheGame) {
                af.runTheGame();
            }
        }
	}
}
