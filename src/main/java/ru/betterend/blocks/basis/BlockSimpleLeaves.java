package ru.betterend.blocks.basis;

import java.io.Reader;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.client.ERenderLayer;
import ru.betterend.client.IRenderTypeable;
import ru.betterend.interfaces.Patterned;

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
				.lightLevel(light)
				.allowsSpawning((state, world, pos, type) -> { return false; })
				.suffocates((state, world, pos) -> { return false; })
				.blockVision((state, world, pos) -> { return false; })
				.materialColor(color));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterned.createJson(data, blockId, blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterned.createJson(Patterned.BASE_BLOCK_MODEL, blockId, blockId.getPath());
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterned.BLOCK_STATES_PATTERN;
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}