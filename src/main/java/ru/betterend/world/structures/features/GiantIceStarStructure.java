package ru.betterend.world.structures.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFRotation;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.operator.SDFUnion;
import ru.bclib.sdf.primitive.SDFCappedCone;
import ru.bclib.util.MHelper;
import ru.betterend.registry.EndBlocks;
import ru.betterend.world.structures.piece.VoxelPiece;

public class GiantIceStarStructure extends SDFStructureFeature {
	private static final float minSize = 20;
	private static final float maxSize = 35;
	private static final int minCount = 25;
	private static final int maxCount = 40;

	public GiantIceStarStructure() {
		super(GiantIceStarStructure::generatePieces);
	}

	protected static SDF getSDF(BlockPos pos, Random random) {
		float size = MHelper.randRange(minSize, maxSize, random);
		int count = MHelper.randRange(minCount, maxCount, random);
		List<Vector3f> points = getFibonacciPoints(count);
		SDF sdf = null;
		SDF spike = new SDFCappedCone().setRadius1(3 + (size - 5) * 0.2F)
									   .setRadius2(0)
									   .setHeight(size)
									   .setBlock(EndBlocks.DENSE_SNOW);
		spike = new SDFTranslate().setTranslate(0, size - 0.5F, 0).setSource(spike);
		for (Vector3f point : points) {
			SDF rotated = spike;
			point = MHelper.normalize(point);
			float angle = MHelper.angle(Vector3f.YP, point);
			if (angle > 0.01F && angle < 3.14F) {
				Vector3f axis = MHelper.normalize(MHelper.cross(Vector3f.YP, point));
				rotated = new SDFRotation().setRotation(axis, angle).setSource(spike);
			}
			else if (angle > 1) {
				rotated = new SDFRotation().setRotation(Vector3f.YP, (float) Math.PI).setSource(spike);
			}
			sdf = (sdf == null) ? rotated : new SDFUnion().setSourceA(sdf).setSourceB(rotated);
		}
		
		final float ancientRadius = size * 0.7F;
		final float denseRadius = size * 0.9F;
		final float iceRadius = size < 7 ? size * 5 : size * 1.3F;
		final float randScale = size * 0.3F;
		
		final BlockPos center = pos;
		final BlockState ice = EndBlocks.EMERALD_ICE.defaultBlockState();
		final BlockState dense = EndBlocks.DENSE_EMERALD_ICE.defaultBlockState();
		final BlockState ancient = EndBlocks.ANCIENT_EMERALD_ICE.defaultBlockState();
		final SDF sdfCopy = sdf;
		
		return sdf.addPostProcess((info) -> {
			BlockPos bpos = info.getPos();
			float px = bpos.getX() - center.getX();
			float py = bpos.getY() - center.getY();
			float pz = bpos.getZ() - center.getZ();
			float distance = MHelper.length(px, py, pz) + sdfCopy.getDistance(
				px,
				py,
				pz
			) * 0.4F + random.nextFloat() * randScale;
			if (distance < ancientRadius) {
				return ancient;
			}
			else if (distance < denseRadius) {
				return dense;
			}
			else if (distance < iceRadius) {
				return ice;
			}
			return info.getState();
		});
	}
	
	private static List<Vector3f> getFibonacciPoints(int count) {
		float max = count - 1;
		List<Vector3f> result = new ArrayList<Vector3f>(count);
		for (int i = 0; i < count; i++) {
			float y = 1F - (i / max) * 2F;
			float radius = (float) Math.sqrt(1F - y * y);
			float theta = MHelper.PHI * i;
			float x = (float) Math.cos(theta) * radius;
			float z = (float) Math.sin(theta) * radius;
			result.add(new Vector3f(x, y, z));
		}
		return result;
	}
		
	public static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
		final Random random = context.random();
		final ChunkPos chunkPos = context.chunkPos();
		final ChunkGenerator chunkGenerator = context.chunkGenerator();
		final LevelHeightAccessor levelHeightAccessor = context.heightAccessor();

		int x = chunkPos.getBlockX(MHelper.randRange(4, 12, random));
		int z = chunkPos.getBlockZ(MHelper.randRange(4, 12, random));
		BlockPos start = new BlockPos(x, MHelper.randRange(32, 128, random), z);
		VoxelPiece piece = new VoxelPiece((world) -> {
			getSDF(start, random).fillRecursive(world, start);
		}, random.nextInt());
		structurePiecesBuilder.addPiece(piece);

		//this.calculateBoundingBox();
	}
}
