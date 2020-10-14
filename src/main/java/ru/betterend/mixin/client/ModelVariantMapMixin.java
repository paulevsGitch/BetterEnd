package ru.betterend.mixin.client;

import java.io.Reader;
import java.io.StringReader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.client.render.model.json.ModelVariantMap;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.interfaces.IdentifiedContext;
import ru.betterend.interfaces.Patterned;

@Mixin(ModelVariantMap.class)
public abstract class ModelVariantMapMixin {
	
	@Shadow
	static ModelVariantMap deserialize(ModelVariantMap.DeserializationContext context, Reader reader) {
		return null;
	}
	
	@Inject(method = "deserialize", at = @At("HEAD"), cancellable = true)
	private static void deserializeBlockState(ModelVariantMap.DeserializationContext context, Reader reader, CallbackInfoReturnable<ModelVariantMap> info) {
		IdentifiedContext idContext = IdentifiedContext.class.cast(context);
		Identifier id = idContext.getContextId();
		if (id != null && id.getPath().contains("pattern")) {
			String[] data = id.getPath().split("/");
			Identifier blockId = new Identifier(id.getNamespace(), data[1]);
			Block block = Registry.BLOCK.get(blockId);
			idContext.removeId();
			if (block instanceof Patterned) {
				String pattern = ((Patterned) block).getStatesPattern(reader);
				if (data[1].contains("violecite")) {
					System.out.println(pattern);
				}
				info.setReturnValue(deserialize(context, new StringReader(pattern)));
				info.cancel();
			}
		}
	}
}
