package ru.betterend.blocks.entities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Tickable;
import ru.betterend.blocks.HydrothermalVentBlock;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndParticles;

public class BlockEntityHydrothermalVent extends BlockEntity implements Tickable {
	public BlockEntityHydrothermalVent() {
		super(EndBlockEntities.HYDROTHERMAL_VENT);
	}

	@Override
	public void tick() {
		if (world.random.nextInt(20) == 0) {
			double x = pos.getX() + world.random.nextDouble();
			double y = pos.getY() + 0.9 + world.random.nextDouble() * 0.3;
			double z = pos.getZ() + world.random.nextDouble();
			BlockState state = getCachedState();
			if (state.is(EndBlocks.HYDROTHERMAL_VENT) && state.getValue(HydrothermalVentBlock.ACTIVATED)) {
				if (state.getValue(HydrothermalVentBlock.WATERLOGGED)) {
					world.addParticle(EndParticles.GEYSER_PARTICLE, x, y, z, 0, 0, 0);
				} else {
					world.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
				}
			}
		}
	}
}
