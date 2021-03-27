package ru.betterend.world.structures.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFRotation;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFCappedCone;
import ru.betterend.world.structures.piece.VoxelPiece;

public class GiantIceStarStructure extends SDFStructureFeature {
	private final float minSize = 20;
	private final float maxSize = 35;
	private final int minCount = 25;
	private final int maxCount = 40;
	
	@Override
	protected SDF getSDF(BlockPos pos, Random random) {
		float size = MHelper.randRange(minSize, maxSize, random);
		int count = MHelper.randRange(minCount, maxCount, random);
		List<Vector3f> points = getFibonacciPoints(count);
		SDF sdf = null;
		SDF spike = new SDFCappedCone().setRadius1(3 + (size - 5) * 0.2F).setRadius2(0).setHeight(size).setBlock(EndBlocks.DENSE_SNOW);
		spike = new SDFTranslate().setTranslate(0, size - 0.5F, 0).setSource(spike);
		for (Vector3f point: points) {
			SDF rotated = spike;
			point = MHelper.normalize(point);
			float angle = MHelper.angle(Vector3f.POSITIVE_Y, point);
			if (angle > 0.01F && angle < 3.14F) {
				Vector3f axis = MHelper.normalize(MHelper.cross(Vector3f.POSITIVE_Y, point));
				rotated = new SDFRotation().setRotation(axis, angle).setSource(spike);
			}
			else if (angle > 1) {
				rotated = new SDFRotation().setRotation(Vector3f.POSITIVE_Y, (float) Math.PI).setSource(spike);
			}
			sdf = (sdf == null) ? rotated : new SDFUnion().setSourceA(sdf).setSourceB(rotated);
		}
		
		final float ancientRadius = size * 0.7F;
		final float denseRadius = size * 0.9F;
		final float iceRadius = size < 7 ? size * 5 : size * 1.3F;
		final float randScale = size * 0.3F;
		
		final BlockPos center = pos;
		final BlockState ice = EndBlocks.EMERALD_ICE.getDefaultState();
		final BlockState dense = EndBlocks.DENSE_EMERALD_ICE.getDefaultState();
		final BlockState ancient = EndBlocks.ANCIENT_EMERALD_ICE.getDefaultState();
		final SDF sdfCopy = sdf;
		
		return sdf.addPostProcess((info) -> {
			BlockPos bpos = info.getPos();
			float px = bpos.getX() - center.getX();
			float py = bpos.getY() - center.getY();
			float pz = bpos.getZ() - center.getZ();
			float distance = MHelper.length(px, py, pz) + sdfCopy.getDistance(px, py, pz) * 0.4F + random.nextFloat() * randScale;
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
	
	private List<Vector3f> getFibonacciPoints(int count) {
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
	
	@Override
	public StructureFeature.StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
		return StarStructureStart::new;
	}
	
	public static class StarStructureStart extends StructureStart<DefaultFeatureConfig> {
		public StarStructureStart(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed) {
			super(feature, chunkX, chunkZ, box, references, seed);
		}

		@Override
		public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX, int chunkZ, Biome biome, DefaultFeatureConfig config) {
			int x = (chunkX << 4) | MHelper.randRange(4, 12, random);
			int z = (chunkZ << 4) | MHelper.randRange(4, 12, random);
			BlockPos start = new BlockPos(x, MHelper.randRange(32, 128, random), z);
			VoxelPiece piece = new VoxelPiece((world) -> { ((SDFStructureFeature) this.getFeature()).getSDF(start, this.random).fillRecursive(world, start); }, random.nextInt());
			this.children.add(piece);
			this.setBoundingBoxFromChildren();
		}
	}
}
