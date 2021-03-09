package ru.betterend.world.features.terrain;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.basis.StalactiteBlock;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.features.DefaultFeature;

public class StalactiteFeature extends DefaultFeature {
	private final boolean ceiling;
	private final Block[] ground;
	private final Block block;
	
	public StalactiteFeature(boolean ceiling, Block block, Block... ground) {
		this.ceiling = ceiling;
		this.ground = ground;
		this.block = block;
	}
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!isGround(world.getBlockState(ceiling ? pos.up() : pos.down()).getBlock())) {
			return false;
		}
		
		Mutable mut = new Mutable().set(pos);
		int height = random.nextInt(8);
		int dir = ceiling ? -1 : 1;
		boolean stalagnate = false;
		
		for (int i = 1; i <= height; i++) {
			mut.setY(pos.getY() + i * dir);
			BlockState state = world.getBlockState(mut);
			if (!state.getMaterial().isReplaceable()) {
				stalagnate = state.isIn(EndTags.GEN_TERRAIN);
				height = i - 1;
				break;
			}
		}
		
		int center = stalagnate ? height >> 1 : 0;
		for (int i = 0; i < height; i++) {
			mut.setY(pos.getY() + i * dir);
			int size = stalagnate ? MathHelper.abs(i - center) + 1 : height - i - 1;
			boolean waterlogged = !world.getFluidState(mut).isEmpty();
			BlockState base = block.getDefaultState().with(StalactiteBlock.SIZE, size).with(Properties.WATERLOGGED, waterlogged);
			BlockState state = stalagnate ? base.with(StalactiteBlock.IS_FLOOR, dir > 0 ? i < center : i > center) : base.with(StalactiteBlock.IS_FLOOR, dir > 0);
			BlocksHelper.setWithoutUpdate(world, mut, state);
		}
		
		return true;
	}

	private boolean isGround(Block block) {
		for (Block b : ground) {
			if (b == block) {
				return true;
			}
		}
		return false;
	}
}
