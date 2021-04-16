package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.google.gson.Gson;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;

@Mixin(BlockModelDefinition.Context.class)
public interface ContextGsonAccessor {
	@Accessor
	Gson getGson();
}
