package org.jwave.model.editor;

import java.util.ArrayList;
import java.util.List;

public class CutImpl implements Cut {
	private int from;
	private int to;
	private final List<Pair<Integer, Integer>> segments;
	
	public CutImpl(int from, int to, ArrayList<Pair<Integer, Integer>> segments) {
		this.from = from;
		this.to = to;
		this.segments = new ArrayList<>(segments);
	}
	
	@Override
	public int getCutFrom() {
		return this.from;
	}	
	
	@Override
	public int getCutTo() {
		return this.to;
	}
	
	@Override
	public void setCutFrom(int from) {
		this.from = from;
	}	
	
	@Override
	public void setCutTo(int to) {
		this.to = to;
	}
	
	@Override
	public int getCutLength() {
		return this.to - this.from;
	}
	
	@Override
	public List<Pair<Integer, Integer>> getSegments() {
		return this.segments;
	}
}
