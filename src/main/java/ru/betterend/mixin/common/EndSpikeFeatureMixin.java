package ru.betterend.mixin.common;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import ru.betterend.BetterEnd;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.StructureHelper;
import ru.betterend.util.WorldDataUtil;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(EndSpikeFeature.class)
public class EndSpikeFeatureMixin {
	@Inject(method = "generate", at = @At("HEAD"), cancellable = true)
	private void beSpikeGenerate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, EndSpikeFeatureConfig endSpikeFeatureConfig, CallbackInfoReturnable<Boolean> info) {
		if (!GeneratorOptions.hasPillars()) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "generateSpike", at = @At("HEAD"), cancellable = true)
	private void be_generateSpike(ServerWorldAccess world, Random random, EndSpikeFeatureConfig config, EndSpikeFeature.Spike spike, CallbackInfo info) {
		int x = spike.getCenterX();
		int z = spike.getCenterZ();
		int radius = spike.getRadius();
		int minY = 0;
		
		long lx = (long) x;
		long lz = (long) z;
		if (lx * lx + lz * lz < 10000) {
			String pillarID = String.format("%d_%d", x, z);
			CompoundTag pillar = WorldDataUtil.getCompoundTag("pillars");
			boolean haveValue = pillar.contains(pillarID);
			minY = haveValue ? pillar.getInt(pillarID) : world.getChunk(x >> 4, z >> 4).sampleHeightmap(Type.WORLD_SURFACE, x & 15, z);
			if (!haveValue) {
				pillar.putInt(pillarID, minY);
				WorldDataUtil.saveFile();
			}
		}
		else {
			minY = world.getChunk(x >> 4, z >> 4).sampleHeightmap(Type.WORLD_SURFACE, x & 15, z);
		}
		
		int maxY = minY + spike.getHeight() - 64;
		
		if (GeneratorOptions.replacePillars() && be_radiusInRange(radius)) {
			radius--;
			Structure base = StructureHelper.readStructure(BetterEnd.makeID("pillars/pillar_base_" + radius));
			Structure top = StructureHelper.readStructure(BetterEnd.makeID("pillars/pillar_top_" + radius + (spike.isGuarded() ? "_cage" : "")));
			BlockPos side = base.getSize();
			BlockPos pos1 = new BlockPos(x - (side.getX() >> 1), minY - 3, z - (side.getZ() >> 1));
			minY = pos1.getY() + side.getY();
			side = top.getSize();
			BlockPos pos2 = new BlockPos(x - (side.getX() >> 1), maxY, z - (side.getZ() >> 1));
			maxY = pos2.getY();
			
			StructurePlacementData data = new StructurePlacementData();
			base.place(world, pos1, data, random);
			top.place(world, pos2, data, random);
			
			int r2 = radius * radius + 1;
			Mutable mut = new Mutable();
			for (int px = -radius; px <= radius; px++) {
				mut.setX(x + px);
				int x2 = px * px;
				for (int pz = -radius; pz <= radius; pz++) {
					mut.setZ(z + pz);
					int z2 = pz * pz;
					if (x2 + z2 <= r2) {
						for (int py = minY; py < maxY; py++) {
							mut.setY(py);
							if (world.getBlockState(mut).getMaterial().isReplaceable()) {
								if ((px == radius || px == -radius || pz == radius || pz == -radius) && random.nextInt(24) == 0) {
									BlocksHelper.setWithoutUpdate(world, mut, Blocks.CRYING_OBSIDIAN);
								}
								else {
									BlocksHelper.setWithoutUpdate(world, mut, Blocks.OBSIDIAN);
								}
							}
						}
					}
				}
			}
		}
		else {
			minY -= 15;
			int r2 = radius * radius + 1;
			Mutable mut = new Mutable();
			for (int px = -radius; px <= radius; px++) {
				mut.setX(x + px);
				int x2 = px * px;
				for (int pz = -radius; pz <= radius; pz++) {
					mut.setZ(z + pz);
					int z2 = pz * pz;
					if (x2 + z2 <= r2) {
						for (int py = minY; py < maxY; py++) {
							mut.setY(py);
							if (world.getBlockState(mut).getMaterial().isReplaceable()) {
								BlocksHelper.setWithoutUpdate(world, mut, Blocks.OBSIDIAN);
							}
						}
					}
				}
			}
			mut.setX(x);
			mut.setZ(z);
			mut.setY(maxY);
			BlocksHelper.setWithoutUpdate(world, mut, Blocks.BEDROCK);
			
			EndCrystalEntity crystal = EntityType.END_CRYSTAL.create(world.toServerWorld());
			crystal.setBeamTarget(config.getPos());
			crystal.setInvulnerable(config.isCrystalInvulnerable());
			crystal.refreshPositionAndAngles(x + 0.5D, maxY + 1, z + 0.5D, random.nextFloat() * 360.0F, 0.0F);
			world.spawnEntity(crystal);
			
			if (spike.isGuarded()) {
				for (int px = -2; px <= 2; ++px) {
					boolean bl = MathHelper.abs(px) == 2;
					for (int pz = -2; pz <= 2; ++pz) {
						boolean bl2 = MathHelper.abs(pz) == 2;
						for (int py = 0; py <= 3; ++py) {
							boolean bl3 = py == 3;
							if (bl || bl2 || bl3) {
								boolean bl4 = px == -2 || px == 2 || bl3;
								boolean bl5 = pz == -2 || pz == 2 || bl3;
								BlockState blockState = (BlockState) ((BlockState) ((BlockState) ((BlockState) Blocks.IRON_BARS.getDefaultState().with(PaneBlock.NORTH, bl4 && pz != -2)).with(PaneBlock.SOUTH, bl4 && pz != 2)).with(PaneBlock.WEST, bl5 && px != -2)).with(PaneBlock.EAST, bl5 && px != 2);
								BlocksHelper.setWithoutUpdate(world, mut.set(spike.getCenterX() + px, maxY + py, spike.getCenterZ() + pz), blockState);
							}
						}
					}
				}
			}
		}

		info.cancel();
	}
	
	private boolean be_radiusInRange(int radius) {
		return radius > 1 && radius < 6;
	}
}
