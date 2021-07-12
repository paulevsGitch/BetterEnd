package ru.betterend.blocks;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.blocks.entities.EternalPedestalEntity;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndPortals;
import ru.betterend.rituals.EternalRitual;

import java.util.List;

public class EternalPedestal extends PedestalBlock {
	public static final BooleanProperty ACTIVATED = EndBlockProperties.ACTIVE;
	
	public EternalPedestal() {
		super(EndBlocks.FLAVOLITE_RUNED_ETERNAL);
		this.registerDefaultState(defaultBlockState().setValue(ACTIVATED, false));
	}
	
	@Override
	public void checkRitual(Level world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EternalPedestalEntity) {
			EternalPedestalEntity pedestal = (EternalPedestalEntity) blockEntity;
			BlockState updatedState = world.getBlockState(pos);
			if (pedestal.isEmpty()) {
				if (pedestal.hasRitual()) {
					EternalRitual ritual = pedestal.getRitual();
					if (ritual.isActive()) {
						ResourceLocation targetWorld = ritual.getTargetWorldId();
						int portalId;
						if (targetWorld != null) {
							portalId = EndPortals.getPortalIdByWorld(targetWorld);
						}
						else {
							portalId = EndPortals.getPortalIdByWorld(EndPortals.OVERWORLD_ID);
						}
						ritual.disablePortal(portalId);
					}
				}
				world.setBlockAndUpdate(pos, updatedState.setValue(ACTIVATED, false).setValue(HAS_LIGHT, false));
			}
			else {
				ItemStack itemStack = pedestal.getItem(0);
				ResourceLocation id = Registry.ITEM.getKey(itemStack.getItem());
				if (EndPortals.isAvailableItem(id)) {
					world.setBlockAndUpdate(pos, updatedState.setValue(ACTIVATED, true).setValue(HAS_LIGHT, true));
					if (pedestal.hasRitual()) {
						pedestal.getRitual().checkStructure();
					}
					else {
						EternalRitual ritual = new EternalRitual(world, pos);
						ritual.checkStructure();
					}
				}
			}
		}
	}
	
	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		BlockState updated = super.updateShape(state, direction, newState, world, pos, posFrom);
		if (!updated.is(this)) return updated;
		if (!this.isPlaceable(updated)) {
			return updated.setValue(ACTIVATED, false);
		}
		return updated;
	}
	
	@Override
	@Deprecated
	public float getDestroyProgress(BlockState state, Player player, BlockGetter world, BlockPos pos) {
		return 0.0F;
	}
	
	@Override
	public float getExplosionResistance() {
		return Blocks.BEDROCK.getExplosionResistance();
	}
	
	@Override
	public boolean dropFromExplosion(Explosion explosion) {
		return false;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.is(this)) {
			EndBlockProperties.PedestalState currentState = state.getValue(EndBlockProperties.PEDESTAL_STATE);
			if (currentState.equals(EndBlockProperties.PedestalState.BOTTOM) || currentState.equals(EndBlockProperties.PedestalState.PILLAR)) {
				return Lists.newArrayList();
			}
		}
		List<ItemStack> drop = Lists.newArrayList();
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof EternalPedestalEntity) {
			EternalPedestalEntity pedestal = (EternalPedestalEntity) blockEntity;
			if (!pedestal.isEmpty()) {
				drop.add(pedestal.getItem(0));
			}
		}
		return drop;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		super.createBlockStateDefinition(stateManager);
		stateManager.add(ACTIVATED);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new EternalPedestalEntity(blockPos, blockState);
	}
	
	@Override
	public boolean hasUniqueEntity() {
		return true;
	}
}
