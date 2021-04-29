package ru.betterend.blocks.basis;

import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;

public class WallMushroomBlock extends EndWallPlantBlock {
	public WallMushroomBlock(int light) {
		super(FabricBlockSettings.of(Material.PLANT)
				.breakByTool(FabricToolTags.AXES)
				.breakByHand(true)
				.luminance(light)
				.hardness(0.2F)
				.sound(SoundType.GRASS)
				.sound(SoundType.WOOD)
				.noCollission());
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Lists.newArrayList(new ItemStack(this));
	}
	
	@Override
	public boolean isSupport(LevelReader world, BlockPos pos, BlockState blockState, Direction direction) {
		return blockState.getMaterial().isSolid() && blockState.isFaceSturdy(world, pos, direction);
	}
}
