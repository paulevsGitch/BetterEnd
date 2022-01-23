package ru.betterend.world.features.terrain;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.tag.CommonBlockTags;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlockFixer;
import ru.betterend.world.biome.EndBiome;

import java.util.Random;

public class DesertLakeFeature extends DefaultFeature {
	private static final BlockState END_STONE = Blocks.END_STONE.defaultBlockState();
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(15152);
	private static final MutableBlockPos POS = new MutableBlockPos();
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final Random random = featureConfig.random();
		BlockPos blockPos = featureConfig.origin();
		final WorldGenLevel world = featureConfig.level();
		double radius = MHelper.randRange(8.0, 15.0, random);
		double depth = radius * 0.5 * MHelper.randRange(0.8, 1.2, random);
		int dist = MHelper.floor(radius);
		int dist2 = MHelper.floor(radius * 1.5);
		int bott = MHelper.floor(depth);
		blockPos = getPosOnSurfaceWG(world, blockPos);
		
		if (blockPos.getY() < 10) return false;
		
		int waterLevel = blockPos.getY();
		
		BlockPos pos = getPosOnSurfaceRaycast(world, blockPos.north(dist).above(10), 20);
		if (Math.abs(blockPos.getY() - pos.getY()) > 5) return false;
		waterLevel = MHelper.min(pos.getY(), waterLevel);
		
		pos = getPosOnSurfaceRaycast(world, blockPos.south(dist).above(10), 20);
		if (Math.abs(blockPos.getY() - pos.getY()) > 5) return false;
		waterLevel = MHelper.min(pos.getY(), waterLevel);
		
		pos = getPosOnSurfaceRaycast(world, blockPos.east(dist).above(10), 20);
		if (Math.abs(blockPos.getY() - pos.getY()) > 5) return false;
		waterLevel = MHelper.min(pos.getY(), waterLevel);
		
		pos = getPosOnSurfaceRaycast(world, blockPos.west(dist).above(10), 20);
		if (Math.abs(blockPos.getY() - pos.getY()) > 5) return false;
		waterLevel = MHelper.min(pos.getY(), waterLevel);
		BlockState state;
		
		int minX = blockPos.getX() - dist2;
		int maxX = blockPos.getX() + dist2;
		int minZ = blockPos.getZ() - dist2;
		int maxZ = blockPos.getZ() + dist2;
		int maskMinX = minX - 1;
		int maskMinZ = minZ - 1;
		
		boolean[][] mask = new boolean[maxX - minX + 3][maxZ - minZ + 3];
		for (int x = minX; x <= maxX; x++) {
			POS.setX(x);
			int mx = x - maskMinX;
			for (int z = minZ; z <= maxZ; z++) {
				POS.setZ(z);
				int mz = z - maskMinZ;
				if (!mask[mx][mz]) {
					for (int y = waterLevel + 1; y <= waterLevel + 20; y++) {
						POS.setY(y);
						FluidState fluid = world.getFluidState(POS);
						if (!fluid.isEmpty()) {
							for (int i = -1; i < 2; i++) {
								int px = mx + i;
								for (int j = -1; j < 2; j++) {
									int pz = mz + j;
									mask[px][pz] = true;
								}
							}
							break;
						}
					}
				}
			}
		}
		
		for (int x = minX; x <= maxX; x++) {
			POS.setX(x);
			int x2 = x - blockPos.getX();
			x2 *= x2;
			int mx = x - maskMinX;
			for (int z = minZ; z <= maxZ; z++) {
				POS.setZ(z);
				int z2 = z - blockPos.getZ();
				z2 *= z2;
				int mz = z - maskMinZ;
				if (!mask[mx][mz]) {
					double size = 1;
					for (int y = blockPos.getY(); y <= blockPos.getY() + 20; y++) {
						POS.setY(y);
						double add = y - blockPos.getY();
						if (add > 5) {
							size *= 0.8;
							add = 5;
						}
						double r = (add * 1.8 + radius * (NOISE.eval(
							x * 0.2,
							y * 0.2,
							z * 0.2
						) * 0.25 + 0.75)) - 1.0 / size;
						if (r > 0) {
							r *= r;
							if (x2 + z2 <= r) {
								state = world.getBlockState(POS);
								if (state.is(CommonBlockTags.GEN_END_STONES)) {
									BlocksHelper.setWithoutUpdate(world, POS, AIR);
								}
								pos = POS.below();
								if (world.getBlockState(pos).is(CommonBlockTags.GEN_END_STONES)) {
									state = EndBiome.findTopMaterial(world, pos); //world.getBiome(pos).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
									if (y > waterLevel + 1) BlocksHelper.setWithoutUpdate(world, pos, state);
									else if (y > waterLevel)
										BlocksHelper.setWithoutUpdate(
											world,
											pos,
											random.nextBoolean() ? state : EndBlocks.ENDSTONE_DUST.defaultBlockState()
										);
									else
										BlocksHelper.setWithoutUpdate(
											world,
											pos,
											EndBlocks.ENDSTONE_DUST.defaultBlockState()
										);
								}
							}
						}
						else {
							break;
						}
					}
				}
			}
		}
		
