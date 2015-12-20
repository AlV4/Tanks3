package com.midgardabc.day7.tanks;

import com.midgardabc.day7.tanks.bf.BFObject;
import com.midgardabc.day7.tanks.bf.BattleField;
import com.midgardabc.day7.tanks.bf.Blank;
import com.midgardabc.day7.tanks.bf.Water;
import com.midgardabc.day7.tanks.bf.tanks.Action;
import com.midgardabc.day7.tanks.bf.tanks.*;

import javax.swing.*;
import java.awt.*;

/**
 * Updated to object oriented style.
 * 
 * @version 3.0
 */
public class ActionField extends JPanel {

	private boolean COLORDED_MODE = false;

	private BattleField battleField;
	private Tank defender;
	private Tank aggressor;
	private Bullet bullet;

	/**
	 * Write your code here.
	 */
	void runTheGame() throws Exception {
		while (true) {
			if (!aggressor.isDestroyed() && !defender.isDestroyed()) {
				processAction(aggressor.setUp(), aggressor);
			}
			if (!aggressor.isDestroyed() && !defender.isDestroyed()) {
				processAction(defender.setUp(), defender);
			}
		}
	}
	
	private void processAction(Action a, Tank t) throws Exception {
		if (a == Action.MOVE) {
			processMove(t);
		} else if (a == Action.FIRE) {
			processTurn(t);
			processFire(t.fire());
		}
	}

	private void processTurn(Tank tank) throws Exception {
		repaint();
	}

	private void processMove(Tank tank) throws Exception {
		processTurn(tank);
		Direction direction = tank.getDirection();
		int step = 1;
		
		for (int i = 0; i < tank.getMovePath(); i++) {
			int covered = 0;

            int[]tankQuadrantCoordinates = getQuadrant(tank.getX(), tank.getY());

			int h = tankQuadrantCoordinates[0];
			int v = tankQuadrantCoordinates[1];

			// check limits x: 0, 513; y: 0, 513
			if ((direction == Direction.UP && tank.getY() <= 0) || (direction == Direction.DOWN && tank.getY() >= 512)
					|| (direction == Direction.LEFT && tank.getX() == 0) || (direction == Direction.RIGHT && tank.getX() >= 512)) {
				System.out.println("[illegal move] direction: " + direction
						+ " tankX: " + tank.getX() + ", tankY: " + tank.getY() + " Border achieved " + tank.toString());
				return;
			}
			
			// check next quadrant before move
			if (direction == Direction.UP) {
				v--;
			} else if (direction == Direction.DOWN) {
				v++;
			} else if (direction == Direction.RIGHT) {
				h++;
			} else if (direction == Direction.LEFT) {
				h--;
			}
			BFObject bfobject = battleField.scanQuadrant(v, h);

			if ((!(bfobject instanceof Blank) || bfobject instanceof Water) && !bfobject.isDestroyed() || h == tank.getEnemyPosition()[0] && v == tank.getEnemyPosition()[1]) {
				System.out.println("[illegal move] direction: " + direction
						+ " tankX: " + tank.getX() + ", tankY: " + tank.getY() + " " + tank.toString());
				return;
			}
	

			// process move
	
			while (covered < 64) {
				if (direction == Direction.UP) {
					tank.updateY(-step);
	//				System.out.println("[move up] direction: " + direction + " tankX: " + tank.getX() + ", tankY: " + tank.getY());
				} else if (direction == Direction.DOWN) {
					tank.updateY(step);
	//				System.out.println("[move down] direction: " + direction + " tankX: " + tank.getX() + ", tankY: " + tank.getY());
				} else if (direction == Direction.LEFT) {
					tank.updateX(-step);
	//				System.out.println("[move left] direction: " + direction + " tankX: " + tank.getX() + ", tankY: " + tank.getY());
				} else {
					tank.updateX(step);
	//				System.out.println("[move right] direction: " + direction + " tankX: " + tank.getX() + ", tankY: " + tank.getY());
				}
				covered += step;
	
				repaint();
				Thread.sleep(tank.getSpeed());
			}
		}
	}

