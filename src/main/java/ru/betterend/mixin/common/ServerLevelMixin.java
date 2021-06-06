package ru.betterend.mixin.common;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import ru.bclib.api.BiomeAPI;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.DataFixerUtil;
import ru.betterend.util.WorldDataUtil;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
	private static final int BE_DEV_VERSION = be_getVersionInt("63.63.63");
	private static final int BE_FIX_VERSION = BE_DEV_VERSION;
	private static String be_lastWorld = null;
	
	protected ServerLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, DimensionType dimensionType, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l) {
		super(writableLevelData, resourceKey, dimensionType, supplier, bl, bl2, l);
	}
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void be_onServerWorldInit(MinecraftServer server, Executor workerExecutor, LevelStorageSource.LevelStorageAccess session, ServerLevelData properties, ResourceKey<Level> registryKey, DimensionType dimensionType, ChunkProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator, boolean debugWorld, long l, List<CustomSpawner> list, boolean bl, CallbackInfo info) {
		if (be_lastWorld != null && be_lastWorld.equals(session.getLevelId())) {
			return;
		}
		
		be_lastWorld = session.getLevelId();
		
		ServerLevel world = ServerLevel.class.cast(this);
		EndBiomes.onWorldLoad(world.getSeed());
		File dir = session.getDimensionPath(world.dimension());
		if (!new File(dir, "level.dat").exists()) {
			dir = dir.getParentFile();
		}
		File data = new File(dir, "data/betterend_data.nbt");
		
		ModMetadata meta = FabricLoader.getInstance().getModContainer(BetterEnd.MOD_ID).get().getMetadata();
		int version = BetterEnd.isDevEnvironment() ? BE_DEV_VERSION : be_getVersionInt(meta.getVersion().toString());
		
		WorldDataUtil.load(data);
		CompoundTag root = WorldDataUtil.getRootTag();
		int dataVersion = be_getVersionInt(root.getString("version"));
		GeneratorOptions.setPortalPos(NbtUtils.readBlockPos(root.getCompound("portal")));
		
		if (dataVersion < version) {
			if (version < BE_FIX_VERSION) {
				DataFixerUtil.fixData(data.getParentFile());
			}
			root.putString("version", be_getVersionString(version));
			WorldDataUtil.saveFile();
		}
	}

	@Inject(method = "getSharedSpawnPos", at = @At("HEAD"), cancellable = true)
	private void be_getSharedSpawnPos(CallbackInfoReturnable<BlockPos> info) {
		if (GeneratorOptions.changeSpawn()) {
			if (ServerLevel.class.cast(this).dimension() == Level.END) {
				info.setReturnValue(GeneratorOptions.getSpawn());
				info.cancel();
			}
		}
	}
	
	@ModifyArg(
		method = "tickChunk",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
		)
	)
	private BlockState be_modifyTickState(BlockPos pos, BlockState state) {
		if (state.is(Blocks.ICE)) {
			ResourceLocation biome = BiomeAPI.getBiomeID(getBiome(pos));
			if (biome.getNamespace().equals(BetterEnd.MOD_ID)) {
				state = EndBlocks.EMERALD_ICE.defaultBlockState();
			}
		}
		return state;
	}
	
	private static int be_getVersionInt(String version) {
		if (version.isEmpty()) {
			return 0;
		}
		try {
			String[] values = version.split("\\.");
			return Integer.parseInt(values[0]) << 12 | Integer.parseInt(values[1]) << 6 | Integer.parseInt(values[1]);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	private static String be_getVersionString(int version) {
		int a = (version >> 12) & 63;
		int b = (version >> 6) & 63;
		int c = version & 63;
		return String.format(Locale.ROOT, "%d.%d.%d", a, b, c);
	}
}
