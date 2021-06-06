package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;

public class EndCropBlock extends EndPlantBlock {
	private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 14, 14);
	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);
	
	private final Block[] terrain;
	private final Item drop;
	
	public EndCropBlock(Item drop, Block... terrain) {
		super(FabricBlockSettings.of(Material.PLANT)
				.breakByTool(FabricToolTags.HOES)
				.breakByHand(true)
				.sound(SoundType.GRASS)
				.randomTicks()
				.noCollission());
		this.drop = drop;
		this.terrain = terrain;
		this.registerDefaultState(defaultBlockState().setValue(AGE, 0));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(AGE);
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		for (Block block: terrain) {
			if (state.is(block)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.getValue(AGE) < 3) {
			return Collections.singletonList(new ItemStack(this));
		}
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool != null && tool.isCorrectToolForDrops(state)) {
			int enchantment = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
			if (enchantment > 0) {
				int countSeeds = MHelper.randRange(Mth.clamp(1 + enchantment, 1, 3), 3, MHelper.RANDOM);
				int countDrops = MHelper.randRange(Mth.clamp(1 + enchantment, 1, 2), 2, MHelper.RANDOM);
				return Lists.newArrayList(new ItemStack(this, countSeeds), new ItemStack(drop, countDrops));
			}
		}
		int countSeeds = MHelper.randRange(1, 3, MHelper.RANDOM);
		int countDrops = MHelper.randRange(1, 2, MHelper.RANDOM);
		return Lists.newArrayList(new ItemStack(this, countSeeds), new ItemStack(drop, countDrops));
	}
	
	@Override
	public BlockBehaviour.OffsetType getOffsetType() {
		return BlockBehaviour.OffsetType.NONE;
	}
	
	@Override
	public void performBonemeal(ServerLevel world, Random random, BlockPos pos, BlockState state) {
		int age = state.getValue(AGE);
		if (age < 3) {
			BlocksHelper.setWithUpdate(world, pos, state.setValue(AGE, age + 1));
		}
	}
	
	@Override
	public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
		return state.getValue(AGE) < 3;
	}
	
	@Override
	public boolean isBonemealSuccess(Level world, Random random, BlockPos pos, BlockState state) {
		return state.getValue(AGE) < 3;
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		super.tick(state, world, pos, random);
		if (isBonemealSuccess(world, random, pos, state) && random.nextInt(8) == 0) {
			performBonemeal(world, random, pos, state);
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return SHAPE;
	}
}