	private void processFire(Bullet bullet) throws Exception {
		this.bullet = bullet;
		int step = 1;
		while ((bullet.getX() > -14 && bullet.getX() < 590)
				&& (bullet.getY() > -14 && bullet.getY() < 590)) {
			if (bullet.getDirection() == Direction.UP) {
				bullet.updateY(-step);
			} else if (bullet.getDirection() == Direction.DOWN) {
				bullet.updateY(step);
			} else if (bullet.getDirection() == Direction.LEFT) {
				bullet.updateX(-step);
			} else {
				bullet.updateX(step);
			}
			if (processInterception()) {
				bullet.destroy();
			}
			repaint();
			Thread.sleep(bullet.getSpeed());
			if (bullet.isDestroyed()) {
				break;
			}
		}
	}

	private boolean processInterception() {
        int [] coordinates = getQuadrant(bullet.getX(), bullet.getY());
        int x = coordinates[0];
		int y = coordinates[1];
		if (y >= 0 && y < 9 && x >= 0 && x < 9) {
			BFObject bfObject = battleField.scanQuadrant(y, x);
			if (!bfObject.isDestroyed() && !(bfObject instanceof Blank) && !(bfObject instanceof Water)) {
				battleField.destroyObject(y, x);
				return true;
			}
			
			// check aggressor
			if (!aggressor.isDestroyed()&& !bullet.autor.equals(aggressor) && checkInterception(getQuadrant(aggressor.getX(), aggressor.getY()), coordinates)) {
				aggressor.destroy();
				return true;
			}

			// check aggressor
			if (!defender.isDestroyed() && !bullet.autor.equals(defender)&& checkInterception(getQuadrant(defender.getX(), defender.getY()), coordinates)) {
				defender.destroy();
				return true;
			}
		}
		return false;
	}
	
	private boolean checkInterception(int [] object, int [] quadrant) {
		int oy = object[1];
		int ox = object[0];

		int qy = quadrant[1];
		int qx = quadrant[0];

		if (oy >= 0 && oy < 9 && ox >= 0 && ox < 9) {
			if (oy == qy && ox == qx) {
				return true;
			}
		}
		return false;
	}

	public int[] getQuadrant(int x, int y) {
		// input data should be correct
		int [] quadrants = new int[2];
        quadrants[0] = x/64;
        quadrants[1] = y/64;
		return quadrants;
	}

	public ActionField() throws Exception {
		battleField = new BattleField();
		defender = new T34(battleField);

		String location = battleField.getAggressorLocation();
		aggressor = new BT7(battleField,
			Integer.parseInt(location.split("_")[1]), Integer.parseInt(location.split("_")[0]), Direction.LEFT);

        defender.setEnemyTank(aggressor);
        aggressor.setEnemyTank(defender);

		bullet = new Bullet(-100, -100, Direction.DOWN, new T34(battleField));

		JFrame frame = new JFrame("BATTLE FIELD, DAY 7");
		frame.setLocation(750, 150);
		frame.setMinimumSize(new Dimension(battleField.getBfWidth()+ 16, battleField.getBfHeight() + 38));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(this);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int i = 0;
		Color cc;
		for (int v = 0; v < 9; v++) {
			for (int h = 0; h < 9; h++) {
				if (COLORDED_MODE) {
					if (i % 2 == 0) {
						cc = new Color(252, 241, 177);
					} else {
						cc = new Color(233, 243, 255);
					}
				} else {
					cc = new Color(180, 180, 180);
				}
				i++;
				g.setColor(cc);
				g.fillRect(h * 64, v * 64, 64, 64);
			}
		}

		battleField.draw(g);
		
		defender.draw(g);
		aggressor.draw(g);
		bullet.draw(g);
	}
}