		double aspect = ((double) radius / (double) depth);
		
		for (int x = blockPos.getX() - dist; x <= blockPos.getX() + dist; x++) {
			POS.setX(x);
			int x2 = x - blockPos.getX();
			x2 *= x2;
			int mx = x - maskMinX;
			for (int z = blockPos.getZ() - dist; z <= blockPos.getZ() + dist; z++) {
				POS.setZ(z);
				int z2 = z - blockPos.getZ();
				z2 *= z2;
				int mz = z - maskMinZ;
				if (!mask[mx][mz]) {
					for (int y = blockPos.getY() - bott; y < blockPos.getY(); y++) {
						POS.setY(y);
						double y2 = (double) (y - blockPos.getY()) * aspect;
						y2 *= y2;
						double r = radius * (NOISE.eval(x * 0.2, y * 0.2, z * 0.2) * 0.25 + 0.75);
						double rb = r * 1.2;
						r *= r;
						rb *= rb;
						if (y2 + x2 + z2 <= r) {
							state = world.getBlockState(POS);
							if (canReplace(state)) {
								state = world.getBlockState(POS.above());
								state = canReplace(state) ? (y < waterLevel ? WATER : AIR) : state;
								BlocksHelper.setWithoutUpdate(world, POS, state);
							}
							pos = POS.below();
							if (world.getBlockState(pos).is(CommonBlockTags.GEN_END_STONES)) {
								BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.ENDSTONE_DUST.defaultBlockState());
							}
							pos = POS.above();
							while (canReplace(state = world.getBlockState(pos)) && !state.isAir() && state.getFluidState()
																										  .isEmpty()) {
								BlocksHelper.setWithoutUpdate(world, pos, pos.getY() < waterLevel ? WATER : AIR);
								pos = pos.above();
							}
						}
						// Make border
						else if (y2 + x2 + z2 <= rb) {
							state = world.getBlockState(POS);
							if (state.is(CommonBlockTags.GEN_END_STONES) && world.isEmptyBlock(POS.above())) {
								BlocksHelper.setWithoutUpdate(world, POS, EndBlocks.END_MOSS);
							}
							else if (y < waterLevel) {
								if (world.isEmptyBlock(POS.above())) {
									state = EndBiome.findTopMaterial(world, pos); //world.getBiome(POS).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
									BlocksHelper.setWithoutUpdate(
										world,
										POS,
										random.nextBoolean() ? state : EndBlocks.ENDSTONE_DUST.defaultBlockState()
									);
									BlocksHelper.setWithoutUpdate(world, POS.below(), END_STONE);
								}
								else {
									BlocksHelper.setWithoutUpdate(
										world,
										POS,
										EndBlocks.ENDSTONE_DUST.defaultBlockState()
									);
									BlocksHelper.setWithoutUpdate(world, POS.below(), END_STONE);
								}
							}
						}
					}
				}
			}
		}
		
		BlockFixer.fixBlocks(
			world,
			new BlockPos(minX - 2, waterLevel - 2, minZ - 2),
			new BlockPos(maxX + 2, blockPos.getY() + 20, maxZ + 2)
		);
		
		return true;
	}
	
	private boolean canReplace(BlockState state) {
		return state.getMaterial()
					.isReplaceable() || state.is(CommonBlockTags.GEN_END_STONES) || state.is(EndBlocks.ENDSTONE_DUST) || state.getMaterial()
																												  .equals(
																													  Material.PLANT) || state
			.getMaterial()
			.equals(Material.WATER_PLANT);
	}
}
