package com.github.glfrazier.util;

import com.github.glfrazier.objectpool.ObjectPool;

/**
 * A struct that holds two values. Useful for methods that want to return multiple values.
 * @see Tuple
 *
 * @param <T> the first of two values
 * @param <U> the second of two values
 */
public class Quaple<T,U,V,W>  extends Tuple<T,U,V> {

	protected W d;
	
	public Quaple(ObjectPool<?> pool) {
		super(pool);
	}
	
	public Quaple() {
		super();
	}

	protected void initialize() {
		d=null;
		super.initialize();
	}
	
	public final W d() {
		return d;
	}
	
	public int size() {
		return 4;
	}
	
	@SuppressWarnings("rawtypes")
	public static Quaple alloc(Object a, Object b, Object c, Object d) {
		Quaple quaple = (Quaple)Tuple.alloc(a,b,c);
		quaple.d = d;
		return quaple;
	}
	
}
