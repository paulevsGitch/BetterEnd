package ru.betterend.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;

import ru.betterend.blocks.basis.BlockSlab;
import ru.betterend.registry.BlockRegistry;

public class EternalPedestal extends BlockSlab {
	public static final BooleanProperty ACTIVATED = BooleanProperty.of("active");
	public static final BooleanProperty HAS_ITEM = BooleanProperty.of("has_item");
	
	public EternalPedestal() {
		super(BlockRegistry.FLAVOLITE_RUNED_ETERNAL);
		this.setDefaultState(stateManager.getDefaultState().with(ACTIVATED, false).with(HAS_ITEM, false));
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
}
