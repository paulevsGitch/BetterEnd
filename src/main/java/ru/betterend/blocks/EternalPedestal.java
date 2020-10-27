package ru.betterend.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import ru.betterend.blocks.basis.BlockSlab;
import ru.betterend.blocks.entities.EternalPedestalBlockEntity;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class EternalPedestal extends BlockSlab implements BlockEntityProvider {
	public static final BooleanProperty ACTIVATED = BooleanProperty.of("active");
	public static final BooleanProperty HAS_ITEM = BooleanProperty.of("has_item");
	
	public EternalPedestal() {
		super(EndBlocks.FLAVOLITE_RUNED_ETERNAL);
		this.setDefaultState(stateManager.getDefaultState().with(ACTIVATED, false).with(HAS_ITEM, false));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) return ActionResult.CONSUME;
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EternalPedestalBlockEntity && state.isOf(this)) {
			EternalPedestalBlockEntity pedestal = (EternalPedestalBlockEntity) blockEntity;
			if (pedestal.isEmpty()) {
				ItemStack itemStack = player.getStackInHand(hand);
				if (itemStack.isEmpty()) return ActionResult.CONSUME;
				if (itemStack.getItem().equals(EndItems.ETERNAL_CRYSTAL)) {
					world.setBlockState(pos, state.with(ACTIVATED, true).with(HAS_ITEM, true));
				} else {
					world.setBlockState(pos, state.with(HAS_ITEM, true));
				}
				pedestal.setStack(0, itemStack.split(1));
				return ActionResult.SUCCESS;
			} else {
				ItemStack itemStack = pedestal.getStack(0);
				if (player.giveItemStack(itemStack)) {
					if (state.get(ACTIVATED)) {
						world.setBlockState(pos, state.with(ACTIVATED, false).with(HAS_ITEM, false));
					} else {
						world.setBlockState(pos, state.with(HAS_ITEM, false));
					}
					pedestal.removeStack(0);
					return ActionResult.SUCCESS;
				}
				return ActionResult.FAIL;
			}
		}
		return ActionResult.PASS;
	}
	
	@Override
	public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
		return 0.0F;
	}
	
	@Override
	public float getBlastResistance() {
		return Blocks.BEDROCK.getBlastResistance();
	}
	
	@Override
	public boolean shouldDropItemsOnExplosion(Explosion explosion) {
		return false;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Lists.newArrayList();
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		super.appendProperties(stateManager);
		stateManager.add(ACTIVATED, HAS_ITEM);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new EternalPedestalBlockEntity();
	}
}
