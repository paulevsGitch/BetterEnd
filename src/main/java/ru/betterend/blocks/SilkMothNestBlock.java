package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.IRenderTyped;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.betterend.entity.SilkMothEntity;
import ru.betterend.registry.EndEntities;
import ru.betterend.registry.EndItems;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SilkMothNestBlock extends BaseBlock implements IRenderTyped {
	public static final BooleanProperty ACTIVE = EndBlockProperties.ACTIVE;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty FULLNESS = EndBlockProperties.FULLNESS;
	private static final VoxelShape TOP = box(6, 0, 6, 10, 16, 10);
	private static final VoxelShape BOTTOM = box(0, 0, 0, 16, 16, 16);
	
	public SilkMothNestBlock() {
		super(FabricBlockSettings.of(Material.WOOL).hardness(0.5F).resistance(0.1F).sound(SoundType.WOOL).noOcclusion().randomTicks());
		this.registerDefaultState(defaultBlockState().setValue(ACTIVE, true).setValue(FULLNESS, 0));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVE, FACING, FULLNESS);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return state.getValue(ACTIVE) ? BOTTOM : TOP;
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction dir = ctx.getHorizontalDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, dir);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!state.getValue(ACTIVE)) {
			if (canSupportCenter(world, pos.above(), Direction.DOWN) || world.getBlockState(pos.above()).is(BlockTags.LEAVES)) {
				return state;
			}
			else {
				return Blocks.AIR.defaultBlockState();
			}
		}
		return state;
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, FACING);
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return BlocksHelper.mirrorHorizontal(state, mirror, FACING);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return state.getValue(ACTIVE) ? Collections.singletonList(new ItemStack(this)) : Collections.emptyList();
	}
	
	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (!state.getValue(ACTIVE) && player.isCreative()) {
			BlocksHelper.setWithUpdate(world, pos.below(), Blocks.AIR);
		}
		BlockState up = world.getBlockState(pos.above());
		if (up.is(this) && !up.getValue(ACTIVE)) {
			BlocksHelper.setWithUpdate(world, pos.above(), Blocks.AIR);
		}
		super.playerWillDestroy(world, pos, state, player);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (!state.getValue(ACTIVE)) {
			return;
		}
		if (random.nextBoolean()) {
			return;
		}
		Direction dir = state.getValue(FACING);
		BlockPos spawn = pos.relative(dir);
		if (!world.getBlockState(spawn).isAir()) {
			return;
		}
		int count = world.getEntities(EndEntities.SILK_MOTH, new AABB(pos).inflate(16), (entity) -> {
			return true;
		}).size();
		if (count > 6) {
			return;
		}
		SilkMothEntity moth = new SilkMothEntity(EndEntities.SILK_MOTH, world);
		moth.moveTo(spawn.getX() + 0.5, spawn.getY() + 0.5, spawn.getZ() + 0.5, dir.toYRot(), 0);
		moth.setDeltaMovement(new Vec3(dir.getStepX() * 0.4, 0, dir.getStepZ() * 0.4));
		moth.setHive(world, pos);
		world.addFreshEntity(moth);
		world.playSound(null, pos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1, 1);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (hand == InteractionHand.MAIN_HAND) {
			ItemStack stack = player.getMainHandItem();
			if (stack.is(FabricToolTags.SHEARS) && state.getValue(ACTIVE) && state.getValue(FULLNESS) == 3) {
				BlocksHelper.setWithUpdate(world, pos, state.setValue(FULLNESS, 0));
				Direction dir = state.getValue(FACING);
				double px = pos.getX() + dir.getStepX() + 0.5;
				double py = pos.getY() + dir.getStepY() + 0.5;
				double pz = pos.getZ() + dir.getStepZ() + 0.5;
				ItemStack drop = new ItemStack(EndItems.SILK_FIBER, MHelper.randRange(1, 4, world.getRandom()));
				ItemEntity entity = new ItemEntity(world, px, py, pz, drop);
				world.addFreshEntity(entity);
				drop = new ItemStack(EndItems.SILK_MOTH_MATRIX, MHelper.randRange(1, 3, world.getRandom()));
				entity = new ItemEntity(world, px, py, pz, drop);
				world.addFreshEntity(entity);
				if (!player.isCreative()) {
					stack.setDamageValue(stack.getDamageValue() + 1);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.FAIL;
	}
}
