package ru.betterend.blocks.basis;

import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;

public class WallMushroomBlock extends EndWallPlantBlock {
	public WallMushroomBlock(int light) {
		super(FabricBlockSettings.of(Material.PLANT)
				.breakByTool(FabricToolTags.AXES)
				.sounds(BlockSoundGroup.GRASS)
				.luminance(light)
				.sounds(BlockSoundGroup.WOOD)
				.hardness(0.2F)
				.breakByHand(true)
				.noCollision());
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Lists.newArrayList(new ItemStack(this));
	}
	
	@Override
	public boolean isSupport(WorldView world, BlockPos pos, BlockState blockState, Direction direction) {
		return blockState.getMaterial().isSolid() && blockState.isSideSolidFullSquare(world, pos, direction);
	}
}
