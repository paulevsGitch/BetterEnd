package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.List;
import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import ru.betterend.blocks.entities.EBarrelBlockEntity;
import ru.betterend.patterns.BlockModelProvider;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndBlockEntities;

public class EndBarrelBlock extends BarrelBlock implements BlockModelProvider {
	public EndBarrelBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).noOcclusion());
	}

	@Override
	public BlockEntity newBlockEntity(BlockGetter world) {
		return EndBlockEntities.BARREL.create();
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = super.getDrops(state, builder);
		drop.add(new ItemStack(this.asItem()));
		return drop;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EBarrelBlockEntity) {
				player.openMenu((EBarrelBlockEntity) blockEntity);
				player.awardStat(Stats.OPEN_BARREL);
				PiglinAi.angerNearbyPiglins(player, true);
			}

			return InteractionResult.CONSUME;
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EBarrelBlockEntity) {
			((EBarrelBlockEntity) blockEntity).tick();
		}
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer,
			ItemStack itemStack) {
		if (itemStack.hasCustomHoverName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof EBarrelBlockEntity) {
				((EBarrelBlockEntity) blockEntity).setCustomName(itemStack.getHoverName());
			}
		}
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String block = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, block, block);
	}
	
	@Override
	public String getModelString(String block) {
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
