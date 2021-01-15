package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import ru.betterend.blocks.basis.BaseBlock;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.entity.SilkMothEntity;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.registry.EndEntities;
import ru.betterend.util.BlocksHelper;

public class SilkMothNestBlock extends BaseBlock implements IRenderTypeable {
	public static final BooleanProperty ACTIVE = BlockProperties.ACTIVE;
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final IntProperty FULLNESS = BlockProperties.FULLNESS;
	private static final VoxelShape TOP = createCuboidShape(6, 0, 6, 10, 16, 10);
	private static final VoxelShape BOTTOM = createCuboidShape(0, 0, 0, 16, 16, 16);
	
	public SilkMothNestBlock() {
		super(FabricBlockSettings.of(Material.WOOL).hardness(0.5F).resistance(0.1F).sounds(BlockSoundGroup.WOOL).nonOpaque().ticksRandomly());
		this.setDefaultState(getDefaultState().with(ACTIVE, true).with(FULLNESS, 0));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVE, FACING, FULLNESS);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return state.get(ACTIVE) ? BOTTOM : TOP;
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction dir = ctx.getPlayerFacing().getOpposite();
		return this.getDefaultState().with(FACING, dir);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!state.get(ACTIVE)) {
			if (sideCoversSmallSquare(world, pos.up(), Direction.DOWN) || world.getBlockState(pos.up()).isIn(BlockTags.LEAVES)) {
				return state;
			}
			else {
				return Blocks.AIR.getDefaultState();
			}
		}
		return state;
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, FACING);
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return BlocksHelper.mirrorHorizontal(state, mirror, FACING);
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return state.get(ACTIVE) ? Collections.singletonList(new ItemStack(this)) : Collections.emptyList();
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!state.get(ACTIVE) && player.isCreative()) {
			BlocksHelper.setWithUpdate(world, pos.down(), Blocks.AIR);
		}
		BlockState up = world.getBlockState(pos.up());
		if (up.isOf(this) && !up.get(ACTIVE)) {
			BlocksHelper.setWithUpdate(world, pos.up(), Blocks.AIR);
		}
		super.onBreak(world, pos, state, player);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.get(ACTIVE)) {
			return;
		}
		if (random.nextBoolean()) {
			return;
		}
		Direction dir = state.get(FACING);
		BlockPos spawn = pos.offset(dir);
		if (!world.getBlockState(spawn).isAir()) {
			return;
		}
		int count = world.getEntitiesByType(EndEntities.SILK_MOTH, new Box(pos).expand(16), (entity) -> { return true; }).size();
		if (count > 8) {
			return;
		}
		SilkMothEntity moth = new SilkMothEntity(EndEntities.SILK_MOTH, world);
		moth.refreshPositionAndAngles(spawn.getX() + 0.5, spawn.getY() + 0.5, spawn.getZ() + 0.5, dir.asRotation(), 0);
		moth.setVelocity(new Vec3d(dir.getOffsetX() * 0.4, 0, dir.getOffsetZ() * 0.4));
		moth.setHive(world, pos);
		world.spawnEntity(moth);
		world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.BLOCKS, 1, 1);
	}
}
