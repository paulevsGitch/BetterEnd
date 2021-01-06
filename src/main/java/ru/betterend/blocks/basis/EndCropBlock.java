package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class EndCropBlock extends EndPlantBlock {
	public static final IntProperty AGE = IntProperty.of("age", 0, 3);
	
	private final Block[] terrain;
	private final Item drop;
	
	public EndCropBlock(Item drop, Block... terrain) {
		super(FabricBlockSettings.of(Material.PLANT)
				.breakByTool(FabricToolTags.HOES)
				.sounds(BlockSoundGroup.GRASS)
				.breakByHand(true)
				.ticksRandomly()
				.noCollision());
		this.drop = drop;
		this.terrain = terrain;
		this.setDefaultState(getDefaultState().with(AGE, 0));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(AGE);
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		for (Block block: terrain) {
			if (state.isOf(block)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		if (state.get(AGE) < 3) {
			return Collections.singletonList(new ItemStack(this));
		}
		ItemStack tool = builder.get(LootContextParameters.TOOL);
		if (tool != null && tool.isEffectiveOn(state)) {
			int enchantment = EnchantmentHelper.getLevel(Enchantments.FORTUNE, tool);
			if (enchantment > 0) {
				int countSeeds = MHelper.randRange(MathHelper.clamp(1 + enchantment, 1, 3), 3, MHelper.RANDOM);
				int countDrops = MHelper.randRange(MathHelper.clamp(1 + enchantment, 1, 2), 2, MHelper.RANDOM);
				return Lists.newArrayList(new ItemStack(this, countSeeds), new ItemStack(drop, countDrops));
			}
		}
		int countSeeds = MHelper.randRange(1, 3, MHelper.RANDOM);
		int countDrops = MHelper.randRange(1, 2, MHelper.RANDOM);
		return Lists.newArrayList(new ItemStack(this, countSeeds), new ItemStack(drop, countDrops));
	}
	
	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.NONE;
	}
	
	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		int age = state.get(AGE);
		if (age < 3) {
			BlocksHelper.setWithUpdate(world, pos, state.with(AGE, age + 1));
		}
	}
	
	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return state.get(AGE) < 3;
	}
	
	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return random.nextInt(8) == 0 && state.get(AGE) < 3;
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		if (canGrow(world, random, pos, state)) {
			grow(world, random, pos, state);
		}
	}
}
