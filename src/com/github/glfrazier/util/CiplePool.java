package com.github.glfrazier.util;

import com.github.glfrazier.objectpool.ObjectPool;

/**
 * An object pool that holds instances of {@link Record}.
 * 
 * @author Greg Frazier
 *
 */
public class CiplePool extends ObjectPool<Ciple> {

	public CiplePool() {
		super(Ciple.class);
	}
	
	public Ciple allocate() {
		Ciple ciple = super.getInstance();
		ciple.initialize();
		return ciple;
	}
	
}
