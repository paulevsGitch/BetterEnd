package ru.betterend.util.sdf.operator;

import ru.betterend.util.sdf.ISDF;

public abstract class SDFBinary implements ISDF {
	protected ISDF sourceA;
	protected ISDF sourceB;
	
	public SDFBinary setSourceA(ISDF sourceA) {
		this.sourceA = sourceA;
		return this;
	}
	
	public SDFBinary setSourceB(ISDF sourceB) {
		this.sourceB = sourceB;
		return this;
	}
}
