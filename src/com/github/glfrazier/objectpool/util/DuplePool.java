package com.github.glfrazier.objectpool.util;

import com.github.glfrazier.objectpool.ObjectPool;

/**
 * An object pool that holds instances of {@link Record}.
 * 
 * @author Greg Frazier
 *
 */
public class DuplePool extends ObjectPool<Duple> {

	public DuplePool() {
		super(Duple.class);
	}
	
	public Duple allocate() {
		Duple d = super.getInstance();
		d.initialize();
		return d;
	}
	
}
