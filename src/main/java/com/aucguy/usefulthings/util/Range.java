package com.aucguy.usefulthings.util;

import java.util.Iterator;

/**
 * goes over a series of numbers. This is like python's range (for 3) or xrange (for 2) function
 * 
 * @author aucguy
 */
public class Range implements Iterable<Integer>, Iterator<Integer> {
	protected int start;
	protected int end;
	/**
	 * the current state of the iterator
	 */
	protected int index;
	
	/**
	 * creates a range iterator
	 * 
	 * @param start - the first element in the sequence
	 * @param end - one more than the last element in the sequence
	 */
	public Range(int start, int end) {
		this.start = start;
		this.end = end;
		this.index = start;
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return this.index != this.end;
	}

	@Override
	public Integer next() {
		return this.index++;
	}

	@Override
	public void remove() {
		throw(new UnsupportedOperationException("there is no underlying sequence object for a range"));
	}
}
