package example;

import glf.objectpool.ObjectPool;

public class RecordPool extends ObjectPool<Record> {

	public RecordPool() {
		super(Record.class);
	}
	
	public synchronized Record allocate(int a, String name) {
		Record r = super.getInstance();
		r.initialize(a, name);
		return r;
	}
	
}
