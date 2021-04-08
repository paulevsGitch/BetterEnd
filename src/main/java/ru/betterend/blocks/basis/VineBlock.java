package ru.betterend.blocks.basis;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.AbstractBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fertilizable;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.ShapeContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.util.BlocksHelper;

public class VineBlock extends BlockBaseNotFull implements IRenderTypeable, Fertilizable {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	private static final VoxelShape VOXEL_SHAPE = Block.createCuboidShape(2, 0, 2, 14, 16, 14);

	public VineBlock() {
		this(0, false);
	}

	public VineBlock(int light) {
		this(light, false);
	}

	public VineBlock(int light, boolean bottomOnly) {
		super(FabricBlockSettings.of(Material.PLANT).breakByTool(FabricToolTags.SHEARS).sounds(SoundType.GRASS)
				.luminance((state) -> {
					return bottomOnly ? state.getValue(SHAPE) == TripleShape.BOTTOM ? light : 0 : light;
				}).breakByHand(true).noCollision());
		this.setDefaultState(this.stateManager.defaultBlockState().with(SHAPE, TripleShape.BOTTOM));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
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

	public boolean canGenerate(BlockState state, WorldView world, BlockPos pos) {
		return isSupport(state, world, pos);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return isSupport(state, world, pos);
	}

	protected boolean isSupport(BlockState state, WorldView world, BlockPos pos) {
		BlockState up = world.getBlockState(pos.up());
		return up.is(this) || up.isIn(BlockTags.LEAVES) || sideCoversSmallSquare(world, pos.up(), Direction.DOWN);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world,
			BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			if (world.getBlockState(pos.below()).getBlock() != this)
				return state.with(SHAPE, TripleShape.BOTTOM);
			else if (world.getBlockState(pos.up()).getBlock() != this)
				return state.with(SHAPE, TripleShape.TOP);
			return state.with(SHAPE, TripleShape.MIDDLE);
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool != null && tool.getItem().isIn(FabricToolTags.SHEARS)
				|| EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Lists.newArrayList(new ItemStack(this));
		} else {
			return Lists.newArrayList();
		}
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		while (world.getBlockState(pos).getBlock() == this) {
			pos = pos.below();
		}
		return world.getBlockState(pos).isAir();
	}

	@Override
	public boolean canGrow(Level world, Random random, BlockPos pos, BlockState state) {
		while (world.getBlockState(pos).getBlock() == this) {
			pos = pos.below();
		}
		return world.isAir(pos);
	}

	@Override
	public void grow(ServerLevel world, Random random, BlockPos pos, BlockState state) {
		while (world.getBlockState(pos).getBlock() == this) {
			pos = pos.below();
		}
		world.setBlockAndUpdate(pos, getDefaultState());
		BlocksHelper.setWithoutUpdate(world, pos, getDefaultState());
	}
}
