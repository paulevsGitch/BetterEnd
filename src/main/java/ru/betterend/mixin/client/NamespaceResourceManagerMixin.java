package ru.betterend.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.block.Block;
import ru.betterend.BetterEnd;
import ru.betterend.client.models.BlockModelProvider;

@Mixin(FallbackResourceManager.class)
public abstract class NamespaceResourceManagerMixin {
	
	@Shadow
	public abstract Resource getResource(ResourceLocation id);
	
	@Inject(method = "getResources", cancellable = true, at = @At(
		value = "NEW",
		target = "java/io/FileNotFoundException",
		shift = Shift.BEFORE))
	public void be_getStatesPattern(ResourceLocation id, CallbackInfoReturnable<List<Resource>> info) {
		if (id.getNamespace().equals(BetterEnd.MOD_ID)) {
			String[] data = id.getPath().split("/");
			if (data.length > 1) {
				ResourceLocation blockId = BetterEnd.makeID(data[1].replace(".json", ""));
				Block block = Registry.BLOCK.get(blockId);
				if (block instanceof BlockModelProvider) {
					ResourceLocation stateId = ((BlockModelProvider) block).statePatternId();
					try {
						List<Resource> resources = Lists.newArrayList();
						Resource stateRes = this.getResource(stateId);
						resources.add(stateRes);
						info.setReturnValue(resources);
					} catch (Exception ex) {
						BetterEnd.LOGGER.catching(ex);
					}
				}
			}
		}
	}
}
