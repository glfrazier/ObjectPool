package com.github.glfrazier.util;

import com.github.glfrazier.objectpool.AbstractPooledObject;
import com.github.glfrazier.objectpool.ObjectPool;

/**
 * A struct that holds two values. Useful for methods that want to return multiple values.
 * @see Tuple
 *
 * @param <T> the first of two values
 * @param <U> the second of two values
 */
public class Quaple<T,U,V,W>  extends AbstractPooledObject {

	private static QuaplePool pool = new QuaplePool();
	
	public T a;
	public U b;public V c;
	public W d;
	
	public Quaple(ObjectPool<?> pool) {
		super(pool);
	}
	
	protected void initialize() {
		a=null;
		b=null;
		super.initialize();
	}
	
	public static Quaple alloc(Object a, Object b, Object c, Object d) {
		Quaple quaple = pool.allocate();
		quaple.a = a;
		quaple.b = b;
		quaple.c = c;
		quaple.d = d;
		return quaple;
	}
	
}
