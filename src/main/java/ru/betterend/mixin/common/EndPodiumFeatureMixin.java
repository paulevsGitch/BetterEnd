package ru.betterend.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bclib.api.WorldDataAPI;
import ru.bclib.util.StructureHelper;
import ru.betterend.BetterEnd;
import ru.betterend.world.generator.GeneratorOptions;

import java.util.Random;

@Mixin(EndPodiumFeature.class)
public class EndPodiumFeatureMixin {
	@Final
	@Shadow
	private boolean active;
	
	@Inject(method = "place", at = @At("HEAD"), cancellable = true)
	private void be_place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext, CallbackInfoReturnable<Boolean> info) {
		if (!GeneratorOptions.hasPortal()) {
			info.setReturnValue(false);
			info.cancel();
		}
		else if (GeneratorOptions.replacePortal()) {
			Random random = featurePlaceContext.random();
			WorldGenLevel world = featurePlaceContext.level();
			BlockPos blockPos = be_updatePos(featurePlaceContext.origin(), world);
			StructureTemplate structure = StructureHelper.readStructure(BetterEnd.makeID(active ? "portal/end_portal_active" : "portal/end_portal_inactive"));
			Vec3i size = structure.getSize();
			blockPos = blockPos.offset(-(size.getX() >> 1), -1, -(size.getZ() >> 1));
			structure.placeInWorld(world, blockPos, blockPos, new StructurePlaceSettings(), random, 2);
			info.setReturnValue(true);
			info.cancel();
		}
	}
	
	@ModifyVariable(method = "place", ordinal = 0, at = @At("HEAD"))
	private FeaturePlaceContext<NoneFeatureConfiguration> be_setPosOnGround(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel world = featurePlaceContext.level();
		BlockPos pos = be_updatePos(featurePlaceContext.origin(), world);
		return new FeaturePlaceContext<NoneFeatureConfiguration>(world, featurePlaceContext.chunkGenerator(), featurePlaceContext.random(), pos, featurePlaceContext.config());
	}
	
	private BlockPos be_updatePos(BlockPos blockPos, WorldGenLevel world) {
		if (GeneratorOptions.useNewGenerator()) {
			BlockPos pos = GeneratorOptions.getPortalPos();
			if (pos.equals(BlockPos.ZERO)) {
				int y = world.getChunk(0, 0, ChunkStatus.FULL).getHeight(Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ());
				if (y < 1) {
					y = 65;
				}
				pos = new BlockPos(pos.getX(), y, pos.getZ());
				GeneratorOptions.setPortalPos(pos);
				WorldDataAPI.getRootTag(BetterEnd.MOD_ID).put("portal", NbtUtils.writeBlockPos(pos));
				WorldDataAPI.saveFile(BetterEnd.MOD_ID);
			}
			return pos;
		}
		return blockPos;
	}
}
