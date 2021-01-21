package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.LumecornShape;
import ru.betterend.blocks.basis.BaseBlockNotFull;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;

public class LumecornBlock extends BaseBlockNotFull implements IRenderTypeable {
	public static final EnumProperty<LumecornShape> SHAPE = EnumProperty.of("shape", LumecornShape.class);
	private static final VoxelShape SHAPE_BOTTOM = Block.createCuboidShape(6, 0, 6, 10, 16, 10);
	private static final VoxelShape SHAPE_TOP = Block.createCuboidShape(6, 0, 6, 10, 8, 10);
	
	public LumecornBlock() {
		super(FabricBlockSettings.of(Material.WOOD)
				.breakByTool(FabricToolTags.AXES)
				.hardness(0.5F)
				.luminance((state) -> {
			return state.get(SHAPE).getLight();
		}));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return state.get(SHAPE) == LumecornShape.LIGHT_TOP ? SHAPE_TOP : SHAPE_BOTTOM;
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		LumecornShape shape = state.get(SHAPE);
		if (shape == LumecornShape.BOTTOM_BIG || shape == LumecornShape.BOTTOM_SMALL) {
			return world.getBlockState(pos.down()).isIn(EndTags.END_GROUND);
		}
		else if (shape == LumecornShape.LIGHT_TOP) {
			return world.getBlockState(pos.down()).isOf(this);
		}
		else {
			return world.getBlockState(pos.down()).isOf(this) && world.getBlockState(pos.up()).isOf(this);
		}
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
		LumecornShape shape = state.get(SHAPE);
		if (shape == LumecornShape.BOTTOM_BIG || shape == LumecornShape.BOTTOM_SMALL || shape == LumecornShape.MIDDLE) {
			if (MHelper.RANDOM.nextBoolean()) {
				return Collections.singletonList(new ItemStack(EndBlocks.LUMECORN_SEED));
			}
			else {
				return Collections.emptyList();
			}
		}
		if (MHelper.RANDOM.nextBoolean()) {
			return Collections.singletonList(new ItemStack(EndItems.LUMECORN_ROD));
		}
		else {
			return Collections.emptyList();
		}
	}
}
