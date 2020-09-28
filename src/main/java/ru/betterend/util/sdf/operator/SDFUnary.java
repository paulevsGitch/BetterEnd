package ru.betterend.util.sdf.operator;

import ru.betterend.util.sdf.ISDF;

public abstract class SDFUnary implements ISDF {
	protected ISDF source;
	
	public SDFUnary setSource(ISDF source) {
		this.source = source;
		return this;
	}
}
