package com.github.glfrazier.objectpool;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * An object pool allows a program to manage the lifecycle of a class. A use
 * case is a class whose instances are being created and freed at a very high
 * rate, causing distress to the garbage collector. Or one has written a program
 * that runs for extended periods of time without blocking on i/o, preventing
 * the garbage collector from running. In either of these situations (or perhaps
 * when both situations are present!), it can be useful to reuse the instances
 * rather than garbage collect them.
 * 
 * A subclass of object pool that holds instances of <code>MyClass</code> must:
 * <ul>
 * <li>Extend <code>ObjectPool&lt;MyClass&gt;</code></li>
 * <li>Invoke <code>super(MyClass.class)</code> in its constructor</li>
 * <li>Provide a public method whereby clients obtain instances of
 * <code>MyClass</code>. Recommend that the method be named
 * <code>allocate</code>, and further recommend that <code>allocate</code>
 * initialize the newly-allocated object before returning it.
 * </ul>
 * 
 * Note that <code>MyClass</code> must have a constructor that takes the object
 * pool as an argument; {@link AbstractPooledObject} provides such a
 * constructor. Further, if <code>MyClass</code> does not extend
 * {@link AbstractPooledObject}, then the object pool subclass must have an
 * accessible method that invokes {@link #releaseInstance(Poolable)}, so that
 * <code>MyClass</code> instances can be returned to the pool after use.
 * 
 * An example of how to use <code>ObjectPool</code> and
 * {@link AbstractPooledObject} is the {@link example.RecordPool} class that is in the
 * demo/ directory that accompanied this distribution.
 * 
 * @see Poolable
 * @see AbstractPooledObject
 * 
 * @author Greg Frazier
 *
 * @param <E> the class whose instances are in the pool
 */
public abstract class ObjectPool<E extends Poolable> {

	/**
	 * The objects in the pool. The list is used like a stack, making ArrayList an
	 * efficient implementation.
	 */
	private List<E> pool = new ArrayList<>();
	/**
	 * The constructor for E that takes an <code>ObjectPool</code> as its argument.
	 */
	private Constructor<E> constructor;

	/** Track the total number of E instances constructed. */
	private int totalConstructed;
	/** Track the maximum number of E instances in the pool at any point in time. */
	private int maxPoolSize;
	/** Track the number of times instances were requested from the pool. */
	private long numberOfAllocations;

	/**
	 * Construct a new ObjectPool.
	 * 
	 * @param clazz The class object for E. Used to obtain the zero-argument
	 *              constructor.
	 * @throws IllegalArgumentException If E does not have a zero-argument
	 *                                  constructor.
	 */
	public ObjectPool(Class<E> clazz) throws IllegalArgumentException {
		Class<?>[] args = new Class[1];
		args[0] = ObjectPool.class;
		try {
			constructor = clazz.getDeclaredConstructor(args);
		} catch (Throwable t) {
			throw new IllegalArgumentException("Failed to obtain constructor(ObjectPool) for " + clazz + ": " + t);
		}
	}

	/**
	 * Obtain an instance of E. Note that<br>
	 * a. This method is protected; the implementing subclass must provide an
	 * accessible method that invokes <code>getInstance()</code>, and<br>
	 * b. The instance of E that is returned is not initialized.
	 * 
	 * See the implementation of <code>example.RecordPool</code> in the demo/
	 * directory of this distribution of an example implementation of
	 * <code>allocate(args)</code>.
	 * 
	 * @return an instance of E, ready to be used.
	 */
	protected synchronized E getInstance() {
		numberOfAllocations++;
		E result;
		if (pool.isEmpty()) {
			try {
				result = constructor.newInstance(this);
				totalConstructed++;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new IllegalStateException(
						"Failed to create an instance of " + constructor.getDeclaringClass() + ": " + e);
			}
		} else {
			result = pool.remove(pool.size() - 1);
		}
		return result;
	}

	/**
	 * Returns an instance of <code>E</code> to the pool.
	 * 
	 * @param instance the instance to be returned to the pool.
	 * @see AbstractPooledObject#release()
	 */
	protected synchronized void releaseInstance(E instance) {
		pool.add(instance);
		int size = pool.size();
		if (size > maxPoolSize) {
			maxPoolSize = size;
		}
	}

	public int size() {
		return pool.size();
	}

	public int getTotalConstructed() {
		return totalConstructed;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public long getNumberOfAllocations() {
		return numberOfAllocations;
	}
}
