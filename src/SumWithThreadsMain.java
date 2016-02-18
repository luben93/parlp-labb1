import java.util.Random;


public class SumWithThreadsMain {

	public static void main(String[] args) throws Exception {

		//if(args.length < 1) {
		//	System.out.println("Usage: java SumWithThreads <no-of-threads>");
		//	System.exit(0);
		//}

		
		final int n = 8; // no of threads
		System.out.println("Threads the old way, threads = " + n);

		
		// Generate data
		final int SIZE = (int) 1E8;
		int[] arr = new int[SIZE];
		Random rand = new Random();
		for (int i = 0; i < arr.length; i++) {
			arr[i] = rand.nextInt(10);
		}

		
		// Warm up, to get the JIT compiler going
		long sum = calculateSum(n, arr);
		System.out.println("Warm up complete, " + sum);


		// Measuring
		for(int i = 0; i < 10; i++) {
			System.gc();
			Thread.sleep(5000);

			long start = System.nanoTime();
			calculateSum(n, arr);
			long elapsed = System.nanoTime() - start;

			System.out.println(elapsed/1.0E9 + " s, (sum " + sum + ")");
		}
	}

	// The calculation
	public static long calculateSum(int n, int[] arr) throws Exception {

		final int step = arr.length/n;

		SumThread[] threads = new SumThread[n];
		for(int i = 0; i < n; i++) {
			int lo = i*step, hi = lo + step;
			threads[i] = new SumThread(arr, lo, hi);
			threads[i].setPriority(Thread.MAX_PRIORITY); // hmm...
		}

		for(int i = 0; i < threads.length; i++) {
			threads[i].start();
		}

		for(int i = 0; i < threads.length; i++) {
			threads[i].join();
		}

		long superSum = 0;
		for(int i = 0; i < threads.length; i++) {
			superSum += threads[i].getSum();
		}

		return superSum;
	}

}

class SumThread extends Thread {

	SumThread(int[] arr, int lo, int hi) {
		sum = 0;
		this.hi = hi; this.lo = lo;
		this.arr = arr;
	}

	public void run() {
		for(int i = lo; i < hi; i++) {
			sum += arr[i];
		}
	}

	long getSum() {
		return sum;
	}

	private int hi, lo;
	private long sum;
	private int[] arr;
}
