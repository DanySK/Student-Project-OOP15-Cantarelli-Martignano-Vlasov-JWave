package org.jwave.model.editor;

public class GroupedSampleInfoImpl implements GroupedSampleInfo {
	private final float leftMax;
	private final float leftMin;
	private final float leftPosRMS;
	private final float leftNegRMS;
	
	private final float rightMax;
	private final float rightMin;
	private final float rightPosRMS;
	private final float rightNegRMS;	
	
	public GroupedSampleInfoImpl(float leftMax, float leftMin, float leftPosRMS, float leftNegRMS,
								 float rightMax, float rightMin, float rightPosRMS, float rightNegRMS) {
		this.leftMax = leftMax;
		this.leftMin = leftMin;
		this.leftPosRMS = leftPosRMS;
		this.leftNegRMS = leftNegRMS;
		
		this.rightMax = rightMax;
		this.rightMin = rightMin;
		this.rightPosRMS = rightPosRMS;
		this.rightNegRMS = rightNegRMS;
	}

	@Override
	public float getLeftChannelMax() {
		return this.leftMax;
	}

	@Override
	public float getLeftChannelMin() {
		return this.leftMin;
	}

	@Override
	public float getLeftChannelPositiveRMS() {
		return this.leftPosRMS;
	}

	@Override
	public float getLeftChannelNegativeRMS() {
		return this.leftNegRMS;
	}

	@Override
	public float getRightChannelMax() {
		return this.rightMax;
	}

	@Override
	public float getRightChannelMin() {
		return this.rightMin;
	}

	@Override
	public float getRightChannelPositiveRMS() {
		return this.rightPosRMS;
	}

	@Override
	public float getRightChannelNegativeRMS() {
		return this.rightNegRMS;
	}

}
