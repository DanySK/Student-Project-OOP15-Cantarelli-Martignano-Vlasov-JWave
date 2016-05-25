package org.jwave.model.editor;

public class SimpleSampleInfoImpl implements SimpleSampleInfo {
	private final int left;
	private final int right;
	
	public SimpleSampleInfoImpl(int left, int right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public int getLeftChannel() {
		return this.left;
	}

	@Override
	public int getRightChannel() {
		return this.right;
	}
}
