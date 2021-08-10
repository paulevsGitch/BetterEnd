package ru.betterend.mixin.common;

import net.minecraft.core.BlockPos;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bclib.api.BiomeAPI;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlocks;
import ru.betterend.world.generator.GeneratorOptions;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
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
	}
	
	@Inject(method = "getSharedSpawnPos", at = @At("HEAD"), cancellable = true)
	private void be_getSharedSpawnPos(CallbackInfoReturnable<BlockPos> info) {
		if (GeneratorOptions.changeSpawn()) {
			if (ServerLevel.class.cast(this).dimension() == Level.END) {
				BlockPos pos = GeneratorOptions.getSpawn();
				info.setReturnValue(pos);
			}
		}
	}
	
	@Inject(method = "makeObsidianPlatform", at = @At("HEAD"), cancellable = true)
	private static void be_createObsidianPlatform(ServerLevel serverLevel, CallbackInfo info) {
		if (!GeneratorOptions.generateObsidianPlatform()) {
			info.cancel();
		}
		else if (GeneratorOptions.changeSpawn()) {
			BlockPos blockPos = GeneratorOptions.getSpawn();
			int i = blockPos.getX();
			int j = blockPos.getY() - 2;
			int k = blockPos.getZ();
			BlockPos.betweenClosed(i - 2, j + 1, k - 2, i + 2, j + 3, k + 2).forEach((blockPosx) -> {
				serverLevel.setBlockAndUpdate(blockPosx, Blocks.AIR.defaultBlockState());
			});
			BlockPos.betweenClosed(i - 2, j, k - 2, i + 2, j, k + 2).forEach((blockPosx) -> {
				serverLevel.setBlockAndUpdate(blockPosx, Blocks.OBSIDIAN.defaultBlockState());
			});
			info.cancel();
		}
	}
	
	@ModifyArg(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private BlockState be_modifyTickState(BlockPos pos, BlockState state) {
		if (state.is(Blocks.ICE)) {
			ResourceLocation biome = BiomeAPI.getBiomeID(getBiome(pos));
			if (biome.getNamespace().equals(BetterEnd.MOD_ID)) {
				state = EndBlocks.EMERALD_ICE.defaultBlockState();
			}
		}
		return state;
	}
}
