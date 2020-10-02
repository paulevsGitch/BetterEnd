package ru.betterend.registry;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.particle.ParticleGlowingSphere;

public class ParticleRegistry {
	public static final DefaultParticleType GLOWING_SPHERE = register("glowing_sphere");
	
	public static void register() {
		ParticleFactoryRegistry.getInstance().register(GLOWING_SPHERE, ParticleGlowingSphere.FactoryGlowingSphere::new);
	}
	
	private static DefaultParticleType register(String name) {
		return Registry.register(Registry.PARTICLE_TYPE, BetterEnd.getIdentifier(name), FabricParticleTypes.simple());
	}
}
