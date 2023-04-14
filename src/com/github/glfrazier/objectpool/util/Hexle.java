package com.github.glfrazier.objectpool.util;

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
public class Hexle<T, U, V, W, X, Y> extends Ciple<T, U, V, W, X> {

	protected Y f;

	public Hexle() {
		super();
	}

	public Hexle(ObjectPool<?> pool) {
		super(pool);
	}

	protected void initialize() {
		f = null;
		super.initialize();
	}

	public final Y f() {
		return f;
	}

	public int size() {
		return 6;
	}

	@SuppressWarnings("rawtypes")
	public static Hexle alloc(Object a, Object b, Object c, Object d, Object e, Object f) {
		Hexle hexle = (Hexle) Ciple.alloc(a, b, c, d, e);
		hexle.f = f;
		return hexle;
	}

	@Override
	public String toString() {
		return "{" + a + ", " + b + ", " + c + ", " + d + ", " + e + ", " + f + "}";
	}

}
