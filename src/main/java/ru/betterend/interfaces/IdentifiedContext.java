package ru.betterend.interfaces;

import net.minecraft.util.Identifier;

public interface IdentifiedContext {
	public Identifier getContextId();
	public void setContextId(Identifier id);
	
	default void removeId() {
		this.setContextId(null);
	}
}
