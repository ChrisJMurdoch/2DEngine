package core;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/** Class to track custom process durations */
public class Profiler {
	
	private static LinkedList<Process> processes = new LinkedList<>();
	private static Process process;
	
	protected static void startProcess(String name) {
		
		// Start
		long time;
		if (process != null) {
			time = process.end();
			processes.add(process);
		} else {
			time = System.nanoTime();
		}
		process = (new Process(name, time));
		
		// Clip
		if (processes.size() > 1000) {
			processes.remove(processes.size()-1);
		}
	}
	
	protected static void print() {
		Map<String, Long> data = new HashMap<>();
		long total = 0;
		for (Process i : processes) {
			total += i.duration;
			if (data.containsKey(i.name)) {
				data.put(i.name, data.get(i.name) + i.duration);
			} else {
				data.put(i.name, i.duration);
			}
		}
		Iterator<Entry<String, Long>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Long> current = it.next();
			float percent = (float)current.getValue() / (float)total * 100;
			System.out.println(current.getKey() + " - " + Double.parseDouble(new DecimalFormat("##.##").format(percent)) + "%" );
		}
		
	}
	
	private static class Process {
		private String name;
		private long start;
		private long duration;
		private Process(String name, long start) {
			this.name = name;
			this.start = start;
		}
		private long end() {
			long time = System.nanoTime();
			duration = time - start;
			return time;
		}
	}
}
