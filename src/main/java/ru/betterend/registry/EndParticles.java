package ru.betterend.registry;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.particle.InfusionParticle;
import ru.betterend.particle.InfusionParticleType;
import ru.betterend.particle.ParticleBlackSpore;
import ru.betterend.particle.ParticleGeyser;
import ru.betterend.particle.ParticleGlowingSphere;
import ru.betterend.particle.ParticleSnowflake;
import ru.betterend.particle.ParticleSulphur;
import ru.betterend.particle.ParticleTenaneaPetal;
import ru.betterend.particle.PaticlePortalSphere;

public class EndParticles {
	public static final DefaultParticleType GLOWING_SPHERE = register("glowing_sphere");
	public static final DefaultParticleType PORTAL_SPHERE = register("portal_sphere");
	public static final ParticleType<InfusionParticleType> INFUSION = register("infusion", FabricParticleTypes.complex(InfusionParticleType.PARAMETERS_FACTORY));
	public static final DefaultParticleType SULPHUR_PARTICLE = register("sulphur_particle");
	public static final DefaultParticleType GEYSER_PARTICLE = registerFar("geyser_particle");
	public static final DefaultParticleType SNOWFLAKE = register("snowflake");
	public static final DefaultParticleType AMBER_SPHERE = register("amber_sphere");
	public static final DefaultParticleType BLACK_SPORE = register("black_spore");
	public static final DefaultParticleType TENANEA_PETAL = register("tenanea_petal");
	
	public static void register() {
		ParticleFactoryRegistry.getInstance().register(GLOWING_SPHERE, ParticleGlowingSphere.FactoryGlowingSphere::new);
		ParticleFactoryRegistry.getInstance().register(PORTAL_SPHERE, PaticlePortalSphere.FactoryPortalSphere::new);
		ParticleFactoryRegistry.getInstance().register(INFUSION, InfusionParticle.InfusionFactory::new);
		ParticleFactoryRegistry.getInstance().register(SULPHUR_PARTICLE, ParticleSulphur.FactorySulphur::new);
		ParticleFactoryRegistry.getInstance().register(GEYSER_PARTICLE, ParticleGeyser.FactoryGeyser::new);
		ParticleFactoryRegistry.getInstance().register(SNOWFLAKE, ParticleSnowflake.FactorySnowflake::new);
		ParticleFactoryRegistry.getInstance().register(AMBER_SPHERE, ParticleGlowingSphere.FactoryGlowingSphere::new);
		ParticleFactoryRegistry.getInstance().register(BLACK_SPORE, ParticleBlackSpore.FactoryBlackSpore::new);
		ParticleFactoryRegistry.getInstance().register(TENANEA_PETAL, ParticleTenaneaPetal.FactoryTenaneaPetal::new);
	}
	
	private static DefaultParticleType register(String name) {
		return Registry.register(Registry.PARTICLE_TYPE, BetterEnd.makeID(name), FabricParticleTypes.simple());
	}
	
	private static DefaultParticleType registerFar(String name) {
		return Registry.register(Registry.PARTICLE_TYPE, BetterEnd.makeID(name), FabricParticleTypes.simple(true));
	}
	
	private static <T extends ParticleEffect> ParticleType<T> register(String name, ParticleType<T> type) {
		return Registry.register(Registry.PARTICLE_TYPE, BetterEnd.makeID(name), type);
	}
}
