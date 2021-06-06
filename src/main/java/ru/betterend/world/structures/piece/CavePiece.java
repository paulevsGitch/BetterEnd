package ru.betterend.world.structures.piece;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import ru.bclib.api.TagAPI;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndStructures;

public class CavePiece extends BasePiece {
	private OpenSimplexNoise noise;
	private BlockPos center;
	private float radius;
	
	public CavePiece(BlockPos center, float radius, int id) {
		super(EndStructures.CAVE_PIECE, id);
		this.center = center;
		this.radius = radius;
		this.noise = new OpenSimplexNoise(MHelper.getSeed(534, center.getX(), center.getZ()));
		makeBoundingBox();
	}

	public CavePiece(StructureManager manager, CompoundTag tag) {
		super(EndStructures.CAVE_PIECE, tag);
		makeBoundingBox();
	}
	
	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager arg, ChunkGenerator chunkGenerator, Random random, BoundingBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		int x1 = MHelper.max(this.boundingBox.x0, blockBox.x0);
		int z1 = MHelper.max(this.boundingBox.z0, blockBox.z0);
		int x2 = MHelper.min(this.boundingBox.x1, blockBox.x1);
		int z2 = MHelper.min(this.boundingBox.z1, blockBox.z1);
		int y1 = this.boundingBox.y0;
		int y2 = this.boundingBox.y1;
		
		double hr = radius * 0.75;
		double nr = radius * 0.25;
		MutableBlockPos pos = new MutableBlockPos();
		for (int x = x1; x <= x2; x++) {
			int xsq = x - center.getX();
			xsq *= xsq;
			pos.setX(x);
			for (int z = z1; z <= z2; z++) {
				int zsq = z - center.getZ();
				zsq *= zsq;
				pos.setZ(z);
				for (int y = y1; y <= y2; y++) {
					int ysq = y - center.getY();
					ysq *= 1.6;
					ysq *= ysq;
					pos.setY(y);
					double r = noise.eval(x * 0.1, y * 0.1, z * 0.1) * nr + hr;
					double r2 = r - 4.5;
					double dist = xsq + ysq + zsq;
					if (dist < r2 * r2) {
						if (world.getBlockState(pos).is(TagAPI.END_GROUND)) {
							BlocksHelper.setWithoutUpdate(world, pos, CAVE_AIR);
						}
					}
					else if (dist < r * r) {
						if (world.getBlockState(pos).getMaterial().isReplaceable()) {
							BlocksHelper.setWithoutUpdate(world, pos, Blocks.END_STONE);
						}
					}
				}
			}
		}
		
		return true;
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.put("center", NbtUtils.writeBlockPos(center));
		tag.putFloat("radius", radius);
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		center = NbtUtils.readBlockPos(tag.getCompound("center"));
		radius = tag.getFloat("radius");
		noise = new OpenSimplexNoise(MHelper.getSeed(534, center.getX(), center.getZ()));
	}
	
	private void makeBoundingBox() {
		int minX = MHelper.floor(center.getX() - radius);
		int minY = MHelper.floor(center.getY() - radius);
		int minZ = MHelper.floor(center.getZ() - radius);
		int maxX = MHelper.floor(center.getX() + radius + 1);
		int maxY = MHelper.floor(center.getY() + radius + 1);
		int maxZ = MHelper.floor(center.getZ() + radius + 1);
		this.boundingBox = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
}
