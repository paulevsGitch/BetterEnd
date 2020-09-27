package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.tag.Tag;

@Mixin(targets = "net.minecraft.tag.RequiredTagList$TagWrapper")
public interface TagAccessor<T> {
	@Accessor
	Tag<T> getDelegate();
}
