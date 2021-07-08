package ru.betterend.world.surface;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import ru.betterend.registry.EndBlocks;

public class SurfaceBuilders {
	public static final SurfaceBuilderBaseConfiguration DEFAULT_END_CONFIG = makeSimpleConfig(Blocks.END_STONE);
	public static final SurfaceBuilderBaseConfiguration FLAVOLITE_CONFIG = makeSimpleConfig(EndBlocks.FLAVOLITE.stone);
	public static final SurfaceBuilderBaseConfiguration BRIMSTONE_CONFIG = makeSimpleConfig(EndBlocks.BRIMSTONE);
	public static final SurfaceBuilderBaseConfiguration SULFURIC_ROCK_CONFIG = makeSimpleConfig(EndBlocks.SULPHURIC_ROCK.stone);

	public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> SULPHURIC_SURFACE = register("sulphuric_surface", new SulphuricSurfaceBuilder());

	private static SurfaceBuilder<SurfaceBuilderBaseConfiguration> register(String name, SurfaceBuilder<SurfaceBuilderBaseConfiguration> builder) {
		return Registry.register(Registry.SURFACE_BUILDER, name, builder);
	}

	private static SurfaceBuilderBaseConfiguration makeSimpleConfig(Block block) {
		BlockState state = block.defaultBlockState();
		return new SurfaceBuilderBaseConfiguration(state, state, state);
	}

	public static void register() {
	}
}
