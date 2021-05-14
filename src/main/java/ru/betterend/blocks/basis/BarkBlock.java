package ru.betterend.blocks.basis;

import java.io.Reader;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Triple;
import ru.betterend.patterns.Patterns;

public class BarkBlock extends EndPillarBlock {
	public BarkBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public String getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(Patterns.BLOCK_BASE, getName(blockId), blockId.getPath());
	}

	@Override
	public String getStatesPattern(Reader data) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(data, getName(blockId), blockId.getPath());
	}

	@Override
	public Triple<ResourceLocation, MultiVariant, BlockModel> getBlockModels(BlockState blockState) {

		return null;
	}

	private String getName(ResourceLocation blockId) {
		String name = blockId.getPath();
		return name.replace("_bark", "_log_side");
	}
}
