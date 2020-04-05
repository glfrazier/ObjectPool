package glf.objectpool;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * A subclass of ObjectPool must implement a public method that invokes
 * getInstance and returns a new instance of the pooled class (recommend
 * "allocate" for the method name). It must also implement a public method that
 * invokes releaseInstance (recommend "deallocate" for its name). Since it is
 * easy to create bugs when implementing object lifecycle management, one should
 * practice defensive programming by having the pooled object maintain its
 * allocated/free state internally. <code>allocate</code> should check that it
 * has obtained a free object from the pool and then set its state to allocated,
 * and deallocate should check that the object passed to it is in the allocated
 * state and then set it to free before returning it to the pool.
 * 
 * See the accompanying demo implementation.
 * 
 * @author glfrazier
 *
 * @param <E>
 */
public abstract class ObjectPool<E extends Poolable> {

	private List<E> pool = new ArrayList<>();
	private Constructor<E> constructor;
	
	private int totalConstructed;
	private int maxPoolSize;
	private long numberOfAllocations;

	public ObjectPool(Class<E> clazz) throws IllegalArgumentException {
		Class<?>[] args = new Class[1];
		args[0] = ObjectPool.class;
		try {
			constructor = clazz.getDeclaredConstructor(args);
		} catch (Throwable t) {
			throw new IllegalArgumentException("Failed to obtain constructor(ObjectPool) for " + clazz + ": " + t);
		}
	}

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
