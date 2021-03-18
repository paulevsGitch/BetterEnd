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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import ru.betterend.util.BlocksHelper;
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
		int minY = world.getChunk(spike.getCenterX() >> 4, spike.getCenterZ() >> 4).sampleHeightmap(Type.WORLD_SURFACE, spike.getCenterX() & 15, spike.getCenterZ() & 15) - 15;
		int maxY = minY + spike.getHeight() - 50;
		
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

		info.cancel();
	}
}
