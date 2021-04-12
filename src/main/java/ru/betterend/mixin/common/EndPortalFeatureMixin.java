package ru.betterend.mixin.common;

import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.NbtHelper;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.EndPortalFeature;
import ru.betterend.BetterEnd;
import ru.betterend.util.StructureHelper;
import ru.betterend.util.WorldDataUtil;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(EndPortalFeature.class)
public class EndPortalFeatureMixin {
	@Final
	@Shadow
	private boolean open;

	@Inject(method = "generate", at = @At("HEAD"), cancellable = true)
	private void bePortalGenerate(WorldGenLevel world, ChunkGenerator generator, Random random, BlockPos blockPos,
			NoneFeatureConfiguration config, CallbackInfoReturnable<Boolean> info) {
		if (!GeneratorOptions.hasPortal()) {
			info.setReturnValue(false);
			info.cancel();
		} else if (GeneratorOptions.replacePortal()) {
			blockPos = be_updatePos(blockPos, world);
			Structure structure = StructureHelper
					.readStructure(BetterEnd.makeID(open ? "portal/end_portal_active" : "portal/end_portal_inactive"));
			BlockPos size = structure.getSize();
			blockPos = blockPos.add(-(size.getX() >> 1), -3, -(size.getZ() >> 1));
			structure.place(world, blockPos, new StructurePlacementData(), random);
			info.setReturnValue(true);
			info.cancel();
		}
	}

	@ModifyVariable(method = "generate", ordinal = 0, at = @At("HEAD"))
	private BlockPos be_setPosOnGround(BlockPos blockPos, WorldGenLevel world) {
		return be_updatePos(blockPos, world);
	}

	private BlockPos be_updatePos(BlockPos blockPos, WorldGenLevel world) {
		if (GeneratorOptions.useNewGenerator()) {
			BlockPos pos = GeneratorOptions.getPortalPos();
			if (pos.equals(BlockPos.ORIGIN)) {
				int y = world.getChunk(blockPos).sampleHeightmap(Type.WORLD_SURFACE, blockPos.getX(), blockPos.getZ());
				if (y < 1) {
					y = 65;
				}
				pos = new BlockPos(pos.getX(), y, pos.getZ());
				GeneratorOptions.setPortalPos(pos);
				WorldDataUtil.getRootTag().put("portal", NbtHelper.fromBlockPos(pos));
				WorldDataUtil.saveFile();
			}
			return pos;
		}
		return blockPos;
	}
}
