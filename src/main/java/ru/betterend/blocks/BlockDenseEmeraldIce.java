package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;

public class BlockDenseEmeraldIce extends BlockBase implements IRenderTypeable {
	public BlockDenseEmeraldIce() {
		super(FabricBlockSettings.copyOf(Blocks.PACKED_ICE));
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.TRANSLUCENT;
	}
}
