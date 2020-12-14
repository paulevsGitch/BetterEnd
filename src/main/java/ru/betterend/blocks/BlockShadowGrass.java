package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.registry.EndParticles;

public class BlockShadowGrass extends BlockTerrain {
	public BlockShadowGrass() {
		super(MaterialColor.BLACK);
	}

	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (random.nextInt(32) == 0) {
			world.addParticle(EndParticles.BLACK_SPORE, (double) pos.getX() + random.nextDouble(), (double) pos.getY() + 1.1D, (double) pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
		}
	}
}
