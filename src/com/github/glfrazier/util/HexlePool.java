package com.github.glfrazier.util;

import com.github.glfrazier.objectpool.ObjectPool;

/**
 * An object pool that holds instances of {@link Record}.
 * 
 * @author Greg Frazier
 *
 */
@SuppressWarnings("rawtypes")
public class HexlePool extends ObjectPool<Hexle> {

	public HexlePool() {
		super(Hexle.class);
	}
	
	public Hexle allocate() {
		Hexle hexle = super.getInstance();
		hexle.initialize();
		return hexle;
	}
	
}
