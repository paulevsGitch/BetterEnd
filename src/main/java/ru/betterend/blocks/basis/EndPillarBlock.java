package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.patterns.BlockModelProvider;
import ru.betterend.patterns.Patterns;

public class EndPillarBlock extends RotatedPillarBlock implements BlockModelProvider {
	public EndPillarBlock(Properties settings) {
		super(settings);
	}
	
	public EndPillarBlock(Block block) {
		super(FabricBlockSettings.copyOf(block));
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public BlockModel getModel() {
		return null;
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String texture = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, texture, texture);
	}
	
	@Override
	public String getModelString(String block) {
		return createBlockPattern();
	}
	
	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_PILLAR;
	}

	@Override
	public BlockModel getBlockModel(BlockState blockState) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		BlockModel model = BlockModel.fromString(createBlockPattern());
		ResourceLocation modelLoc = new ResourceLocation(blockId.getNamespace(), "blocks/" + blockId.getPath());
		model.name = modelLoc.toString();
		return model;
	}

	@Override
	public MultiVariant getModelVariant(ResourceLocation resourceLocation, BlockState blockState) {
		Direction.Axis axis = blockState.getValue(AXIS);
		Transformation transform = Transformation.identity();
		switch (axis) {
			case X: {
				transform = new Transformation(null, Vector3f.ZP.rotationDegrees(90), null, null);
				break;
			}
			case Z: {
				transform = new Transformation(null, Vector3f.XP.rotationDegrees(90), null, null);
				break;
			}
		}
		Variant variant = new Variant(resourceLocation, transform, false, 1);
		return new MultiVariant(Collections.singletonList(variant));
	}

	private String createBlockPattern() {
		String texture = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createBlockPillar(texture);
	}
}
