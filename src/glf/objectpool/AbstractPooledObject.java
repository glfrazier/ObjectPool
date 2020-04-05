package glf.objectpool;


public class AbstractPooledObject implements Poolable {

	private boolean allocated = false;
	private ObjectPool<AbstractPooledObject> pool;

	
	@SuppressWarnings("unchecked")
	public AbstractPooledObject(ObjectPool<?> pool) {
		this.pool = (ObjectPool<AbstractPooledObject>)pool;
	}
	
	protected synchronized void initialize() {
		if (allocated) {
			throw new IllegalStateException("Initializing an already-allocated instance.");
		}
		allocated = true;
	}
	
	public synchronized void release() {
		if (!allocated) {
			throw new IllegalStateException("Freeing an already-freed instance.");
		}
		allocated = false;
		pool.releaseInstance(this);
	}

}
