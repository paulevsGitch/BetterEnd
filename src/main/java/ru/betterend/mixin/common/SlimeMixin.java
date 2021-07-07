package ru.betterend.mixin.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.entity.monster.Slime;
import ru.betterend.interfaces.ISlime;

@Mixin(Slime.class)
public abstract class SlimeMixin extends Entity implements ISlime {
	public SlimeMixin(EntityType<? extends Slime> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	protected void setSize(int size, boolean heal) {}
	
	@Override
	public void be_setSlimeSize(int size, boolean heal) {
		setSize(size, heal);
	}

	@Override
	public void entityRemove(Entity.RemovalReason removalReason){
		super.remove(removalReason);
	}
}
