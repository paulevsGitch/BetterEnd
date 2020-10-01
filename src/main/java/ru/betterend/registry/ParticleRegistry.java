package ru.betterend.registry;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.particle.AnimatedTestParticle;

public class ParticleRegistry {
	public static final DefaultParticleType TEST = register("test");
	
	public static void register() {
		ParticleFactoryRegistry.getInstance().register(TEST, AnimatedTestParticle.Factory::new);
	}
	
	private static DefaultParticleType register(String name) {
		return Registry.register(Registry.PARTICLE_TYPE, BetterEnd.getResId(name), FabricParticleTypes.simple());
	}
}
