package ru.betterend.blocks;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlimeBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;

public class UmbrellaTreeMembraneBlock extends SlimeBlock implements IRenderTypeable, BlockPatterned {
	public static final IntProperty COLOR = IntProperty.of("color", 0, 7);
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(0);
	
	public UmbrellaTreeMembraneBlock() {
		super(FabricBlockSettings.copyOf(Blocks.SLIME_BLOCK));
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		double px = ctx.getBlockPos().getX() * 0.1;
		double py = ctx.getBlockPos().getY() * 0.1;
		double pz = ctx.getBlockPos().getZ() * 0.1;
		return this.getDefaultState().with(COLOR, MHelper.floor(NOISE.eval(px, py, pz) * 3.5 + 4));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(COLOR);
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.TRANSLUCENT;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		if (state.get(COLOR) > 0) {
			return Lists.newArrayList(new ItemStack(this));
		}
		else {
			return MHelper.RANDOM.nextInt(4) == 0 ? Lists.newArrayList(new ItemStack(EndBlocks.UMBRELLA_TREE_SAPLING)) : Collections.emptyList();
		}
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String block = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(data, block, block);
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterns.createJson(Patterns.BLOCK_BASE, blockId.getPath(), block);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_SIMPLE;
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return state.get(COLOR) > 0;
	}
	
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		if (state.get(COLOR) > 0) {
			return super.isSideInvisible(state, stateFrom, direction);
		}
		else {
			return false;
		}
	}
}
