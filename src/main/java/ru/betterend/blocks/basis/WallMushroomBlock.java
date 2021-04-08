package ru.betterend.blocks.basis;

import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldView;

public class WallMushroomBlock extends EndWallPlantBlock {
	public WallMushroomBlock(int light) {
		super(FabricBlockSettings.of(Material.PLANT).breakByTool(FabricToolTags.AXES).sounds(SoundType.GRASS)
				.luminance(light).sounds(SoundType.WOOD).hardness(0.2F).breakByHand(true).noCollision());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Lists.newArrayList(new ItemStack(this));
	}

	@Override
	public boolean isSupport(WorldView world, BlockPos pos, BlockState blockState, Direction direction) {
		return blockState.getMaterial().isSolid() && blockState.isSideSolidFullSquare(world, pos, direction);
	}
}
