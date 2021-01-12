package ru.betterend.integration.byg.features;

import java.util.List;
import java.util.Random;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.integration.Integrations;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.PosInfo;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFFlatWave;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.primitive.SDFCappedCone;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class NightshadeRedwoodTreeFeature extends DefaultFeature {
	private static final List<Vector3f> BRANCH;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).getBlock().isIn(EndTags.END_GROUND)) return false;
		
		BlockState log = Integrations.BYG.getDefaultState("nightshade_log");
		BlockState wood = Integrations.BYG.getDefaultState("nightshade_wood");
		BlockState leaves = Integrations.BYG.getDefaultState("nightshade_leaves");
		//BlockState leaves_flower = Integrations.BYG.getDefaultState("flowering_nightshade_leaves");
		
		Function<BlockPos, BlockState> splinePlacer = (bpos) -> { return log; };
		Function<BlockState, Boolean> replace = (state) -> {
			return state.isIn(EndTags.END_GROUND) || state.getMaterial().equals(Material.PLANT) || state.getMaterial().isReplaceable();
		};
		Function<PosInfo, BlockState> post = (info) -> {
			if (info.getState().equals(log) && (!info.getStateUp().equals(log) || !info.getStateDown().equals(log))) {
				return wood;
			}
			return info.getState();
		};
		Function<BlockState, Boolean> ignore = (state) -> {
			return state.equals(log) || state.equals(wood);
		};
		
		int height = MHelper.randRange(40, 60, random);
		List<Vector3f> trunk = SplineHelper.makeSpline(0, 0, 0, 0, height, 0, height / 4);
		SplineHelper.offsetParts(trunk, random, 0.8F, 0, 0.8F);
		
		if (!SplineHelper.canGenerate(trunk, pos, world, replace)) {
			return false;
		}
		
		int count = height >> 2;
		float start = trunk.size() / 3F;
		float delta = trunk.size() * 0.6F;
		float max = height - 7;
		//SDF leavesSDF = null;
		for (int i = 0; i < count; i++) {
			float scale = (float) (count - i) / count * 15;
			Vector3f offset = SplineHelper.getPos(trunk, (float) i / count * delta + start);
			if (offset.getY() > max) {
				break;
			}
			List<Vector3f> branch = SplineHelper.copySpline(BRANCH);
			SplineHelper.rotateSpline(branch, i * 1.3F);
			SplineHelper.scale(branch, scale);
			SplineHelper.offsetParts(branch, random, 0.3F, 0.3F, 0.3F);
			SplineHelper.offset(branch, offset);
			SplineHelper.fillSpline(branch, world, wood, pos, replace);
			
			/*SDF leaf = null;
			for (int j = 2; j < 5; j++) {
				Vector3f point = branch.get(j);
				float radius = (float) (j - 2) / 3F;
				radius = (1F - radius) * 3F;
				SDF sphere = new SDFSphere().setRadius(radius).setBlock(leaves);
				sphere = new SDFTranslate().setTranslate(point.getX(), point.getY(), point.getZ()).setSource(sphere);
				leaf = leaf == null ? sphere : new SDFUnion().setSourceA(leaf).setSourceB(leaf);
			}*/
			
			/*SDF leaf = SplineHelper.buildSDF(branch, (del) -> {
				float radius = 1 - del;
				radius = radius * radius * 2 - 1;
				radius *= radius;
				radius = (1 - radius) * 2;
				return radius;
			}, (bpos) -> {
				return leaves;
			});*/
			//leavesSDF = leavesSDF == null ? leaf : new SDFUnion().setSourceA(leavesSDF).setSourceB(leaf);
		}
		
		SDF sdf = SplineHelper.buildSDF(trunk, 2.3F, 0.8F, splinePlacer);
		SDF roots = new SDFSphere().setRadius(2F).setBlock(log);
		roots = new SDFFlatWave().setIntensity(2F).setRaysCount(MHelper.randRange(5, 7, random)).setAngle(random.nextFloat() * MHelper.PI2).setSource(roots);
		sdf = new SDFSmoothUnion().setRadius(2F).setSourceA(sdf).setSourceB(roots);
		sdf.setReplaceFunction(replace).setPostProcess(post).fillRecursive(world, pos);
		
		/*if (leavesSDF != null) {
			leavesSDF.fillArea(world, pos, new Box(pos).expand(20, 80, 20));
		}*/
		
		Mutable mut = new Mutable();
		Function<PosInfo, BlockState> leavesPost = (info) -> {
			if (info.getState().equals(log) || info.getState().equals(wood)) {
				for (int x = -6; x < 7; x++) {
					int ax = Math.abs(x);
					mut.setX(x + info.getPos().getX());
					for (int z = -6; z < 7; z++) {
						int az = Math.abs(z);
						mut.setZ(z + info.getPos().getZ());
						for (int y = -6; y < 7; y++) {
							int ay = Math.abs(y);
							int d = ax + ay + az;
							if (d < 7) {
								mut.setY(y + info.getPos().getY());
								BlockState state = info.getState(mut);
								if (state.getBlock() instanceof LeavesBlock) {
									int distance = state.get(LeavesBlock.DISTANCE);
									if (d < distance) {
										info.setState(mut, state.with(LeavesBlock.DISTANCE, d));
									}
								}
							}
						}
					}
				}
			}
			else if (info.getState().getBlock() instanceof LeavesBlock) {
				int distance = info.getState().get(LeavesBlock.DISTANCE);
				return distance > MHelper.randRange(3, 5, random) ? Blocks.AIR.getDefaultState() : info.getState();
			}
			return info.getState();
		};
		
		SDF canopy = new SDFCappedCone().setRadius1(12F).setRadius2(3f).setHeight(height * 0.3F).setBlock(leaves);
		canopy.setPostProcess(leavesPost ).fillRecursiveIgnore(world, pos.add(0, height * 0.75, 0), ignore);
		
		return true;
	}
	
	//private void makeLeavesSphere(StructureWorldAccess world, BlockPos pos, BlockState leaves, Function<BlockState, Boolean> ignore) {
	//	
	//}
	
	static {
		BRANCH = Lists.newArrayList(new Vector3f(0, 0, 0),
				new Vector3f(0.25F, 0.1F, 0),
				new Vector3f(0.40F, 0.2F, 0),
				new Vector3f(0.50F, 0.4F, 0),
				new Vector3f(0.55F, 0.6F, 0));
	}
}
