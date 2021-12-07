package ru.betterend.mixin.common;

import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ru.betterend.interfaces.StructureFeaturesAccessor;

@Mixin(StructureFeatures.class)
public class StructureFeaturesMixin implements StructureFeaturesAccessor {
    @Shadow @Final private static ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> END_CITY;

    public ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> getEND_CITY(){
        return END_CITY;
    }
}
