package ru.betterend.mixin.common;

import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ru.betterend.interfaces.ISlime;

@Mixin(Slime.class)
public class SlimeMixin implements ISlime {
	@Shadow
	protected void setSize(int size, boolean heal) {}
	
	@Override
	public void be_setSlimeSize(int size, boolean heal) {
		setSize(size, heal);
	}
}
