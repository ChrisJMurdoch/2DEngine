package core;

/** Custom class to abstract and modularise single-threaded repeating tasks with monitoring tools */
public abstract class AbstractEngine implements Runnable {
	
	/** Public flag used to stop process */
	public static boolean running;
	
	/** Start time of last tick, used to determine next tick's measurements */
	private long tick;
	
	/** Queue to store frame durations in nanoseconds */
	private final double[] fps;
	private int fpsIndex = 0;
	
	public AbstractEngine() {
		running = true;
		tick = System.nanoTime();
		fps = new double[500];
	}
	
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		while(running) {

			// Get starting time
			long start = System.nanoTime();
			long nanoSeconds = start - tick;
			
			// calculate time difference
			double seconds = (double)nanoSeconds / 1000000000;
			
			// Complete task in-thread
			System.out.println(1/seconds);
			tick(seconds);
			
			// Add duration to queue for monitoring
			fps[fpsIndex++] = seconds;
			fpsIndex %= fps.length;
			
			// Save tick starting time
			tick = start;
		}
		end();
	}
	
	public int getFrameRate() {
		double sum = 0;
		for (int i=0; i<fps.length; i++) {
			if (fps[i] > 0) {
				sum += fps[i];
			} else {
				return (int) (sum == 0 ? 1 : i / sum);
			}
		}
		return (int) (fps.length / sum);
	}
	
	/** The main task of the engine, it will be performed sequentially as fast
	 *  as possible and passed in the duration of the last tick in seconds */
	public abstract void tick(double secondsElapsed);
	
	/** performed when enggine successfully exits */
	public void end() {}
}
