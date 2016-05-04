package org.jwave.model.editor;

import java.util.ArrayList;
import java.util.List;

public class Cut {
	private int msFrom;
	private int msTo;
	private final List<Pair<Integer, Integer>> singleSegments;
	
	public Cut(int from, int to, ArrayList<Pair<Integer, Integer>> segments) {
		msFrom = from;
		msTo = to;
		singleSegments = new ArrayList<>(segments);
	}
	
	public int getCutTo() {
		return this.msTo;
	}
	
	public int getCutFrom() {
		return this.msFrom;
	}
	
	public void setCutTo(int to) {
		this.msTo = to;
	}
	
	public void setCutFrom(int from) {
		this.msFrom = from;
	}
	
	public int getCutLength() {
		return this.msTo - this.msFrom;
	}
	
	public List<Pair<Integer, Integer>> getSegments() {
		return this.singleSegments;
	}
}
