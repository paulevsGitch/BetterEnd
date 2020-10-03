package ru.betterend.world.features;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.BlockMossyGlowshroomCap;
import ru.betterend.blocks.BlockGlowingFur;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.PosInfo;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFBinary;
import ru.betterend.util.sdf.operator.SDFCoordModify;
import ru.betterend.util.sdf.operator.SDFFlatWave;
import ru.betterend.util.sdf.operator.SDFScale;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFCapedCone;
import ru.betterend.util.sdf.primitive.SDFPrimitive;
import ru.betterend.util.sdf.primitive.SDFSphere;

public class MossyGlowshroomFeature extends DefaultFeature {
	private static final Function<PosInfo, BlockState> POST_PROCESS;
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Vector3f CENTER = new Vector3f();
	private static final SDFBinary FUNCTION;
	private static final SDFTranslate HEAD_POS;
	private static final SDFFlatWave ROOTS_ROT;
	
	private static final SDFPrimitive CONE1;
	private static final SDFPrimitive CONE2;
	private static final SDFPrimitive CONE_GLOW;
	private static final SDFPrimitive ROOTS;
	
	private static final Mutable POS = new Mutable();
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig featureConfig) {
		blockPos = getPosOnSurface(world, blockPos);
		if (blockPos.getY() < 5) {
			return false;
		}
		if (!world.getBlockState(blockPos.down()).isIn(BlockTagRegistry.END_GROUND)) {
			return false;
		}
		
		CONE1.setBlock(BlockRegistry.MOSSY_GLOWSHROOM_CAP);
		CONE2.setBlock(BlockRegistry.MOSSY_GLOWSHROOM_CAP);
		CONE_GLOW.setBlock(BlockRegistry.MOSSY_GLOWSHROOM_HYMENOPHORE);
		ROOTS.setBlock(BlockRegistry.MOSSY_GLOWSHROOM.bark);
		
		float height = MHelper.randRange(10F, 25F, random);
		int count = MHelper.floor(height / 4);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, height, 0, count);
		SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
		SDF sdf = SplineHelper.buildSDF(spline, 2.1F, 1.5F, (pos) -> {
			return BlockRegistry.MOSSY_GLOWSHROOM.log.getDefaultState();
		});
		Vector3f pos = spline.get(spline.size() - 1);
		CENTER.set(blockPos.getX(), 0, blockPos.getZ());
		HEAD_POS.setTranslate(pos.getX(), pos.getY(), pos.getZ());
		ROOTS_ROT.setAngle(random.nextFloat() * MHelper.PI2);
		FUNCTION.setSourceA(sdf);
		float scale = MHelper.randRange(0.75F, 1.1F, random);
		
		Vector3f vec = spline.get(0);
		float x1 = blockPos.getX() + vec.getX() * scale;
		float y1 = blockPos.getY() + vec.getY() * scale;
		float z1 = blockPos.getZ() + vec.getZ() * scale;
		for (int i = 1; i < count; i++) {
			vec = spline.get(i);
			float x2 = blockPos.getX() + vec.getX() * scale;
			float y2 = blockPos.getY() + vec.getY() * scale;
			float z2 = blockPos.getZ() + vec.getZ() * scale;
			
			for (float py = y1; py < y2; py += 3) {
				if (py - blockPos.getY() < 10) continue;
				float lerp = (py - y1) / (y2 - y1);
				float x = MathHelper.lerp(lerp, x1, x2);
				float z = MathHelper.lerp(lerp, z1, z2);
				POS.set(x, py, z);
				BlockState state = world.getBlockState(POS);
				boolean generate = state.getMaterial().isReplaceable() || state.getMaterial().equals(Material.PLANT);
				if (!generate) {
					return false;
				}
			}
			
			x1 = x2;
			y1 = y2;
			z1 = z2;
		}
		BlocksHelper.setWithoutUpdate(world, blockPos, AIR);
		
		Set<BlockPos> blocks = new SDFScale()
				.setScale(scale)
				.setSource(FUNCTION)
				.setReplaceFunction(REPLACE)
				.setPostProcess(POST_PROCESS)
				.fillRecursive(world, blockPos);
		
		for (BlockPos bpos: blocks) {
			BlockState state = world.getBlockState(bpos);
			if (state.getBlock() == BlockRegistry.MOSSY_GLOWSHROOM_HYMENOPHORE) {
				if (world.isAir(bpos.north())) {
					BlocksHelper.setWithoutUpdate(world, bpos.north(), BlockRegistry.MOSSY_GLOWSHROOM_FUR.getDefaultState().with(BlockGlowingFur.FACING, Direction.NORTH));
				}
				if (world.isAir(bpos.east())) {
					BlocksHelper.setWithoutUpdate(world, bpos.east(), BlockRegistry.MOSSY_GLOWSHROOM_FUR.getDefaultState().with(BlockGlowingFur.FACING, Direction.EAST));
				}
				if (world.isAir(bpos.south())) {
					BlocksHelper.setWithoutUpdate(world, bpos.south(), BlockRegistry.MOSSY_GLOWSHROOM_FUR.getDefaultState().with(BlockGlowingFur.FACING, Direction.SOUTH));
				}
				if (world.isAir(bpos.west())) {
					BlocksHelper.setWithoutUpdate(world, bpos.west(), BlockRegistry.MOSSY_GLOWSHROOM_FUR.getDefaultState().with(BlockGlowingFur.FACING, Direction.WEST));
				}
				if (world.getBlockState(bpos.down()).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, bpos.down(), BlockRegistry.MOSSY_GLOWSHROOM_FUR.getDefaultState().with(BlockGlowingFur.FACING, Direction.DOWN));
				}
			}
			else if (BlockRegistry.MOSSY_GLOWSHROOM.isTreeLog(state) && random.nextBoolean() && world.getBlockState(bpos.up()).getBlock() == BlockRegistry.MOSSY_GLOWSHROOM_CAP) {
				BlocksHelper.setWithoutUpdate(world, bpos, BlockRegistry.MOSSY_GLOWSHROOM_CAP.getDefaultState().with(BlockMossyGlowshroomCap.TRANSITION, true));
				BlocksHelper.setWithoutUpdate(world, bpos.up(), BlockRegistry.MOSSY_GLOWSHROOM_CAP);
			}
		}
		
		return true;
	}
	
	static {
		SDFCapedCone cone1 = new SDFCapedCone().setHeight(2.5F).setRadius1(1.5F).setRadius2(2.5F);
		SDFCapedCone cone2 = new SDFCapedCone().setHeight(3F).setRadius1(2.5F).setRadius2(13F);
		SDF posedCone2 = new SDFTranslate().setTranslate(0, 5, 0).setSource(cone2);
		SDF posedCone3 = new SDFTranslate().setTranslate(0, 7F, 0).setSource(cone2);
		SDF upCone = new SDFSubtraction().setSourceA(posedCone2).setSourceB(posedCone3);
		SDF wave = new SDFFlatWave().setRaysCount(12).setIntensity(1.3F).setSource(upCone);
		SDF cones = new SDFSmoothUnion().setRadius(3).setSourceA(cone1).setSourceB(wave);
		
		CONE1 = cone1;
		CONE2 = cone2;
		
		SDF innerCone = new SDFTranslate().setTranslate(0, 1.25F, 0).setSource(upCone);
		innerCone = new SDFScale3D().setScale(1.2F, 1F, 1.2F).setSource(innerCone);
		cones = new SDFUnion().setSourceA(cones).setSourceB(innerCone);
		
		SDF glowCone = new SDFCapedCone().setHeight(3F).setRadius1(2F).setRadius2(12.5F);
		CONE_GLOW = (SDFPrimitive) glowCone;
		glowCone = new SDFTranslate().setTranslate(0, 4.25F, 0).setSource(glowCone);
		glowCone = new SDFSubtraction().setSourceA(glowCone).setSourceB(posedCone3);
		
		cones = new SDFUnion().setSourceA(cones).setSourceB(glowCone);
		
		OpenSimplexNoise noise = new OpenSimplexNoise(1234);
		cones = new SDFCoordModify().setFunction((pos) -> {
			float dist = MHelper.length(pos.getX(), pos.getZ());
			float y = pos.getY() + (float) noise.eval(pos.getX() * 0.1 + CENTER.getX(), pos.getZ() * 0.1 + CENTER.getZ()) * dist * 0.3F - dist * 0.15F;
			pos.set(pos.getX(), y, pos.getZ());
		}).setSource(cones);
		
		HEAD_POS = (SDFTranslate) new SDFTranslate().setSource(new SDFTranslate().setTranslate(0, 2.5F, 0).setSource(cones));
		
		SDF roots = new SDFSphere().setRadius(4F);
		ROOTS = (SDFPrimitive) roots;
		roots = new SDFScale3D().setScale(1, 0.7F, 1).setSource(roots);
		ROOTS_ROT = (SDFFlatWave) new SDFFlatWave().setRaysCount(5).setIntensity(1.5F).setSource(roots);
		
		FUNCTION = new SDFSmoothUnion().setRadius(4).setSourceB(new SDFUnion().setSourceA(HEAD_POS).setSourceB(ROOTS_ROT));
		
		REPLACE = (state) -> {
			if (state.getBlock() != Blocks.END_STONE && state.isIn(BlockTagRegistry.END_GROUND)) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
		
		POST_PROCESS = (info) -> {
			if (BlockRegistry.MOSSY_GLOWSHROOM.isTreeLog(info.getState())) {
				if (!BlockRegistry.MOSSY_GLOWSHROOM.isTreeLog(info.getStateUp()) || !BlockRegistry.MOSSY_GLOWSHROOM.isTreeLog(info.getStateDown())) {
					return BlockRegistry.MOSSY_GLOWSHROOM.bark.getDefaultState();
				}
			}
			else if (info.getState().getBlock() == BlockRegistry.MOSSY_GLOWSHROOM_CAP) {
				if (BlockRegistry.MOSSY_GLOWSHROOM.isTreeLog(info.getStateDown())) {
					return info.getState().with(BlockMossyGlowshroomCap.TRANSITION, true);
				}
				
				int air = 0;
				for (Direction dir: Direction.values()) {
					if (info.getState(dir).isAir()) {
						air ++;
					}
				}
				if (air > 4) {
					info.setState(AIR);
					return AIR;
				}
			}
			return info.getState();
		};
	}
}
