package com.github.glfrazier.objectpool;

import java.io.Serializable;

/**
 * An abstract implementation of {@link Poolable} that provides the constructor
 * that {@link ObjectPool} requires, as well as implementations of
 * {@link #initialize()} and {@link #release()}. Maintains a private boolean
 * {@link #allocated} that provides error-checking on the instance's life cycle.
 * 
 * @author Greg Frazier
 *
 */
public class AbstractPooledObject implements Poolable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Ensures that already-allocated objects are not reallocated, and
	 * already-released objects are not re-released.
	 */
	private boolean allocated = false;
	/**
	 * The pool that this instance came from and will be returned to.
	 */
	private transient ObjectPool<AbstractPooledObject> pool;

	/**
	 * Construct a new instance of the pooled object.
	 * 
	 * @param pool the object pool to which this instance belongs.
	 */
	@SuppressWarnings("unchecked")
	public AbstractPooledObject(ObjectPool<?> pool) {
		this.pool = (ObjectPool<AbstractPooledObject>) pool;
	}
	
	public AbstractPooledObject() {
		pool = null;
	}

	/**
	 * After the instance is allocated from the pool and before it is returned to
	 * the client, it must be <i>initialized</i>. Checks and toggles
	 * {@link #allocated}.
	 */
	protected synchronized void initialize() {
		if (allocated) {
			throw new IllegalStateException("Initializing an already-allocated instance.");
		}
		allocated = true;
	}

	/**
	 * Returns the instance to the pool, making it available to future allocations.
	 * Also, checks and toggles {@link #allocated}.
	 */
	public synchronized void release() {
		if (!allocated || pool == null) {
			return;
		}
		allocated = false;
		pool.releaseInstance(this);
	}

}
