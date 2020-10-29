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
	
	public static int getYOnSurface(StructureWorldAccess world, int x, int z) {
		return world.getTopY(Type.WORLD_SURFACE, x, z);
	}
	
	public static int getYOnSurfaceWG(StructureWorldAccess world, int x, int z) {
		return world.getTopY(Type.WORLD_SURFACE_WG, x, z);
	}
	
	public static BlockPos getPosOnSurface(StructureWorldAccess world, BlockPos pos) {
		return world.getTopPosition(Type.WORLD_SURFACE, pos);
	}
	
	public static BlockPos getPosOnSurfaceWG(StructureWorldAccess world, BlockPos pos) {
		return world.getTopPosition(Type.WORLD_SURFACE_WG, pos);
	}
	
	public static BlockPos getPosOnSurfaceRaycast(StructureWorldAccess world, BlockPos pos) {
		return getPosOnSurfaceRaycast(world, pos, 256);
	}
	
	public static BlockPos getPosOnSurfaceRaycast(StructureWorldAccess world, BlockPos pos, int dist) {
		int h = BlocksHelper.downRay(world, pos, dist);
		return pos.down(h);
	}
}
