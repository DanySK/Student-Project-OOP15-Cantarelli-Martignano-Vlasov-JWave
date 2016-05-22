package org.jwave.model.editor;

import java.util.ArrayList;
import java.util.List;

public class CutImpl implements Cut {
	private int msFrom;
	private int msTo;
	private final List<Pair<Integer, Integer>> singleSegments;
	
	public CutImpl(int from, int to, ArrayList<Pair<Integer, Integer>> segments) {
		msFrom = from;
		msTo = to;
		singleSegments = new ArrayList<>(segments);
	}
	
	@Override
	public int getCutFrom() {
		return this.msFrom;
	}	
	
	@Override
	public int getCutTo() {
		return this.msTo;
	}
	
	@Override
	public void setCutFrom(int from) {
		this.msFrom = from;
	}	
	
	@Override
	public void setCutTo(int to) {
		this.msTo = to;
	}
	
	@Override
	public int getCutLength() {
		return this.msTo - this.msFrom;
	}
	
	@Override
	public List<Pair<Integer, Integer>> getSegments() {
		return this.singleSegments;
	}
}
