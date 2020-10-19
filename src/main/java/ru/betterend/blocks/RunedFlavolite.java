package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.BlockRegistry;

public class RunedFlavolite extends BlockBase {

	public RunedFlavolite() {
		super(FabricBlockSettings.copyOf(BlockRegistry.FLAVOLITE.polished).lightLevel(6));
	}
}
