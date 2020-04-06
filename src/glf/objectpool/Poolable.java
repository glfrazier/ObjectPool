package glf.objectpool;

/**
 * Implemented by objects that reside in object pools. It can also be
 * implemented by objects that are not pooled, with a no-op implementation of
 * {@link #release()}. This means that a service that releases objects when
 * finished with them can process objects that are not allocated from a pool.
 * 
 * @author Greg Frazier
 *
 */
public interface Poolable {

	/**
	 * If this object is pooled, this method releases it back into the pool,
	 * allowing it to be re-allocated.
	 */
	public void release();

}
