package ru.betterend.world.features;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import ru.betterend.util.BlocksHelper;

public abstract class DefaultFeature extends Feature<DefaultFeatureConfig> {
	protected static final BlockState AIR = Blocks.AIR.getDefaultState();
	protected static final BlockState WATER = Blocks.WATER.getDefaultState();
	
	public DefaultFeature() {
		super(DefaultFeatureConfig.CODEC);
	}
	
	protected BlockPos getPosOnSurface(StructureWorldAccess world, BlockPos pos) {
		return world.getTopPosition(Type.WORLD_SURFACE, pos);
	}
	
	protected BlockPos getPosOnSurfaceRaycast(StructureWorldAccess world, BlockPos pos) {
		int h = BlocksHelper.downRay(world, pos, 256);
		return pos.down(h);
	}
	
	protected BlockPos getPosOnSurfaceRaycast(StructureWorldAccess world, BlockPos pos, int dist) {
		int h = BlocksHelper.downRay(world, pos, dist);
		return pos.down(h);
	}
}
