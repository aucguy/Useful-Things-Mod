package com.aucguy.usefulthings.util;

import java.util.Iterator;

/**
 * goes over multiple iterators in one iterator. It goes over all the elements of one iterator then the next, then the
 * next, etc.
 * 
 * @author aucguy
 */
public class ComboIter<T> implements Iterable<T>, Iterator<T> {
	/**
	 * the iterators this iterator goes over
	 */
	Iterator<T>[] iterators;
	
	/**
	 * the index of the current iterator
	 */
	int current;
	
	@SuppressWarnings("unchecked")
	public ComboIter(Iterable<T> ... iterators) {
		this.iterators = new Iterator[iterators.length];
		
		for(int i=0; i<this.iterators.length; i++) {
			this.iterators[i] = iterators[i].iterator();
		}
	}
	
	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		//if any of the next iterators have more elements
		for(int i=this.current; i<this.iterators.length; i++) {
			if(this.iterators[i].hasNext()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public T next() {
		if(this.iterators[this.current].hasNext()) {
			return this.iterators[this.current].next();
		}
		return this.iterators[++this.current].next();
	}
	
	@Override
	public void remove() {
		this.iterators[this.current].remove();
	}
}