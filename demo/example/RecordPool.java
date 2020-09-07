package example;

import com.github.glfrazier.objectpool.ObjectPool;

/**
 * An object pool that holds instances of {@link Record}.
 * 
 * @author Greg Frazier
 *
 */
public class RecordPool extends ObjectPool<Record> {

	public RecordPool() {
		super(Record.class);
	}
	
	public Record allocate(int a, String name) {
		Record r = super.getInstance();
		r.initialize(a, name);
		return r;
	}
	
}
