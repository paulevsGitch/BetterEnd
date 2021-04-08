package ru.betterend.blocks;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.BlockGetter;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;

public class UmbrellaTreeMembraneBlock extends SlimeBlock implements IRenderTypeable, BlockPatterned {
	public static final IntegerProperty COLOR = BlockProperties.COLOR;
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(0);

	public UmbrellaTreeMembraneBlock() {
		super(FabricBlockSettings.copyOf(Blocks.SLIME_BLOCK));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		double px = ctx.getBlockPos().getX() * 0.1;
		double py = ctx.getBlockPos().getY() * 0.1;
		double pz = ctx.getBlockPos().getZ() * 0.1;
		return this.defaultBlockState().with(COLOR, MHelper.floor(NOISE.eval(px, py, pz) * 3.5 + 4));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(COLOR);
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.TRANSLUCENT;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.getValue(COLOR) > 0) {
			return Lists.newArrayList(new ItemStack(this));
		} else {
			return MHelper.RANDOM.nextInt(4) == 0 ? Lists.newArrayList(new ItemStack(EndBlocks.UMBRELLA_TREE_SAPLING))
					: Collections.emptyList();
		}
	}

	@Override
	public String getStatesPattern(Reader data) {
		String block = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, block, block);
	}

	@Override
	public String getModelPattern(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(Patterns.BLOCK_BASE, blockId.getPath(), block);
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_SIMPLE;
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return state.getValue(COLOR) > 0;
	}

	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		if (state.getValue(COLOR) > 0) {
			return super.isSideInvisible(state, stateFrom, direction);
		} else {
			return false;
		}
	}
}
