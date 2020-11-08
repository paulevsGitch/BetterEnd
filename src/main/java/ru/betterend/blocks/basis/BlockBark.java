package ru.betterend.blocks.basis;

import java.io.Reader;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.interfaces.Patterned;

public class BlockBark extends BlockPillar {
	public BlockBark(Settings settings) {
		super(settings);
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		String name = getName(blockId);
		return Patterned.createJson(Patterned.BLOCK_BASE, BetterEnd.makeID(name), blockId.getPath());
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		String name = getName(blockId);
		return Patterned.createJson(data, BetterEnd.makeID(name), blockId.getPath());
	}
	
	private String getName(Identifier blockId) {
		String name = blockId.getPath();
		return name.replace("_bark", "_log_side");
	}
}
