package ru.betterend.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.google.gson.Gson;

import net.minecraft.client.render.model.json.ModelVariantMap;

@Mixin(ModelVariantMap.DeserializationContext.class)
public interface ContextGsonAccessor {
	@Accessor
	public Gson getGson();
}
