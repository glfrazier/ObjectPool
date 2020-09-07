package com.github.glfrazier.util;

import com.github.glfrazier.objectpool.AbstractPooledObject;
import com.github.glfrazier.objectpool.ObjectPool;
import com.github.glfrazier.objectpool.Poolable;

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
public class Tuple<T, U, V> extends AbstractPooledObject {

	private static TuplePool pool = new TuplePool();

	public T a;
	public U b;
	public V c;

	public Tuple(ObjectPool<?> pool) {
		super(pool);
	}
	
	protected void initialize() {
		a=null;
		b=null;
		c=null;
		super.initialize();
	}
	
	public static Tuple alloc(Object a, Object b, Object c) {
		Tuple d = pool.allocate();
		d.a = a;
		d.b = b;
		d.c = c;
		return d;
	}

}
