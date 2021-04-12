package ru.betterend.mixin.common;

import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ru.betterend.interfaces.ISlime;

@Mixin(Slime.class)
public class SlimeEntityMixin implements ISlime {
	@Shadow
	protected void setSize(int size, boolean heal) {}
	
	@Override
	public void beSetSlimeSize(int size, boolean heal) {
		setSize(size, heal);
	}
}
