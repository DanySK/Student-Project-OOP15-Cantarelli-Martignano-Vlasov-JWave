package org.jwave.model.editor;

public class SegmentImpl implements Segment {
	private final int from;
	private final int to;
	
	public SegmentImpl(int from, int to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public int getFrom() {
		return this.from;
	}

	@Override
	public int getTo() {
		return this.to;
	}

	@Override
	public int getLength() {
		return this.to - this.from;
	}

}
