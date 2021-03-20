package ru.betterend.mixin.common;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import ru.betterend.BetterEnd;
import ru.betterend.util.DataFixerUtil;
import ru.betterend.util.WorldDataUtil;
import ru.betterend.world.generator.GeneratorOptions;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void be_onServerWorldInit(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> registryKey, DimensionType dimensionType, WorldGenerationProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator, boolean debugWorld, long l, List<Spawner> list, boolean bl, CallbackInfo info) {
		File beData = new File(FabricLoader.getInstance().getGameDir().getParent().toString(), "saves/" + properties.getLevelName() + "/data/betterend_data.nbt");
		ModMetadata meta = FabricLoader.getInstance().getModContainer(BetterEnd.MOD_ID).get().getMetadata();
		String version = BetterEnd.isDevEnvironment() ? "development" : meta.getVersion().toString();
		
		WorldDataUtil.load(beData);
		CompoundTag root = WorldDataUtil.getRootTag();
		String dataVersion = root.getString("version");
		GeneratorOptions.setPortalPos(NbtHelper.toBlockPos(root.getCompound("portal")));
		boolean fix = !dataVersion.equals(version);
		
		if (fix) {
			DataFixerUtil.fixData(beData.getParentFile());
			root.putString("version", version);
			WorldDataUtil.saveFile();
		}
	}

	@Inject(method = "getSpawnPos", at = @At("HEAD"), cancellable = true)
	private void be_getSpawnPos(CallbackInfoReturnable<BlockPos> info) {
		if (GeneratorOptions.changeSpawn()) {
			if (((ServerWorld) (Object) this).getRegistryKey() == World.END) {
				info.setReturnValue(GeneratorOptions.getSpawn());
				info.cancel();
			}
		}
	}
}
