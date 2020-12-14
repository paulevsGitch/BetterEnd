package ru.betterend.blocks;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class BlockRespawnObelisk extends BlockBase implements IColorProvider, IRenderTypeable {
	private static final VoxelShape VOXEL_SHAPE_BOTTOM = Block.createCuboidShape(1, 0, 1, 15, 16, 15);
	private static final VoxelShape VOXEL_SHAPE_MIDDLE_TOP = Block.createCuboidShape(2, 0, 2, 14, 16, 14);
	
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	
	public BlockRespawnObelisk() {
		super(FabricBlockSettings.copyOf(Blocks.END_STONE).luminance((state) -> {
			return (state.get(SHAPE) == TripleShape.BOTTOM) ? 0 : 15;
		}));
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return (state.get(SHAPE) == TripleShape.BOTTOM) ? VOXEL_SHAPE_BOTTOM : VOXEL_SHAPE_MIDDLE_TOP;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		for (int i = 0; i < 3; i++) {
			if (!world.getBlockState(pos.up(i)).getMaterial().isReplaceable()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		state = this.getDefaultState();
		BlocksHelper.setWithUpdate(world, pos, state.with(SHAPE, TripleShape.BOTTOM));
		BlocksHelper.setWithUpdate(world, pos.up(), state.with(SHAPE, TripleShape.MIDDLE));
		BlocksHelper.setWithUpdate(world, pos.up(2), state.with(SHAPE, TripleShape.TOP));
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		TripleShape shape = state.get(SHAPE);
		if (shape == TripleShape.BOTTOM) {
			if (world.getBlockState(pos.up()).isOf(this)) {
				return state;
			}
			else {
				return Blocks.AIR.getDefaultState();
			}
		}
		else if (shape == TripleShape.MIDDLE) {
			if (world.getBlockState(pos.up()).isOf(this) && world.getBlockState(pos.down()).isOf(this)) {
				return state;
			}
			else {
				return Blocks.AIR.getDefaultState();
			}
		}
		else {
			if (world.getBlockState(pos.down()).isOf(this)) {
				return state;
			}
			else {
				return Blocks.AIR.getDefaultState();
			}
		}
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (player.isCreative()) {
			TripleShape shape = state.get(SHAPE);
			if (shape == TripleShape.MIDDLE) {
				BlocksHelper.setWithUpdate(world, pos.down(), Blocks.AIR);
			}
			else if (shape == TripleShape.TOP) {
				BlocksHelper.setWithUpdate(world, pos.down(2), Blocks.AIR);
			}
		}
		super.onBreak(world, pos, state, player);
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		if (state.get(SHAPE) == TripleShape.BOTTOM) {
			return Lists.newArrayList(new ItemStack(this));
		}
		else {
			return Lists.newArrayList();
		}
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.TRANSLUCENT;
	}
	
	@Override
	public BlockColorProvider getProvider() {
		return ((IColorProvider) EndBlocks.AURORA_CRYSTAL).getProvider();
	}
	
	@Override
	public ItemColorProvider getItemProvider() {
		return (stack, tintIndex) -> {
			return MHelper.color(255, 255, 255);
		};
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack itemStack = player.getStackInHand(hand);
		boolean canActivate = itemStack.getItem() == EndItems.AMBER_GEM && itemStack.getCount() > 3;
		if (hand != Hand.MAIN_HAND || !canActivate) {
			if (!world.isClient && !(itemStack.getItem() instanceof BlockItem)) {
				ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
				serverPlayerEntity.sendMessage(new TranslatableText("message.betterend.fail_spawn"), true);
			}
			return ActionResult.FAIL;
		}
		if (!world.isClient) {
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
			serverPlayerEntity.setSpawnPoint(world.getRegistryKey(), pos, 0.0F, false, false);
			serverPlayerEntity.sendMessage(new TranslatableText("message.betterend.set_spawn"), true);
            world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!player.isCreative()) {
            	itemStack.decrement(4);
            }
		}
		return player.isCreative() ? ActionResult.PASS : ActionResult.success(world.isClient);
	}
}
