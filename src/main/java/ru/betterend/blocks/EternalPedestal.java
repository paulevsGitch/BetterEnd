package ru.betterend.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.blocks.entities.EternalPedestalEntity;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndPortals;
import ru.betterend.rituals.EternalRitual;

public class EternalPedestal extends PedestalBlock {
	public static final BooleanProperty ACTIVATED = BlockProperties.ACTIVE;
	
	public EternalPedestal() {
		super(EndBlocks.FLAVOLITE_RUNED_ETERNAL);
		this.setDefaultState(getDefaultState().with(ACTIVATED, false));
	}
	
	@Override
	public void checkRitual(World world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EternalPedestalEntity) {
			EternalPedestalEntity pedestal = (EternalPedestalEntity) blockEntity;
			BlockState updatedState = world.getBlockState(pos);
			if (pedestal.isEmpty() && updatedState.get(ACTIVATED)) {
				if (pedestal.hasRitual()) {
					EternalRitual ritual = pedestal.getRitual();
					Item item = pedestal.getStack(0).getItem();
					int dim = EndPortals.getPortalState(Registry.ITEM.getId(item));
					ritual.removePortal(dim);
				}
				world.setBlockState(pos, updatedState.with(ACTIVATED, false).with(HAS_LIGHT, false));
			} else {
				ItemStack itemStack = pedestal.getStack(0);
				Identifier id = Registry.ITEM.getId(itemStack.getItem());
				if (EndPortals.isAvailableItem(id)) {
					world.setBlockState(pos, updatedState.with(ACTIVATED, true).with(HAS_LIGHT, true));
					if (pedestal.hasRitual()) {
						pedestal.getRitual().checkStructure();
					} else {
						EternalRitual ritual = new EternalRitual(world, pos);
						ritual.checkStructure();
					}
				}
			}
		}
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		BlockState updated = super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
		if (!updated.isOf(this)) return updated;
		if (!this.isPlaceable(updated)) {
			return updated.with(ACTIVATED, false);
		}
		return updated;
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
		if (state.isOf(this)) {
			BlockProperties.PedestalState currentState = state.get(BlockProperties.PEDESTAL_STATE);
			if (currentState.equals(BlockProperties.PedestalState.BOTTOM) || currentState.equals(BlockProperties.PedestalState.PILLAR)) {
				return Lists.newArrayList();
			}
		}
		List<ItemStack> drop = Lists.newArrayList();
		BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
		if (blockEntity instanceof EternalPedestalEntity) {
			EternalPedestalEntity pedestal = (EternalPedestalEntity) blockEntity;
			if (!pedestal.isEmpty()) {
				drop.add(pedestal.getStack(0));
			}
		}
		return drop;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		super.appendProperties(stateManager);
		stateManager.add(ACTIVATED);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new EternalPedestalEntity();
	}
}
