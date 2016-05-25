package org.jwave.model.editor;

public class SimpleSampleInfoImpl implements SimpleSampleInfo {
	private final float left;
	private final float right;
	
	public SimpleSampleInfoImpl(float left, float right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public float getLeftChannel() {
		return this.left;
	}

	@Override
	public float getRightChannel() {
		return this.right;
	}
}
