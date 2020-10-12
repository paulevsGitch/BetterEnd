package ru.betterend.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.interfaces.Patterned;

@Mixin(NamespaceResourceManager.class)
public abstract class NamespaceResourceManagerMixin {
	
	@Shadow
	public abstract Resource getResource(Identifier id);
	
	@Inject(method = "getAllResources", at = @At("HEAD"), cancellable = true)
	public void getAllResources(Identifier id, CallbackInfoReturnable<List<Resource>> info) {
		if (id.getNamespace().contains(BetterEnd.MOD_ID)) {
			String[] data = id.getPath().split("/");
			if (data.length > 1) {
				Identifier blockId = BetterEnd.makeID(data[1].replace(".json", ""));
				Block block = Registry.BLOCK.get(blockId);
				if (block instanceof Patterned) {
					List<Resource> resources = Lists.newArrayList();
					resources.add(this.getResource(((Patterned) block).statePatternId()));
					info.setReturnValue(resources);
					info.cancel();
				}
			}
		}
	}
}
