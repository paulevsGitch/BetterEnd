package ru.betterend.mixin.common;

import net.minecraft.world.entity.ai.behavior.ShufflingList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ru.betterend.util.ShuffelingListExtended;

import java.util.List;

@Mixin(ShufflingList.class)
public abstract class ShuffelingListMixin<U> implements ShuffelingListExtended<U> {
	@Shadow
	@Final
	protected List<ShufflingList.WeightedEntry<U>> entries;
	
	public boolean isEmpty() {
		return this.entries.isEmpty();
	}
	
	@Shadow
	public abstract ShufflingList<U> shuffle();
	
	public U getOne() {
		return this.shuffle().stream().findFirst().orElseThrow(RuntimeException::new);
	}
}
