package example;

public class Demo extends Thread {
	static RecordPool pool = new RecordPool();

	public Demo() {
		super();
	}

	public void run() {
		Record record[] = new Record[20];
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				record[j] = pool.allocate(j, "My name is " + j);
			}
			Thread.yield();
			for (int j = 0; j < 20; j++) {
				record[j].release();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		int n = 40;
		Demo demo[] = new Demo[n];
		for (int i = 0; i < n; i++) {
			demo[i] = new Demo();
			demo[i].start();
		}
		for (int i = 0; i < n; i++) {
			demo[i].join();
		}
		System.out.println("Pool size = " + pool.size() + ", total records constructed = " + pool.getTotalConstructed()
				+ ", Number of allocations = " + pool.getNumberOfAllocations());
	}
}
