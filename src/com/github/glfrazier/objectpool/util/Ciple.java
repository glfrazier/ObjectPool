package com.github.glfrazier.objectpool.util;

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
public class Ciple<T, U, V, W, X> extends Quaple<T, U, V, W> {

	protected X e;

	public Ciple() {
		super();
	}

	public Ciple(ObjectPool<?> pool) {
		super(pool);
	}

	protected void initialize() {
		e = null;
		super.initialize();
	}

	public final X e() {
		return e;
	}

	public int size() {
		return 5;
	}

	@SuppressWarnings("rawtypes")
	public static Ciple alloc(Object a, Object b, Object c, Object d, Object e) {
		Ciple ciple = (Ciple) Quaple.alloc(a, b, c, d);
		ciple.e = e;
		return ciple;
	}

	@Override
	public String toString() {
		return "{" + a + ", " + b + ", " + c + ", " + d + ", " + e + "}";
	}

}
