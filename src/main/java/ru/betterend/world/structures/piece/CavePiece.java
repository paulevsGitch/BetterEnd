package ru.betterend.world.structures.piece;

import java.util.Random;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndStructures;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

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
	public boolean place(WorldGenLevel world, StructureAccessor arg, ChunkGenerator chunkGenerator, Random random,
			BlockBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		int x1 = MHelper.max(this.boundingBox.minX, blockBox.minX);
		int z1 = MHelper.max(this.boundingBox.minZ, blockBox.minZ);
		int x2 = MHelper.min(this.boundingBox.maxX, blockBox.maxX);
		int z2 = MHelper.min(this.boundingBox.maxZ, blockBox.maxZ);
		int y1 = this.boundingBox.minY;
		int y2 = this.boundingBox.maxY;

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
						if (world.getBlockState(pos).isIn(EndTags.END_GROUND)) {
							BlocksHelper.setWithoutUpdate(world, pos, AIR);
						}
					} else if (dist < r * r) {
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
	protected void toNbt(CompoundTag tag) {
		tag.put("center", NbtHelper.fromBlockPos(center));
		tag.putFloat("radius", radius);
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		center = NbtHelper.toBlockPos(tag.getCompound("center"));
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
		this.boundingBox = new BlockBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
}
