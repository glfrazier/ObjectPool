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
public class Ciple<T,U,V,W,X>  extends AbstractPooledObject {

	private static CiplePool pool = new CiplePool();
	
	public T a;
	public U b;
	public V c;
	public W d;
	public X e;
	
	public Ciple(ObjectPool<?> pool) {
		super(pool);
	}
	
	protected void initialize() {
		a=null;
		b=null;
		c=null;
		d=null;
		e=null;
		super.initialize();
	}
	
	@SuppressWarnings("rawtypes")
	public static Ciple alloc(Object a, Object b, Object c, Object d, Object e) {
		Ciple ciple = pool.allocate();
		ciple.a = a;
		ciple.b = b;
		ciple.c = c;
		ciple.d = d;
		ciple.e = e;
		return ciple;
	}
	
	@Override
	public String toString() {
		return "{" + a + ", " + b + ", " + c + ", " + d + ", " + e + "}";
	}
	
}
