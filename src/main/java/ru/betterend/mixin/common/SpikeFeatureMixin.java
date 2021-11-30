package ru.betterend.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bclib.api.WorldDataAPI;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.StructureHelper;
import ru.betterend.BetterEnd;
import ru.betterend.world.generator.GeneratorOptions;

import java.util.Random;

@Mixin(SpikeFeature.class)
public class SpikeFeatureMixin {
	@Inject(method = "place", at = @At("HEAD"), cancellable = true)
	private void be_place(FeaturePlaceContext<SpikeConfiguration> featurePlaceContext, CallbackInfoReturnable<Boolean> info) {
		if (!GeneratorOptions.hasPillars()) {
			info.setReturnValue(false);
		}
	}
	
	@Inject(method = "placeSpike", at = @At("HEAD"), cancellable = true)
	private void be_placeSpike(ServerLevelAccessor world, Random random, SpikeConfiguration config, SpikeFeature.EndSpike spike, CallbackInfo info) {
		int x = spike.getCenterX();
		int z = spike.getCenterZ();
		int radius = spike.getRadius();
		int minY = 0;
		
		long lx = (long) x;
		long lz = (long) z;
		if (lx * lx + lz * lz < 10000) {
			String pillarID = String.format("%d_%d", x, z);
			CompoundTag pillar = WorldDataAPI.getCompoundTag(BetterEnd.MOD_ID, "pillars");
			boolean haveValue = pillar.contains(pillarID);
			minY = haveValue ? pillar.getInt(pillarID) : world.getChunk(x >> 4, z >> 4).getHeight(Types.WORLD_SURFACE, x & 15, z);
			if (!haveValue) {
				pillar.putInt(pillarID, minY);
				WorldDataAPI.saveFile(BetterEnd.MOD_ID);
			}
		}
		else {
			minY = world.getChunk(x >> 4, z >> 4).getHeight(Types.WORLD_SURFACE, x & 15, z);
		}
		
		GeneratorOptions.setDirectSpikeHeight();
		int maxY = minY + spike.getHeight() - 64;
		
		if (GeneratorOptions.replacePillars() && be_radiusInRange(radius)) {
			radius--;
			StructureTemplate base = StructureHelper.readStructure(BetterEnd.makeID("pillars/pillar_base_" + radius));
			StructureTemplate top = StructureHelper.readStructure(BetterEnd.makeID("pillars/pillar_top_" + radius + (spike
				.isGuarded() ? "_cage" : "")));
			Vec3i side = base.getSize();
			BlockPos pos1 = new BlockPos(x - (side.getX() >> 1), minY - 3, z - (side.getZ() >> 1));
			minY = pos1.getY() + side.getY();
			side = top.getSize();
			BlockPos pos2 = new BlockPos(x - (side.getX() >> 1), maxY, z - (side.getZ() >> 1));
			maxY = pos2.getY();
			
			StructurePlaceSettings data = new StructurePlaceSettings();
			base.placeInWorld(world, pos1, pos1, data, random, 2);
			top.placeInWorld(world, pos2, pos2, data, random, 2);
			
			int r2 = radius * radius + 1;
			MutableBlockPos mut = new MutableBlockPos();
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
								if ((px == radius || px == -radius || pz == radius || pz == -radius) && random.nextInt(
									24) == 0) {
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
			MutableBlockPos mut = new MutableBlockPos();
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
			
			EndCrystal crystal = EntityType.END_CRYSTAL.create(world.getLevel());
			crystal.setBeamTarget(config.getCrystalBeamTarget());
			crystal.setInvulnerable(config.isCrystalInvulnerable());
			crystal.moveTo(x + 0.5D, maxY + 1, z + 0.5D, random.nextFloat() * 360.0F, 0.0F);
			world.addFreshEntity(crystal);
			
			if (spike.isGuarded()) {
				for (int px = -2; px <= 2; ++px) {
					boolean bl = Mth.abs(px) == 2;
					for (int pz = -2; pz <= 2; ++pz) {
						boolean bl2 = Mth.abs(pz) == 2;
						for (int py = 0; py <= 3; ++py) {
							boolean bl3 = py == 3;
							if (bl || bl2 || bl3) {
								boolean bl4 = px == -2 || px == 2 || bl3;
								boolean bl5 = pz == -2 || pz == 2 || bl3;
								BlockState blockState = (BlockState) ((BlockState) ((BlockState) ((BlockState) Blocks.IRON_BARS
									.defaultBlockState()
									.setValue(IronBarsBlock.NORTH, bl4 && pz != -2)).setValue(
									IronBarsBlock.SOUTH,
									bl4 && pz != 2
								)).setValue(IronBarsBlock.WEST, bl5 && px != -2)).setValue(
									IronBarsBlock.EAST,
									bl5 && px != 2
								);
								BlocksHelper.setWithoutUpdate(
									world,
									mut.set(spike.getCenterX() + px, maxY + py, spike.getCenterZ() + pz),
									blockState
								);
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
