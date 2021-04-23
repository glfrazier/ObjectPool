package com.github.glfrazier.util;

import com.github.glfrazier.objectpool.ObjectPool;

/**
 * A struct that holds three values. Useful for methods that want to return
 * multiple values.
 * 
 * @see Duple
 *
 * @param <T> the first of three values
 * @param <U> the second of three values
 * @param <V> the third of three values
 */
public class Tuple<T, U, V> extends Duple<T, U> {

	protected V c;

	public Tuple(ObjectPool<?> pool) {
		super(pool);
	}
	
	public Tuple() {
		super();
	}

	protected void initialize() {
		a=null;
		b=null;
		c=null;
		super.initialize();
	}
	
	public final V c() {
		return c;
	}
	
	@SuppressWarnings("rawtypes")
	public static Tuple alloc(Object a, Object b, Object c) {
		Tuple tuple = (Tuple)pool.allocate();
		tuple.a = a;
		tuple.b = b;
		tuple.c = c;
		return tuple;
	}

}
