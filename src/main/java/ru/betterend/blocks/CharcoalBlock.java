package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Blocks;
import ru.betterend.blocks.basis.BlockBase;

public class CharcoalBlock extends BlockBase {
	public CharcoalBlock() {
		super(FabricBlockSettings.copyOf(Blocks.COAL_BLOCK));
		FuelRegistry.INSTANCE.add(this, 16000);
	}
}
