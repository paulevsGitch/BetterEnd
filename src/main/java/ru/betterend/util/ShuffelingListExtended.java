package ru.betterend.util;

import net.minecraft.world.entity.ai.behavior.ShufflingList;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

public interface ShuffelingListExtended<U> {
    public boolean isEmpty();
    public U getOne();
}
