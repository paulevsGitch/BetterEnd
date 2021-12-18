package ru.betterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;

import java.util.Optional;
import java.util.Random;

public class LumecornSeedBlock extends EndPlantWithAgeBlock {

    @Override
    public void growAdult(WorldGenLevel world, Random random, BlockPos pos) {
        ((Feature<NoneFeatureConfiguration>) (EndFeatures.LUMECORN.getFeature())).place(new FeaturePlaceContext<>(
                Optional.empty(),
                world,
                null,
                random,
                pos,
                (NoneFeatureConfiguration) null));
    }

    @Override
    protected boolean isTerrain(BlockState state) {
        return state.is(EndBlocks.END_MOSS);
    }

    @Override
    public BlockBehaviour.OffsetType getOffsetType() {
        return BlockBehaviour.OffsetType.NONE;
    }
}
