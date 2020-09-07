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
public class Duple<T,U>  extends AbstractPooledObject {

	private static DuplePool pool = new DuplePool();
	
	public T a;
	public U b;
	
	public Duple(ObjectPool<?> pool) {
		super(pool);
	}
	
	protected void initialize() {
		a=null;
		b=null;
		super.initialize();
	}
	
	public static Duple alloc(Object a, Object b) {
		Duple d = pool.allocate();
		d.a = a;
		d.b = b;
		return d;
	}
	
	public String toString() {
		return "Duple(" + a + ", " + b + ")";
	}
	
}
