package ru.betterend.blocks.basis;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.util.BlocksHelper;

public class BlockVine extends BlockBaseNotFull implements IRenderTypeable, Fertilizable {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	private static final VoxelShape VOXEL_SHAPE = Block.createCuboidShape(2, 0, 2, 14, 16, 14);
	
	public BlockVine() {
		this(0, false);
	}
	
	public BlockVine(int light) {
		this(light, false);
	}
	
	public BlockVine(int light, boolean bottomOnly) {
		super(FabricBlockSettings.of(Material.PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.GRASS)
				.luminance((state) -> {
					return bottomOnly ? state.get(SHAPE) == TripleShape.BOTTOM ? light : 0 : light;
				})
				.breakByHand(true)
				.noCollision());
		this.setDefaultState(this.stateManager.getDefaultState().with(SHAPE, TripleShape.BOTTOM));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		Vec3d vec3d = state.getModelOffset(view, pos);
		return VOXEL_SHAPE.offset(vec3d.x, vec3d.y, vec3d.z);
	}

	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.XZ;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return isSupport(state, world, pos);
	}
	
	protected boolean isSupport(BlockState state, WorldView world, BlockPos pos) {
		BlockState up = world.getBlockState(pos.up());
		return up.isOf(this) || up.isIn(BlockTags.LEAVES) || sideCoversSmallSquare(world, pos.up(), Direction.UP);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return Blocks.AIR.getDefaultState();
		}
		else {
			if (world.getBlockState(pos.down()).getBlock() != this)
				return state.with(SHAPE, TripleShape.BOTTOM);
			else if (world.getBlockState(pos.up()).getBlock() != this)
				return state.with(SHAPE, TripleShape.TOP);
			return state.with(SHAPE, TripleShape.MIDDLE);
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

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		while (world.getBlockState(pos).getBlock() == this) {
			pos = pos.down();
		}
		return world.isAir(pos);
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		while (world.getBlockState(pos).getBlock() == this) {
			pos = pos.down();
		}
		world.setBlockState(pos, getDefaultState());
		BlocksHelper.setWithoutUpdate(world, pos, getDefaultState());
	}
}
