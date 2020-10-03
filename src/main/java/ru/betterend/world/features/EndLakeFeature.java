package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class EndLakeFeature extends DefaultFeature {
	private static final BlockState END_STONE = Blocks.END_STONE.getDefaultState();
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(15152);
	private static final Mutable POS = new Mutable();
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig featureConfig) {
		double radius = MHelper.randRange(10.0, 20.0, random);
		double depth = radius * 0.5 * MHelper.randRange(0.8, 1.2, random);
		int dist = MHelper.floor(radius);
		int dist2 = MHelper.floor(radius * 1.5);
		int bott = MHelper.floor(depth);
		blockPos = getPosOnSurfaceWG(world, blockPos);
		if (blockPos.getY() < 10) return false;
		
		int waterLevel = blockPos.getY();
		
		BlockPos pos = getPosOnSurfaceRaycast(world, blockPos.north(dist).up(10), 20);
		if (Math.abs(blockPos.getY() - pos.getY()) > 5) return false;
		waterLevel = MHelper.min(pos.getY(), waterLevel);
		
		pos = getPosOnSurfaceRaycast(world, blockPos.south(dist).up(10), 20);
		if (Math.abs(blockPos.getY() - pos.getY()) > 5) return false;
		waterLevel = MHelper.min(pos.getY(), waterLevel);
		
		pos = getPosOnSurfaceRaycast(world, blockPos.east(dist).up(10), 20);
		if (Math.abs(blockPos.getY() - pos.getY()) > 5) return false;
		waterLevel = MHelper.min(pos.getY(), waterLevel);
		
		pos = getPosOnSurfaceRaycast(world, blockPos.west(dist).up(10), 20);
		if (Math.abs(blockPos.getY() - pos.getY()) > 5) return false;
		waterLevel = MHelper.min(pos.getY(), waterLevel);
		BlockState state;
		
		for (int y = blockPos.getY(); y <= blockPos.getY() + 10; y++) {
			POS.setY(y);
			int add = y - blockPos.getY();
			for (int x = blockPos.getX() - dist2; x <= blockPos.getX() + dist2; x++) {
				POS.setX(x);
				int x2 = x - blockPos.getX();
				x2 *= x2;
				for (int z = blockPos.getZ() - dist2; z <= blockPos.getZ() + dist2; z++) {
					POS.setZ(z);
					int z2 = z - blockPos.getZ();
					z2 *= z2;
					double r = add * 1.8 + radius  * (NOISE.eval(x * 0.2, y * 0.2, z * 0.2) * 0.25 + 0.75);
					r *= r;
					if (x2 + z2 <= r) {
						state = world.getBlockState(POS);
						if (state.isIn(BlockTagRegistry.END_GROUND) || !state.canPlaceAt(world, POS) || state.getMaterial().equals(Material.PLANT)/* || state.getBlock() == BlockRegistry.ENDSTONE_DUST*/) {
							BlocksHelper.setWithoutUpdate(world, POS, AIR);
						}
						pos = POS.down();
						if (world.getBlockState(pos).isIn(BlockTagRegistry.END_GROUND))
						{
							state = world.getBiome(pos).getGenerationSettings().getSurfaceConfig().getTopMaterial();
							if (y > waterLevel + 1)
								BlocksHelper.setWithoutUpdate(world, pos, state);
							else if (y > waterLevel)
								BlocksHelper.setWithoutUpdate(world, pos, random.nextBoolean() ? state : BlockRegistry.ENDSTONE_DUST.getDefaultState());
							else
								BlocksHelper.setWithoutUpdate(world, pos, BlockRegistry.ENDSTONE_DUST.getDefaultState());
						}
						/*pos = POS.up();
						while (world.getBlockState(pos).isIn(BlockTagRegistry.END_GROUND) || !state.canPlaceAt(world, pos) || state.getBlock() == BlockRegistry.ENDSTONE_DUST) {
							BlocksHelper.setWithoutUpdate(world, pos, AIR);
							pos = pos.up();
						}*/
					}
				}
			}
		}
		
		double aspect = ((double) radius / (double) depth);
		for (int y = blockPos.getY() - bott; y < blockPos.getY(); y++) {
			POS.setY(y);
			double y2 = (double) (y - blockPos.getY()) * aspect;
			y2 *= y2;
			for (int x = blockPos.getX() - dist; x <= blockPos.getX() + dist; x++) {
				POS.setX(x);
				int x2 = x - blockPos.getX();
				x2 *= x2;
				for (int z = blockPos.getZ() - dist; z <= blockPos.getZ() + dist; z++) {
					POS.setZ(z);
					int z2 = z - blockPos.getZ();
					z2 *= z2;
					double r = radius * (NOISE.eval(x * 0.2, y * 0.2, z * 0.2) * 0.25 + 0.75);
					double rb = r * 1.2;
					r *= r;
					rb *= rb;
					if (y2 + x2 + z2 <= r) {
						state = world.getBlockState(POS);
						if (state.isIn(BlockTagRegistry.END_GROUND) || !state.canPlaceAt(world, POS) || state.getBlock() == BlockRegistry.ENDSTONE_DUST) {
							BlocksHelper.setWithoutUpdate(world, POS, y < waterLevel ? WATER : AIR);
						}
						pos = POS.down();
						if (world.getBlockState(pos).getBlock().isIn(BlockTagRegistry.END_GROUND))
							BlocksHelper.setWithoutUpdate(world, POS.down(), BlockRegistry.ENDSTONE_DUST.getDefaultState());
						pos = POS.up();
						if (world.getBlockState(pos).isIn(BlockTagRegistry.END_GROUND)) {
							while (world.getBlockState(pos).isIn(BlockTagRegistry.END_GROUND)) {
								BlocksHelper.setWithoutUpdate(world, pos, pos.getY() < waterLevel ? WATER : AIR);
								pos = pos.up();
							}
						}
					}
					else if (y <= waterLevel && y2 + x2 + z2 <= rb) {
						if (world.getBlockState(POS).getMaterial().isReplaceable()) {
							if (world.isAir(POS.up())) {
								state = world.getBiome(POS).getGenerationSettings().getSurfaceConfig().getTopMaterial();
								BlocksHelper.setWithoutUpdate(world, POS, random.nextBoolean() ? state : BlockRegistry.ENDSTONE_DUST.getDefaultState());
								BlocksHelper.setWithoutUpdate(world, POS.down(), END_STONE);
							}
							else {
								BlocksHelper.setWithoutUpdate(world, POS, BlockRegistry.ENDSTONE_DUST.getDefaultState());
								BlocksHelper.setWithoutUpdate(world, POS.down(), END_STONE);
							}
						}
					}
				}
			}
		}
		
		return true;
	}
}
