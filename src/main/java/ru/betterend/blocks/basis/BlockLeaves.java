package ru.betterend.blocks.basis;

import java.io.Reader;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.MaterialColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class BlockLeaves extends LeavesBlock implements BlockPatterned, IRenderTypeable {
	public BlockLeaves(MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)
				.allowsSpawning((state, world, pos, type) -> { return false; })
				.suffocates((state, world, pos) -> { return false; })
				.blockVision((state, world, pos) -> { return false; })
				.materialColor(color));
	}
	
	public BlockLeaves(MaterialColor color, int light) {
		super(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)
				.allowsSpawning((state, world, pos, type) -> { return false; })
				.suffocates((state, world, pos) -> { return false; })
				.blockVision((state, world, pos) -> { return false; })
				.materialColor(color)
				.luminance(light));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String blockId = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(data, blockId, blockId);
	}
	
	@Override
	public String getModelPattern(String block) {
		String blockId = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(Patterns.BLOCK_BASE, blockId, blockId);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_SIMPLE;
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}
