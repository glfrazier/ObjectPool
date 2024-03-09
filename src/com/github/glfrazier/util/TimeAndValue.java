package com.github.glfrazier.util;

public class TimeAndValue implements Comparable<TimeAndValue> {

	public final long time;
	public final float value;
	
	public TimeAndValue(long time, float value) {
		this.time = time;
		this.value = value;
	}

	@Override
	public int compareTo(TimeAndValue o) {
		return Long.compare(time, o.time);
	}
}
