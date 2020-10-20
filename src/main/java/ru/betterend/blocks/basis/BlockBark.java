package ru.betterend.blocks.basis;

import java.io.Reader;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.betterend.interfaces.Patterned;

public class BlockBark extends BlockPillar {
	public BlockBark(Settings settings) {
		super(settings);
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterned.createJson(Patterned.BASE_BLOCK_MODEL, blockId, getName(blockId));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterned.createJson(data, blockId, getName(blockId));
	}
	
	private String getName(Identifier blockId) {
		String name = blockId.getPath();
		return name.replace("_bark", "_log_side");
	}
}
