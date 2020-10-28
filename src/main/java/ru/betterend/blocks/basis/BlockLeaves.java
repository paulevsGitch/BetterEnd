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
import ru.betterend.interfaces.Patterned;

public class BlockLeaves extends LeavesBlock implements Patterned, IRenderTypeable {
	public BlockLeaves(MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).materialColor(color));
	}
	
	public BlockLeaves(MaterialColor color, int light) {
		super(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).materialColor(color).luminance(light));
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
