package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.List;
import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockRenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.mob.PiglinBrain;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import ru.betterend.blocks.entities.EBarrelBlockEntity;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndBlockEntities;

public class EndBarrelBlock extends BarrelBlock implements BlockPatterned {
	public EndBarrelBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).nonOpaque());
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return EndBlockEntities.BARREL.instantiate();
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = super.getDrops(state, builder);
		drop.add(new ItemStack(this.asItem()));
		return drop;
	}

	@Override
	public ActionResult onUse(BlockState state, Level world, BlockPos pos, Player player, Hand hand,
			BlockHitResult hit) {
		if (world.isClientSide) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EBarrelBlockEntity) {
				player.openHandledScreen((EBarrelBlockEntity) blockEntity);
				player.incrementStat(Stats.OPEN_BARREL);
				PiglinBrain.onGuardedBlockInteracted(player, true);
			}

			return ActionResult.CONSUME;
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EBarrelBlockEntity) {
			((EBarrelBlockEntity) blockEntity).tick();
		}
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public void onPlaced(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EBarrelBlockEntity) {
				((EBarrelBlockEntity) blockEntity).setCustomName(itemStack.getName());
			}
		}
	}

	@Override
	public String getStatesPattern(Reader data) {
		String block = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, block, block);
	}

	@Override
	public String getModelPattern(String block) {
		String texture = Registry.BLOCK.getKey(this).getPath();
		if (block.contains("open")) {
			return Patterns.createJson(Patterns.BLOCK_BARREL_OPEN, texture, texture);
		}
		return Patterns.createJson(Patterns.BLOCK_BOTTOM_TOP, texture, texture);
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_BARREL;
	}
}
