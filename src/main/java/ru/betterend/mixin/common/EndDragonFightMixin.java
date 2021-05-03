package ru.betterend.mixin.common;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.dimension.end.DragonRespawnAnimation;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.phys.AABB;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(EndDragonFight.class)
public class EndDragonFightMixin {
	@Shadow
	private DragonRespawnAnimation respawnStage;
	@Shadow
	private boolean dragonKilled;
	@Shadow
	private BlockPos portalLocation;
	@Final
	@Shadow
	private static Logger LOGGER;
	@Final
	@Shadow
	private ServerLevel level;

	@Shadow
	private BlockPattern.BlockPatternMatch findExitPortal() {
		return null;
	}

	@Shadow
	private void spawnExitPortal(boolean bl) {}

	@Shadow
	private void respawnDragon(List<EndCrystal> list) {}

	@Inject(method = "tryRespawn", at = @At("HEAD"), cancellable = true)
	private void be_tryRespawnDragon(CallbackInfo info) {
		if (GeneratorOptions.replacePortal() && GeneratorOptions.hasDragonFights() && this.dragonKilled && this.respawnStage == null) {
			BlockPos blockPos = this.portalLocation;
			if (blockPos == null) {
				LOGGER.debug("Tried to respawn, but need to find the portal first.");
				BlockPattern.BlockPatternMatch blockPatternMatch = this.findExitPortal();
				if (blockPatternMatch == null) {
					LOGGER.debug("Couldn't find a portal, so we made one.");
					this.spawnExitPortal(true);
				}
				else {
					LOGGER.debug("Found the exit portal & temporarily using it.");
				}

				blockPos = this.portalLocation;
			}

			List<EndCrystal> crystals = Lists.newArrayList();
			BlockPos center = blockPos.above(2);
			for (Direction dir : BlocksHelper.HORIZONTAL) {
				List<EndCrystal> crystalList = this.level.getEntitiesOfClass(EndCrystal.class, new AABB(center.relative(dir, 3)));
				if (crystalList.isEmpty()) {
					info.cancel();
					return;
				}

				crystals.addAll(crystalList);
			}

			LOGGER.debug("Found all crystals, respawning dragon.");
			this.respawnDragon(crystals);
			info.cancel();
		}
	}
}
