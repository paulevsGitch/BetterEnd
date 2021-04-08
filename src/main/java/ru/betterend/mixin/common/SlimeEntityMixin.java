package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.entity.mob.SlimeEntity;
import ru.betterend.interfaces.ISlime;

@Mixin(SlimeEntity.class)
public class SlimeEntityMixin implements ISlime {
	@Shadow
	protected void setSize(int size, boolean heal) {
	}

	@Override
	public void beSetSlimeSize(int size, boolean heal) {
		setSize(size, heal);
	}
}
