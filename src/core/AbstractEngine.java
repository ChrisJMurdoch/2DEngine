package core;

/** Custom class to abstract and modularise single-threaded repeating tasks with monitoring tools */
public abstract class AbstractEngine implements Runnable {
	
	/** Static flag used to stop program */
	public static boolean running = true;
	
	/** Start time of last tick, used to determine next tick's measurements */
	private long lastTick;
	
	/** Queue to store frame durations in nanoseconds */
	private final double[] fps;
	private int fpsIndex;
	
	public AbstractEngine() {
		lastTick = System.nanoTime();
		fps = new double[500];
		fpsIndex = 0;
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
			long nanoSeconds = start - lastTick;
			
			// calculate time difference
			double seconds = (double)nanoSeconds / 1000000000;
			
			// Complete task in-thread
			tick(seconds);
			
			// Add duration to queue for monitoring
			fps[fpsIndex++] = seconds;
			fpsIndex %= fps.length;
			
			// Save tick starting time
			lastTick = start;
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
	
	/** performed when engine successfully exits */
	public void end() {}
}
