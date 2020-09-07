package com.github.glfrazier.util;

import com.github.glfrazier.objectpool.ObjectPool;

/**
 * An object pool that holds instances of {@link Record}.
 * 
 * @author Greg Frazier
 *
 */
public class QuaplePool extends ObjectPool<Quaple> {

	public QuaplePool() {
		super(Quaple.class);
	}
	
	public Quaple allocate() {
		Quaple quaple = super.getInstance();
		quaple.initialize();
		return quaple;
	}
	
}
