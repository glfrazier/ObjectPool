package com.github.glfrazier.util;

import java.io.Serializable;

import com.github.glfrazier.objectpool.AbstractPooledObject;
import com.github.glfrazier.objectpool.ObjectPool;

/**
 * A struct that holds two values. Useful for methods that want to return
 * multiple values.
 * 
 * @see Tuple
 *
 * @param <T> the first of two values
 * @param <U> the second of two values
 */
public class Duple<T, U> extends AbstractPooledObject implements Serializable {

	protected static HexlePool pool = new HexlePool();

	protected T a;
	protected U b;
	protected Object c;
	protected Object d;
	protected Object e;
	protected Object f;
	protected Object g;

	public Duple(ObjectPool<?> pool) {
		super(pool);
	}

	public Duple() {
		super(null);
	}

	protected void initialize() {
		a = null;
		b = null;
		super.initialize();
	}

	@SuppressWarnings("rawtypes")
	public static Hexle alloc(Object a, Object b) {
		Hexle hexle = pool.allocate();
		hexle.a = a;
		hexle.b = b;
		return hexle;
	}

	public final T a() {
		return a;
	}

	public final U b() {
		return b;
	}

	public String toString() {
		return "{" + a + ", " + b + "}";
	}
	
	public int size() {
		return 2;
	}

	@Override
	public void release() {
		a = null;
		b = null;
		c = null;
		d = null;
		e = null;
		f = null;
		g = null;
		super.release();
	}

}
