package ru.betterend.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Tickable;
import ru.betterend.blocks.BlockHydrothermalVent;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndParticles;

public class BlockEntityHydrothermalVent extends BlockEntity implements Tickable {
	public BlockEntityHydrothermalVent() {
		super(EndBlockEntities.HYDROTHERMAL_VENT);
	}

	@Override
	public void tick() {
		if (world.random.nextInt(32) == 0) {
			double x = pos.getX() + world.random.nextDouble();
			double y = pos.getY() + 0.9 + world.random.nextDouble() * 0.3;
			double z = pos.getZ() + world.random.nextDouble();
			BlockState state = getCachedState();
			if (state.isOf(EndBlocks.HYDROTHERMAL_VENT)) {
				if (getCachedState().get(BlockHydrothermalVent.WATERLOGGED)) {
					world.addParticle(EndParticles.GEYSER_PARTICLE, x, y, z, 0, 0, 0);
				}
				else {
					world.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
				}
			}
		}
	}
}
