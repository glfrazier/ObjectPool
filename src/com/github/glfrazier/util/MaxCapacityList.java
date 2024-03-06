package com.github.glfrazier.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

public class MaxCapacityList<E> extends LinkedList<E> {

	private static final long serialVersionUID = 1L;
	
	private Integer capacity=500;
	
	private Random rand = new Random();
	
	public void setCapacity(int len) {
		capacity = len;
	}

	public MaxCapacityList() {
		super();
	}
	
	public MaxCapacityList(int capacity) {
		this();
		this.capacity = capacity;
	}	
	
	@Override
	public boolean add(E item) {
		boolean result = super.add(item);
		if (size() > capacity) {
			removeRandomElement();
		}
		return result;
	}	
	
	@Override
	public boolean addAll(Collection<? extends E> items) {
		boolean result = super.addAll(items);
		while (size() > capacity) {
			remove(0);
		}
		return result;
	}
	
	private void removeRandomElement() {
		int index = rand.nextInt(size());
		remove(index);
	}

}
