package example;

import java.lang.reflect.Constructor;

import glf.objectpool.AbstractPooledObject;
import glf.objectpool.ObjectPool;

public class Record extends AbstractPooledObject {

	private int a;
	private String name;
	
	public Record(ObjectPool<Record> pool) {
		super(pool);
	}
	
	public synchronized void initialize(int a, String name) {
		super.initialize();
		this.a = a;
		this.name = name;
	}
	
	public int getA() {
		return a;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static void main(String[] args) {
		for(Constructor<?> c : Record.class.getDeclaredConstructors()) {
			System.out.println("Constructor: " + c);
		}
	}
}
