package ru.betterend.world.features.terrain;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.features.DefaultFeature;

public class SmaragdantCrystalFeature extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).isIn(EndTags.GEN_TERRAIN)) {
			return false;
		}
		
		Mutable mut = new Mutable();
		int count = MHelper.randRange(15, 30, random);
		BlockState crystal = EndBlocks.SMARAGDANT_CRYSTAL.getDefaultState();
		BlockState shard = EndBlocks.SMARAGDANT_CRYSTAL_SHARD.getDefaultState();
		for (int i = 0; i < count; i++) {
			mut.set(pos).move(MHelper.floor(random.nextGaussian() * 2 + 0.5), 5, MHelper.floor(random.nextGaussian() * 2 + 0.5));
			int dist = MHelper.floor(1.5F - MHelper.length(mut.getX() - pos.getX(), mut.getZ() - pos.getZ())) + random.nextInt(3);
			if (dist > 0) {
				BlockState state = world.getBlockState(mut);
				for (int n = 0; n < 10 && state.isAir(); n++) {
					mut.setY(mut.getY() - 1);
					state = world.getBlockState(mut);
				}
				if (state.isIn(EndTags.GEN_TERRAIN) && !world.getBlockState(mut.up()).isOf(crystal.getBlock())) {
					for (int j = 0; j <= dist; j++) {
						BlocksHelper.setWithoutUpdate(world, mut, crystal);
						mut.setY(mut.getY() + 1);
					}
					if (random.nextBoolean()) {
						boolean waterlogged = !world.getFluidState(mut).isEmpty();
						BlocksHelper.setWithoutUpdate(world, mut, shard.with(Properties.WATERLOGGED, waterlogged));
					}
					else {
						BlocksHelper.setWithoutUpdate(world, mut, crystal);
					}
				}
			}
		}
		
		return true;
	}
}
