package ru.betterend.world.surface;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Registry;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import ru.betterend.registry.EndBlocks;

public class SurfaceBuilders {
	public static final TernarySurfaceConfig DEFAULT_END_CONFIG = makeSimpleConfig(Blocks.END_STONE);
	public static final TernarySurfaceConfig FLAVOLITE_CONFIG = makeSimpleConfig(EndBlocks.FLAVOLITE.stone);
	public static final TernarySurfaceConfig BRIMSTONE_CONFIG = makeSimpleConfig(EndBlocks.BRIMSTONE);
	public static final TernarySurfaceConfig SULFURIC_ROCK_CONFIG = makeSimpleConfig(EndBlocks.SULPHURIC_ROCK.stone);

	public static final SurfaceBuilder<TernarySurfaceConfig> SULPHURIC_SURFACE = register("sulphuric_surface",
			new SulphuricSurfaceBuilder());

	private static SurfaceBuilder<TernarySurfaceConfig> register(String name,
			SurfaceBuilder<TernarySurfaceConfig> builder) {
		return Registry.register(Registry.SURFACE_BUILDER, name, builder);
	}

	private static TernarySurfaceConfig makeSimpleConfig(Block block) {
		BlockState state = block.defaultBlockState();
		return new TernarySurfaceConfig(state, state, state);
	}

	public static void register() {
	}
}
