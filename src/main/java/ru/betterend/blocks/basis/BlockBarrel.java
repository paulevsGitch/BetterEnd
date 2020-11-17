package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.List;
import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import ru.betterend.blocks.entities.EBarrelBlockEntity;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndBlockEntities;

public class BlockBarrel extends BarrelBlock implements BlockPatterned {
	public BlockBarrel(Block source) {
		super(FabricBlockSettings.copyOf(source).nonOpaque());
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return EndBlockEntities.BARREL.instantiate();
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = super.getDroppedStacks(state, builder);
		drop.add(new ItemStack(this.asItem()));
		return drop;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		if (world.isClient) {
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
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
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
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer,
			ItemStack itemStack) {
		if (itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EBarrelBlockEntity) {
				((EBarrelBlockEntity) blockEntity).setCustomName(itemStack.getName());
			}
		}
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String block = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(data, block, block);
	}
	
	@Override
	public String getModelPattern(String block) {
		String texture = Registry.BLOCK.getId(this).getPath();
		if (block.contains("open")) {
			return Patterns.createJson(Patterns.BLOCK_BARREL_OPEN, texture, texture);
		}
		return Patterns.createJson(Patterns.BLOCK_BOTTOM_TOP, texture, texture);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_BARREL;
	}
}
