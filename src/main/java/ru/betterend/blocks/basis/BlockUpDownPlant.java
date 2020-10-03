package ru.betterend.blocks.basis;

import java.util.List;
import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.client.ERenderLayer;
import ru.betterend.client.IRenderTypeable;
import ru.betterend.registry.BlockTagRegistry;

public class BlockUpDownPlant extends BlockBaseNotFull implements IRenderTypeable {
	private static final VoxelShape SHAPE = Block.createCuboidShape(4, 0, 4, 12, 16, 12);
	
	public BlockUpDownPlant() {
		super(FabricBlockSettings.of(Material.PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.GRASS)
				.breakByHand(true)
				.noCollision());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		Vec3d vec3d = state.getModelOffset(view, pos);
		return SHAPE.offset(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.XZ;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState down = world.getBlockState(pos.down());
		return isTerrain(down);
	}
	
	protected boolean isTerrain(BlockState state) {
		return state.isIn(BlockTagRegistry.END_GROUND);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return Blocks.AIR.getDefaultState();
		}
		else {
			return state;
		}
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootContextParameters.TOOL);
		if (tool != null && tool.getItem().isIn(FabricToolTags.SHEARS) || EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Lists.newArrayList(new ItemStack(this));
		}
		else {
			return Lists.newArrayList();
		}
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}
