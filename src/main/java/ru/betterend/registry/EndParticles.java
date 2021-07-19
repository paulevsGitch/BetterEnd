package ru.betterend.registry;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import ru.betterend.BetterEnd;
import ru.betterend.particle.FireflyParticle;
import ru.betterend.particle.InfusionParticle;
import ru.betterend.particle.InfusionParticleType;
import ru.betterend.particle.ParticleBlackSpore;
import ru.betterend.particle.ParticleGeyser;
import ru.betterend.particle.ParticleGlowingSphere;
import ru.betterend.particle.ParticleJungleSpore;
import ru.betterend.particle.ParticleSnowflake;
import ru.betterend.particle.ParticleSulphur;
import ru.betterend.particle.ParticleTenaneaPetal;
import ru.betterend.particle.PaticlePortalSphere;
import ru.betterend.particle.SmaragdantParticle;

public class EndParticles {
	public static final SimpleParticleType GLOWING_SPHERE = register("glowing_sphere");
	public static final SimpleParticleType PORTAL_SPHERE = register("portal_sphere");
	public static final ParticleType<InfusionParticleType> INFUSION = register(
		"infusion",
		FabricParticleTypes.complex(InfusionParticleType.PARAMETERS_FACTORY)
	);
	public static final SimpleParticleType SULPHUR_PARTICLE = register("sulphur_particle");
	public static final SimpleParticleType GEYSER_PARTICLE = registerFar("geyser_particle");
	public static final SimpleParticleType SNOWFLAKE = register("snowflake");
	public static final SimpleParticleType AMBER_SPHERE = register("amber_sphere");
	public static final SimpleParticleType BLACK_SPORE = register("black_spore");
	public static final SimpleParticleType TENANEA_PETAL = register("tenanea_petal");
	public static final SimpleParticleType JUNGLE_SPORE = register("jungle_spore");
	public static final SimpleParticleType FIREFLY = register("firefly");
	public static final SimpleParticleType SMARAGDANT = register("smaragdant_particle");
	
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
		ParticleFactoryRegistry.getInstance().register(JUNGLE_SPORE, ParticleJungleSpore.FactoryJungleSpore::new);
		ParticleFactoryRegistry.getInstance().register(FIREFLY, FireflyParticle.FireflyParticleFactory::new);
		ParticleFactoryRegistry.getInstance().register(SMARAGDANT, SmaragdantParticle.SmaragdantParticleFactory::new);
	}
	
	private static SimpleParticleType register(String name) {
		return Registry.register(Registry.PARTICLE_TYPE, BetterEnd.makeID(name), FabricParticleTypes.simple());
	}
	
	private static SimpleParticleType registerFar(String name) {
		return Registry.register(Registry.PARTICLE_TYPE, BetterEnd.makeID(name), FabricParticleTypes.simple(true));
	}
	
	private static <T extends ParticleOptions> ParticleType<T> register(String name, ParticleType<T> type) {
		return Registry.register(Registry.PARTICLE_TYPE, BetterEnd.makeID(name), type);
	}
}
