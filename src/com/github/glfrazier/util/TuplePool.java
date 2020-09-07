package com.github.glfrazier.util;

import com.github.glfrazier.objectpool.ObjectPool;

/**
 * An object pool that holds instances of {@link Record}.
 * 
 * @author Greg Frazier
 *
 */
public class TuplePool extends ObjectPool<Tuple> {

	public TuplePool() {
		super(Tuple.class);
	}
	
	public Tuple allocate() {
		Tuple d = super.getInstance();
		d.initialize();
		return d;
	}
	
}
