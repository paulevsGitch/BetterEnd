package ru.betterend.world.features.terrain;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import ru.bclib.api.TagAPI;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFDisplacement;
import ru.bclib.sdf.operator.SDFRotation;
import ru.bclib.sdf.primitive.SDFTorus;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.noise.OpenSimplexNoise;

import java.util.Random;

public class ArchFeature extends DefaultFeature {
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        final WorldGenLevel world = featurePlaceContext.level();
        BlockPos origin = featurePlaceContext.origin();
        Random random = featurePlaceContext.random();

        BlockPos pos = getPosOnSurfaceWG(world, new BlockPos((origin.getX() & 0xFFFFFFF0) | 7, 0, (origin.getZ() & 0xFFFFFFF0) | 7));
        if (!world.getBlockState(pos.below(5)).is(TagAPI.BLOCK_GEN_TERRAIN)) {
            return false;
        }

        float bigRadius = MHelper.randRange(10F, 20F, random);
        float smallRadius = MHelper.randRange(3F, 7F, random);
        if (smallRadius + bigRadius > 23) {
            smallRadius = 23 - bigRadius;
        }
        SDF arch = new SDFTorus().setBigRadius(bigRadius).setSmallRadius(smallRadius).setBlock(Blocks.DIAMOND_BLOCK);
        arch = new SDFRotation().setRotation(MHelper.randomHorizontal(random), (float) Math.PI * 0.5F).setSource(arch);

        final float smallRadiusF = smallRadius;
        OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
        arch = new SDFDisplacement().setFunction((vec) -> {
            return (float) (Math.abs(noise.eval(
                    vec.x() * 0.1,
                    vec.y() * 0.1,
                    vec.z() * 0.1
            )) * 3F + Math.abs(noise.eval(vec.x() * 0.3, vec.y() * 0.3 + 100, vec.z() * 0.3)) * 1.3F) - smallRadiusF * Math.abs(1 - vec.y() / bigRadius);
        }).setSource(arch);

        arch.fillArea(world, pos, AABB.ofSize(Vec3.atCenterOf(pos), 46, 46, 46));
        return true;
    }
}
