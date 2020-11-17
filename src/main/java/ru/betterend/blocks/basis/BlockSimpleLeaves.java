package ru.betterend.blocks.basis;

import java.io.Reader;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.Patterns;

public class BlockSimpleLeaves extends BlockBaseNotFull implements IRenderTypeable {
	public BlockSimpleLeaves(MaterialColor color) {
		super(FabricBlockSettings.of(Material.LEAVES)
				.strength(0.2F)
				.sounds(BlockSoundGroup.GRASS)
				.nonOpaque()
				.allowsSpawning((state, world, pos, type) -> { return false; })
				.suffocates((state, world, pos) -> { return false; })
				.blockVision((state, world, pos) -> { return false; })
				.materialColor(color));
	}
	
	public BlockSimpleLeaves(MaterialColor color, int light) {
		super(FabricBlockSettings.of(Material.LEAVES)
				.strength(0.2F)
				.sounds(BlockSoundGroup.GRASS)
				.nonOpaque()
				.luminance(light)
				.allowsSpawning((state, world, pos, type) -> { return false; })
				.suffocates((state, world, pos) -> { return false; })
				.blockVision((state, world, pos) -> { return false; })
				.materialColor(color));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String texture = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(data, texture, texture);
	}
	
	@Override
	public String getModelPattern(String block) {
		String texture = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(Patterns.BLOCK_BASE, texture, texture);
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