package ru.betterend.registry;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.particle.ParticleGlowingSphere;
import ru.betterend.particle.PaticlePortalSphere;

public class EndParticles {
	public static final DefaultParticleType GLOWING_SPHERE = register("glowing_sphere");
	public static final DefaultParticleType PORTAL_SPHERE = register("portal_sphere");
	
	public static void register() {
		ParticleFactoryRegistry.getInstance().register(GLOWING_SPHERE, ParticleGlowingSphere.FactoryGlowingSphere::new);
		ParticleFactoryRegistry.getInstance().register(PORTAL_SPHERE, PaticlePortalSphere.FactoryPortalSphere::new);
	}
	
	private static DefaultParticleType register(String name) {
		return Registry.register(Registry.PARTICLE_TYPE, BetterEnd.makeID(name), FabricParticleTypes.simple());
	}
}
