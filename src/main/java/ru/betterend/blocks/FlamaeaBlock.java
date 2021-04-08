package ru.betterend.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.AbstractBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.interfaces.ISpetialItem;

public class FlamaeaBlock extends EndPlantBlock implements ISpetialItem {
	private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 1, 16);

	public FlamaeaBlock() {
		super(FabricBlockSettings.of(Material.PLANT).breakByTool(FabricToolTags.SHEARS).sounds(SoundType.WET_GRASS)
				.breakByHand(true));
	}

	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(Blocks.WATER);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPE;
	}

	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.NONE;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Lists.newArrayList(new ItemStack(this));
	}

	@Override
	public int getStackSize() {
		return 64;
	}

	@Override
	public boolean canPlaceOnWater() {
		return true;
	}
}
