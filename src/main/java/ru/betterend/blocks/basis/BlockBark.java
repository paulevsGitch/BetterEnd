package ru.betterend.blocks.basis;

import java.io.Reader;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.patterns.Patterns;

public class BlockBark extends BlockPillar {
	public BlockBark(Settings settings) {
		super(settings);
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterns.createJson(Patterns.BLOCK_BASE, getName(blockId), blockId.getPath());
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterns.createJson(data, getName(blockId), blockId.getPath());
	}
	
	private String getName(Identifier blockId) {
		String name = blockId.getPath();
		return name.replace("_bark", "_log_side");
	}
}
