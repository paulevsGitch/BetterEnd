package ru.betterend.blocks.entities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.blocks.HydrothermalVentBlock;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndParticles;

public class BlockEntityHydrothermalVent extends BlockEntity implements TickableBlockEntity {
	public BlockEntityHydrothermalVent() {
		super(EndBlockEntities.HYDROTHERMAL_VENT);
	}

	@Override
	public void tick() {
		if (level.random.nextInt(20) == 0) {
			double x = worldPosition.getX() + level.random.nextDouble();
			double y = worldPosition.getY() + 0.9 + level.random.nextDouble() * 0.3;
			double z = worldPosition.getZ() + level.random.nextDouble();
			BlockState state = getBlockState();
			if (state.is(EndBlocks.HYDROTHERMAL_VENT) && state.getValue(HydrothermalVentBlock.ACTIVATED)) {
				if (state.getValue(HydrothermalVentBlock.WATERLOGGED)) {
					level.addParticle(EndParticles.GEYSER_PARTICLE, x, y, z, 0, 0, 0);
				}
				else {
					level.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
				}
			}
		}
	}
}
