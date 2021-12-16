package ru.betterend.mixin.common;

import java.util.HashSet;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.google.common.collect.Sets;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.betterend.world.generator.EndNoiseFiller;
import ru.betterend.world.generator.GeneratorOptions;
import ru.betterend.world.generator.TerrainGenerator;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin extends ChunkGenerator {
	@Final
	@Shadow
	protected Supplier<NoiseGeneratorSettings> settings;
	
	@Final
	@Shadow
	protected BlockState defaultBlock;
	
	@Final
	@Shadow
	private SurfaceSystem surfaceSystem;
	
	public NoiseBasedChunkGeneratorMixin(BiomeSource populationSource, BiomeSource biomeSource, StructureSettings structuresConfig, long worldSeed) {
		super(populationSource, biomeSource, structuresConfig, worldSeed);
	}
	
	@Inject(method = "<init>(Lnet/minecraft/core/Registry;Lnet/minecraft/world/level/biome/BiomeSource;Lnet/minecraft/world/level/biome/BiomeSource;JLjava/util/function/Supplier;)V", at = @At("TAIL"))
	private void beOnInit(Registry registry, BiomeSource biomeSource, BiomeSource biomeSource2, long seed, Supplier supplier, CallbackInfo ci) {
		TerrainGenerator.initNoise(seed);
		if (GeneratorOptions.useNewGenerator() && settings.get().stable(NoiseGeneratorSettings.END)) {
			EndNoiseFiller.INSTANCE.setBiomeSource(biomeSource);
		}
	}

	//TODO: 1.18 Find another place for this
//	@Inject(method = "fillNoiseColumn([DIIII)V", at = @At("HEAD"), cancellable = true, allow = 2)
//	private void be_fillNoiseColumn(double[] buffer, int x, int z, int k, int l, CallbackInfo info) {
//		if (GeneratorOptions.useNewGenerator() && settings.get().stable(NoiseGeneratorSettings.END)) {
//			TerrainGenerator.fillTerrainDensity(buffer, x, z, getBiomeSource());
//			info.cancel();
//		}
//	}
	
	@Inject(method = "buildSurface", at = @At("HEAD"), cancellable = true)
	private void be_buildSurface(WorldGenRegion worldGenRegion, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess, CallbackInfo info) {
		//NoiseChunk noiseChunk = chunkAccess.getOrCreateNoiseChunk(this.sampler, () -> new Beardifier(structureFeatureManager, chunkAccess), noiseGeneratorSettings, this.globalFluidPicker, Blender.of(worldGenRegion));
		/*WorldGenerationContext worldGenerationContext = new WorldGenerationContext(this, worldGenRegion);
		NoiseGeneratorSettings noiseGeneratorSettings = this.settings.get();
		surfaceSystem.buildSurface(
			worldGenRegion.getBiomeManager(),
			worldGenRegion.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY),
			noiseGeneratorSettings.useLegacyRandomSource(),
			worldGenerationContext,
			chunkAccess,
			null,
			noiseGeneratorSettings.surfaceRule()
		);*/
		//System.out.println("Build surface!");
	}
	
	@Inject(method = "fillFromNoise", at = @At("HEAD"), cancellable = true)
	private void be_fillFromNoise(Executor executor, Blender blender, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess2, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> info) {
		info.setReturnValue(CompletableFuture.supplyAsync(
			Util.wrapThreadWithTaskName(
				"wgen_fill_noise",
				() -> this.fill(chunkAccess2)
			),
			Util.backgroundExecutor()
		));
	}
	
	private ChunkAccess fill(ChunkAccess chunkAccess) {
		ChunkPos chunkPos = chunkAccess.getPos();
		
		int px = chunkPos.x << 1;
		int pz = chunkPos.z << 1;
		double[][][] noiseColumns = new double[3][3][33];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				TerrainGenerator.fillTerrainDensity(noiseColumns[i][j], px + i, pz + j, biomeSource);
			}
		}
		
		// Testing
		BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
		
		Heightmap heightmap = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
		Heightmap heightmap2 = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
		
		short maxHeight = (short) (Math.min(127, chunkAccess.getMaxBuildHeight()) - chunkAccess.getMinBuildHeight());
		short start = (short) ((-chunkAccess.getMinBuildHeight()) >> 4);
		short end = (short) (maxHeight >> 4);
		
		//IntStream.range(start, end).parallel().forEach(sectionIndex -> {
		for (short sectionIndex = start; sectionIndex <= end; sectionIndex++) {
			LevelChunkSection section = chunkAccess.getSection(sectionIndex);
			for (byte y = 0; y < 16; y++) {
				short iy = (short) ((y >> 2) | ((sectionIndex - start) << 2));
				float dy = (y & 3) / 4F;
				for (byte x = 0; x < 16; x++) {
					float dx = (x & 7) / 8F;
					byte ix = (byte) (x >> 3);
					for (byte z = 0; z < 16; z++) {
						float dz = (z & 7) / 8F;
						byte iz = (byte) (z >> 3);
						
						float a = (float) noiseColumns[ix][iz][iy];
						float b = (float) noiseColumns[ix + 1][iz][iy];
						float c = (float) noiseColumns[ix][iz][iy + 1];
						float d = (float) noiseColumns[ix + 1][iz][iy + 1];
						
						float e = (float) noiseColumns[ix][iz + 1][iy];
						float f = (float) noiseColumns[ix + 1][iz + 1][iy];
						float g = (float) noiseColumns[ix][iz + 1][iy + 1];
						float h = (float) noiseColumns[ix + 1][iz + 1][iy + 1];
						
						a = Mth.lerp(dx, a, b);
						b = Mth.lerp(dx, c, d);
						c = Mth.lerp(dx, e, f);
						d = Mth.lerp(dx, g, h);
						
						a = Mth.lerp(dy, a, b);
						b = Mth.lerp(dy, c, d);
						
						if (Mth.lerp(dz, a, b) > 0) {
							section.setBlockState(x, y, z, defaultBlock);
							heightmap.update(x, y, z, defaultBlock);
							heightmap2.update(x, y, z, defaultBlock);
						}
						else if (iy > 0) {
							byte py = (byte) ((y - 1) & 15);
							LevelChunkSection section2 = y == 0 ? chunkAccess.getSection(sectionIndex - 1) : section;
							if (section2.getBlockState(x, py, z).equals(defaultBlock)) {
								section2.setBlockState(x, py, z, grass);
							}
						}
					}
				}
			}
		}
		//});
		
		return chunkAccess;
	}
}
