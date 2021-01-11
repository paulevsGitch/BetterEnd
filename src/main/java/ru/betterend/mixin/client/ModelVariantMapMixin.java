package ru.betterend.mixin.client;

import java.io.Reader;
import java.io.StringReader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.Gson;

import net.minecraft.block.Block;
import net.minecraft.client.render.model.json.ModelVariantMap;
import net.minecraft.util.JsonHelper;
import ru.betterend.patterns.BlockPatterned;

@Mixin(ModelVariantMap.class)
public abstract class ModelVariantMapMixin {
	
	@Inject(method = "deserialize", at = @At("HEAD"), cancellable = true)
	private static void be_deserializeBlockState(ModelVariantMap.DeserializationContext context, Reader reader, CallbackInfoReturnable<ModelVariantMap> info) {
		Block block = context.getStateFactory().getDefaultState().getBlock();
		if (block instanceof BlockPatterned) {
			String pattern = ((BlockPatterned) block).getStatesPattern(reader);
			Gson gson = ContextGsonAccessor.class.cast(context).getGson();
			ModelVariantMap map = JsonHelper.deserialize(gson, new StringReader(pattern), ModelVariantMap.class);
			info.setReturnValue(map);
		}
	}
}
