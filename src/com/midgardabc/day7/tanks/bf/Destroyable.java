package com.midgardabc.day7.tanks.bf;

public interface Destroyable {
	
	public void destroy();
	
	public boolean isDestroyed();

    void recover();
}
