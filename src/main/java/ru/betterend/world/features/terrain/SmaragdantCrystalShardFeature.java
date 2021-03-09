package ru.betterend.world.features.terrain;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.features.DefaultFeature;

public class SmaragdantCrystalShardFeature extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).isIn(EndTags.GEN_TERRAIN)) {
			return false;
		}
		
		BlockState shard = EndBlocks.SMARAGDANT_CRYSTAL_SHARD.getDefaultState();
		boolean waterlogged = !world.getFluidState(pos).isEmpty();
		BlocksHelper.setWithoutUpdate(world, pos, shard.with(Properties.WATERLOGGED, waterlogged));
		
		return true;
	}
}
