package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockPlant;
import ru.betterend.registry.EndBlocks;

public class BlockMurkweed extends BlockPlant {
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		double x = pos.getX() + random.nextDouble();
		double y = pos.getY() + random.nextDouble() * 0.5 + 0.5;
		double z = pos.getZ() + random.nextDouble();
		double v = random.nextDouble() * 0.1;
		world.addParticle(ParticleTypes.ENTITY_EFFECT, x, y, z, v, v, v);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity && !((LivingEntity) entity).hasStatusEffect(StatusEffects.BLINDNESS)) {
			((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50));
		}
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.isOf(EndBlocks.SHADOW_GRASS);
	}
}
