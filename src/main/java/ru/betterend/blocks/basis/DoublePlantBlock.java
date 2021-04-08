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
import net.minecraft.world.entity.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;

public class DoublePlantBlock extends BlockBaseNotFull implements IRenderTypeable, Fertilizable {
	private static final VoxelShape SHAPE = Block.createCuboidShape(4, 2, 4, 12, 16, 12);
	public static final IntegerProperty ROTATION = BlockProperties.ROTATION;
	public static final BooleanProperty TOP = BooleanProperty.of("top");

	public DoublePlantBlock() {
		super(FabricBlockSettings.of(Material.PLANT).breakByTool(FabricToolTags.SHEARS).sounds(SoundType.WET_GRASS)
				.breakByHand(true).noCollision());
		this.setDefaultState(this.stateManager.defaultBlockState().with(TOP, false));
	}

	public DoublePlantBlock(int light) {
		super(FabricBlockSettings.of(Material.PLANT).breakByTool(FabricToolTags.SHEARS).sounds(SoundType.WET_GRASS)
				.luminance((state) -> {
					return state.getValue(TOP) ? light : 0;
				}).breakByHand(true).noCollision());
		this.setDefaultState(this.stateManager.defaultBlockState().with(TOP, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(TOP, ROTATION);
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
		BlockState down = world.getBlockState(pos.below());
		BlockState up = world.getBlockState(pos.up());
		return state.getValue(TOP) ? down.getBlock() == this : isTerrain(down) && (up.getMaterial().isReplaceable());
	}

	public boolean canStayAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState down = world.getBlockState(pos.below());
		BlockState up = world.getBlockState(pos.up());
		return state.getValue(TOP) ? down.getBlock() == this : isTerrain(down) && (up.getBlock() == this);
	}

	protected boolean isTerrain(BlockState state) {
		return state.isIn(EndTags.END_GROUND);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world,
			BlockPos pos, BlockPos neighborPos) {
		if (!canStayAt(state, world, pos)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state;
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.getValue(TOP)) {
			return Lists.newArrayList();
		}

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
		return true;
	}

	@Override
	public boolean canGrow(Level world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerLevel world, Random random, BlockPos pos, BlockState state) {
		ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
				new ItemStack(this));
		world.spawnEntity(item);
	}

	@Override
	public void onPlaced(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		int rot = world.random.nextInt(4);
		BlockState bs = this.defaultBlockState().with(ROTATION, rot);
		BlocksHelper.setWithoutUpdate(world, pos, bs);
		BlocksHelper.setWithoutUpdate(world, pos.up(), bs.with(TOP, true));
	}
}
