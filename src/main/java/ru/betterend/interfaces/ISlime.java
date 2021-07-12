package ru.betterend.interfaces;

import net.minecraft.world.entity.Entity;

public interface ISlime {
	public void be_setSlimeSize(int size, boolean heal);
	
	void entityRemove(Entity.RemovalReason removalReason);
}
