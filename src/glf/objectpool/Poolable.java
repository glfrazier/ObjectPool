package glf.objectpool;

/**
 * Implemented by objects that reside in object pools. It can also be
 * implemented by objects that are not pooled. This means that a service that
 * releases objects when finished with them can process objects that are not
 * allocated from a pool. They implement <code>release()</code> as a no-op, and
 * the object is garbage collected as normal.
 * 
 * @author glfrazier
 *
 */
public interface Poolable {

	/**
	 * If this object is pooled, this method releases it back into the pool,
	 * allowing it to be re-allocated.
	 */
	public void release();

}
