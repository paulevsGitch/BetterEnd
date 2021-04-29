package ru.betterend.mixin.client;

import java.io.Reader;
import java.io.StringReader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.Gson;

import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import ru.betterend.patterns.BlockPatterned;

@Mixin(BlockModelDefinition.class)
public abstract class ModelVariantMapMixin {
	
	@Inject(method = "fromStream", at = @At("HEAD"), cancellable = true)
	private static void be_deserializeBlockState(BlockModelDefinition.Context context, Reader reader, CallbackInfoReturnable<BlockModelDefinition> info) {
		Block block = context.getDefinition().any().getBlock();
		if (block instanceof BlockPatterned) {
			String pattern = ((BlockPatterned) block).getStatesPattern(reader);
			Gson gson = ContextGsonAccessor.class.cast(context).getGson();
			BlockModelDefinition map = GsonHelper.fromJson(gson, new StringReader(pattern), BlockModelDefinition.class);
			info.setReturnValue(map);
		}
	}
}
