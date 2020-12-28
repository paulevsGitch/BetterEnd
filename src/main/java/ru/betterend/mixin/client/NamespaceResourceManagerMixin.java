package ru.betterend.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.patterns.BlockPatterned;

@Mixin(NamespaceResourceManager.class)
public abstract class NamespaceResourceManagerMixin {
	
	@Final
	@Shadow
	protected List<ResourcePack> packList;
	
	@Shadow
	public abstract Resource getResource(Identifier id);
	@Shadow
	public abstract List<Resource> getAllResources(Identifier id);
	
	@Inject(method = "getAllResources", cancellable = true, at = @At(
		value = "NEW",
		target = "java/io/FileNotFoundException",
		shift = Shift.BEFORE))
	public void getStatesPattern(Identifier id, CallbackInfoReturnable<List<Resource>> info) {
		if (id.getNamespace().equals(BetterEnd.MOD_ID)) {
			String[] data = id.getPath().split("/");
			if (data.length > 1) {
				Identifier blockId = BetterEnd.makeID(data[1].replace(".json", ""));
				Block block = Registry.BLOCK.get(blockId);
				if (block instanceof BlockPatterned) {
					Identifier stateId = ((BlockPatterned) block).statePatternId();
					try {
						List<Resource> resources = Lists.newArrayList();
						Resource stateRes = this.getResource(stateId);
						resources.add(stateRes);
						info.setReturnValue(resources);
						info.cancel();
					} catch (Exception ex) {
						BetterEnd.LOGGER.catching(ex);
					}
				}
			}
		}
	}
}
